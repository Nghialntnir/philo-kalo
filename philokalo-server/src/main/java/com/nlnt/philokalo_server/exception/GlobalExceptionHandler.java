package com.nlnt.philokalo_server.exception;

import com.nlnt.philokalo_server.dto.response.ApiResponse;
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
// Controller -> throw exception -> spring interception -> @ControllerAdvice -> tìm @ExceptionHanlder phù hợp -> trả về respone tự định nghĩa
public class GlobalExceptionHandler {

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
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class) // Throw exception của Dto
    ResponseEntity<ApiResponse> handlerArgumentException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorCode = ErrorCode.NOT_FOUND_MESSAGE_KEY;
        }
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

}
