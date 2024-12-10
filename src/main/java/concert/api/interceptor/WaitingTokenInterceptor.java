package concert.api.interceptor;

import concert.domain.token.jwt.WaitingTokenValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingTokenInterceptor implements HandlerInterceptor {

    private final WaitingTokenValidator waitingTokenValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String waitingToken = request.getHeader("WaitingToken");
        Long userId = waitingTokenValidator.isTokenActive(waitingToken);
        request.setAttribute("userId",userId);
        return true;
    }
}
