package concert.common.exception;

import concert.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {

        int statusCode = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(statusCode)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST,e.getMessage(),null));
    }
}
