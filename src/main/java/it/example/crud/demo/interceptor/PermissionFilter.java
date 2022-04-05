package it.example.crud.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.example.crud.demo.controller.UserApiController;
import it.example.crud.demo.exception.UserException401;
import it.example.crud.demo.models.ErrorResponse;
import it.example.crud.demo.models.db.PermissionTable;
import it.example.crud.demo.permission.Permission;
import it.example.crud.demo.repository.PermissionRepository;
import org.aspectj.lang.reflect.MethodSignature;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Order(2)
public class PermissionFilter implements Filter {

    @Autowired
    private PermissionRepository permissionRepository;

    private List<String> pathToExclude = Arrays.asList("/api-docs", "/swagger-resources/**", "/swagger-ui.html",
            "/webjars/**", "/csrf", "/", "/v2/api-docs");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (!pathToExclude.stream().anyMatch(p -> pathMatcher.match(p, httpRequest.getServletPath()))) {
            Optional<String> user = Optional.ofNullable(httpRequest.getHeader("username"));
            Optional<String> pw = Optional.ofNullable(httpRequest.getHeader("password"));
            if (!user.isEmpty() && !pw.isEmpty()) {

                Set<Method> methodsAnnotatedWith = new Reflections(UserApiController.class, new MethodAnnotationsScanner()).getMethodsAnnotatedWith(Permission.class);
                for (Method m : methodsAnnotatedWith) {
                    if (m.isAnnotationPresent(Permission.class)) {
                        Permission annotation = m.getAnnotation(Permission.class);
                        Optional<PermissionTable> first = permissionRepository.findAll().stream().filter(permission -> permission.getUsername().equals(user.get())).findFirst();
                        if (first.isPresent() && first.get().getPassword().equals(pw.get())) {
                            if (!first.get().getRole().equals(annotation.role())) {
                                metElse(servletResponse);
                                return;
                            }
                        } else {
                            metElse(servletResponse);
                            return;
                        }

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
