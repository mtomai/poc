package it.example.crud.demo.aspect;

import it.example.crud.demo.exception.UserException401;
import it.example.crud.demo.models.User;
import it.example.crud.demo.models.db.PermissionTable;
import it.example.crud.demo.permission.Permission;
import it.example.crud.demo.repository.PermissionRepository;
import it.example.crud.demo.repository.UserRepository;
import it.example.crud.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import static org.springframework.boot.env.ConfigTreePropertySource.PropertyFile.findAll;

/*@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Autowired
    private PermissionRepository permissionRepository;

    //serve per far arrivare all'eccezione 401 enon far partire la 500
    @AfterThrowing(throwing = "e")
    public void myAfterThrowing(JoinPoint joinPoint, UserException401 ue) {
        System.out.println("IDS HABBENING");
    }

    @Around("execution(* * (..)) && @annotation( it.example.crud.demo.permission.Permission)")
    public Object aroundAdviceCache(ProceedingJoinPoint pjp) throws Throwable {

        log.info("CIAOOOOO!");


        //salvare username e password dall'header
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String user = request.getHeader("username");
        String pw = request.getHeader("password");

        Optional<PermissionTable> first = permissionRepository.findAll().stream().filter(permission -> permission.getUsername().equals(user)).findFirst();

        if (first.isPresent() && first.get().getPassword().equals(pw)) {
            //recuperare il valore dell'annotation
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Permission perm = method.getAnnotation(Permission.class);
            if (first.get().getRole().equals(perm)) {
                Object proceed = pjp.proceed();
                return proceed;
            } else{
                Object proceed = pjp.proceed();
                throw new UserException401("invalid role");
            }
        } else {
            Object proceed = pjp.proceed();
            throw new UserException401("invalid permission");
        }


    }

}*/
