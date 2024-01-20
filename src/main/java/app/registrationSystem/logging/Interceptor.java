package app.registrationSystem.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Interceptor implements HandlerInterceptor {
    private static final Logger log = LogManager.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NotNull Object handler) {

        log.info("Request: [{}] {}", request.getMethod(), request.getRequestURL());
        return true;
    }

    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NotNull Object handler,
            ModelAndView modelAndView) {

        log.info("Response: [STATUS {}]", response.getStatus());
    }

}
