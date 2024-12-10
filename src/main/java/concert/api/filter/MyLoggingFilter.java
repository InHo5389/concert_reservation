package concert.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청,응답 로그를 찍고 요청-응답 시간을 구하는 필터
 */
@Slf4j
@Component
public class MyLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String threadName = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();

        logRequest(requestWrapper, threadName);

        filterChain.doFilter(requestWrapper, responseWrapper);

        logResponse(responseWrapper, threadName, System.currentTimeMillis() - startTime);

        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request, String threadName) {
        String queryString = request.getQueryString();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String payload = getRequestPayload(request);

        Map<String, String[]> paramMap = new HashMap<>(request.getParameterMap());

        log.info("[REQUEST][Thread: {}] METHOD: {}; URL: {}; Query: {}; Payload: {}; Params: {}",
                threadName, method, url, queryString, payload, paramMap);
    }

    private void logResponse(ContentCachingResponseWrapper response, String threadName, long timeTaken) {
        String responseBody = getResponsePayload(response);
        int status = response.getStatus();

        log.info("[RESPONSE][Thread: {}] Status: {}; Body: {}; Time: {} ms",
                threadName, status, responseBody, timeTaken);
    }

    private String getRequestPayload(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                log.error("Failed to parse request payload", e);
                return "[Error parsing payload]";
            }
        }
        return "[Empty]";
    }

    private String getResponsePayload(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, response.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                log.error("Failed to parse response payload", e);
                return "[Error parsing payload]";
            }
        }
        return "[Empty]";
    }
}
