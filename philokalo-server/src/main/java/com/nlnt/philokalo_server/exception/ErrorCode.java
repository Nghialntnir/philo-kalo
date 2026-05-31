package com.nlnt.philokalo_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 *
 * @author nghia
 */
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MESSAGE_KEY(0000, "Not found message key", HttpStatus.NOT_FOUND),
    // Auth
    UNAUTHENTICATED(999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(998, "You do not have permission", HttpStatus.FORBIDDEN),
    // Username
    USERNAME_EXISTED(1001, "Username already existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_REQUIRED(1003, "Username required", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Username must be at least 3 characters and max 50 characteres", HttpStatus.BAD_REQUEST),
    USERNAME_PATTERN_INVALID(1005, "Only letters, numbers, and underscores are allowed — no special characters", HttpStatus.BAD_REQUEST),
    // Password
    PASSWORD_INVALID(1006, "Password must be at least 8 characters and max 255 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1007, "Password required", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN_INVALID(1008, "Password at least: 1 lowercase letter, 1 uppercase letter, 1 number, 1 special character", HttpStatus.BAD_REQUEST),
    // EMAIL
    EMAIL_EXISTED(1009, "User's email already existed", HttpStatus.CONFLICT),
    EMAIL_REQUIRED(1010, "Email required", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1011, "Invalid email format", HttpStatus.BAD_REQUEST),
    // Permission
    PERMISSION_NAME_EXISTED(1012, "Permission has been named", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTS(1014, "Permission not exists", HttpStatus.BAD_REQUEST),
    // Role
    ROLE_NAME_EXISTED(1015, "Role has been named", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTS(1016, "Role not exists", HttpStatus.BAD_REQUEST),
    // Size
    SIZE_ERROR(1013, "Max size is 50", HttpStatus.BAD_REQUEST);
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    private ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
