package com.nlnt.philokalo_server.exception;

import com.nlnt.philokalo_server.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.access.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author nghia
 */
@Slf4j
@ControllerAdvice
// Controller -> Throw exception -> Spring interception -> @ControllerAdvice -> Tìm @ExceptionHanlder phù hợp -> trả về respone tự định nghĩa
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = RuntimeException.class) // Throw lỗi không kiểm soát đc
    ResponseEntity<ApiResponse> handlerRuntimeException(RuntimeException ex) {
        log.error("Uncategorized exception: ", ex);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.internalServerError().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class) // Throw lỗi tự định nghĩa
    ResponseEntity<ApiResponse> handlerAppException(AppException ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ex.getErrorCode().getCode());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity
                .status(ex.getErrorCode().getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlerAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class) // Throw exception của Dto
    ResponseEntity<ApiResponse> handlerArgumentException(MethodArgumentNotValidException ex) {
        var fieldError = ex.getFieldError();
        String enumKey = fieldError != null ? fieldError.getDefaultMessage() : "UNCATEGORIZED_EXCEPTION";
        ErrorCode errorCode;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constrainViolation = ex.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        } catch (IllegalArgumentException illegalArgumentException) {
            errorCode = ErrorCode.NOT_FOUND_MESSAGE_KEY;
        }
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) 
                ? mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
