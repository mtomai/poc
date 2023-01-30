package it.example.crud.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import it.example.crud.demo.controller.UserApiController;
import it.example.crud.demo.models.ErrorResponse;
import it.example.crud.demo.models.db.CompositeKey;
import it.example.crud.demo.models.db.PermissionPlusTable;
import it.example.crud.demo.models.db.PermissionTable;
import it.example.crud.demo.permission.Permission;
import it.example.crud.demo.permission.PermissionPlus;
import it.example.crud.demo.repository.PermissionPlusRepository;
import it.example.crud.demo.repository.PermissionRepository;
import it.example.crud.demo.utils.Vault;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
@Order(1)
public class PermissionPlusFilter implements Filter {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionPlusRepository permissionPlusRepository;
    @Autowired
    private Vault vault;

    private List<String> pathToExclude = Arrays.asList("/api-docs", "/swagger-resources/**", "/swagger-ui.html",
            "/webjars/**", "/csrf", "/", "/v2/api-docs");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String jwt = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        //header contiene la crittografia e body le informazioni in più per cui si usano i jwt
        //Jwts.parser().parse(jwt).getBody();
        if(jwt!=null && !jwt.equals("")) {
            Claims claims = Jwts.parser().setSigningKey(vault.getKey()).parseClaimsJws(jwt).getBody();
            claims.getSubject();
            Optional<PermissionTable> permissionTable = permissionRepository.findById(Long.parseLong(claims.getSubject()));
            //controllo quali metodi sono annotati con @PermissionPlus
            Set<Method> methodsAnnotatedWith = new Reflections(UserApiController.class, new MethodAnnotationsScanner()).getMethodsAnnotatedWith(PermissionPlus.class);
            log.debug("metodi annotati con @PermissionPlus: "+ methodsAnnotatedWith);
            Iterator<Method> iterator = methodsAnnotatedWith.iterator();
            while (iterator.hasNext()) {
                Method next = iterator.next();
                if (next.isAnnotationPresent(PermissionPlus.class)) {
                    PermissionPlus annotation = next.getAnnotation(PermissionPlus.class);
                    log.debug("annotation: "+ annotation);
                    if (permissionTable.isPresent()) {
                        String role = permissionTable.get().getRole();
                        log.debug("role: "+ role);
                        List<PermissionPlusTable> permissionPlusTableList = permissionPlusRepository.findByRole(role);

                        //lista che contiene le operazioni permesse a quel determinato ruolo
                        List<String> operationsPerm = new ArrayList<>();
                        for (PermissionPlusTable p : permissionPlusTableList) {
                            operationsPerm.add(p.getCompositeKey().getOperation());
                        }
                        log.debug("Authorized operations : "+ operationsPerm);
                        if (operationsPerm.contains(annotation.operationType())) {
                            break;
                        }else if(!iterator.hasNext()){
                            log.debug("L'operazione non è autorizzata per questo ruolo");
                            metElse(servletResponse);
                            return;
                        }

                    } else {
                        log.debug("L'utente non esiste");
                        metElse(servletResponse);
                        return;
                    }

                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    //creo un metodo else per non doverlo scrivere più volte
    private void metElse(ServletResponse servletResponse) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("AUTH-00", HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid Authorization header!");

        byte[] responseToSend = restResponseBytes(errorResponse);
        ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
        ((HttpServletResponse) servletResponse).setStatus(401);
        servletResponse.getOutputStream().write(responseToSend);
    }


    //metodo per scrivere il messaggio della response
    private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }

}
