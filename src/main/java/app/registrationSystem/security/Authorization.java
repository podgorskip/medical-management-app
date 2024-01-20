package app.registrationSystem.security;

import app.registrationSystem.exceptions.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Aspect
@Component
public class Authorization {

    @Before("@annotation(app.registrationSystem.security.RequiredPrivilege)")
    public void authorize(JoinPoint joinPoint) {
        Privilege privilege = extractPrivilege(joinPoint);

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CustomUserDetails customUserDetails && isUnauthorized(customUserDetails, privilege)) {
                throw new UnauthorizedException("User lacked privilege to perform the task");
            }
        }

    }

    private boolean isUnauthorized(UserDetails userDetails, Privilege privilege) {
        return Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals(privilege.name()));
    }

    private Privilege extractPrivilege(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RequiredPrivilege privilege = methodSignature.getMethod().getAnnotation(RequiredPrivilege.class);

        if (Objects.isNull(privilege)) {
            throw new UnauthorizedException("User lacked privilege to perform the task");
        }

        return privilege.value();
    }
}
