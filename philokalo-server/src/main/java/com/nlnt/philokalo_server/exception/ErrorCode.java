package com.nlnt.philokalo_server.exception;

/**
 *
 * @author nghia
 */
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    NOT_FOUND_MESSAGE_KEY(0000, "Not found message key"),
    // Username
    USERNAME_EXISTED(1001, "Username already existed"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_REQUIRED(1003, "Username required"),
    USERNAME_INVALID(1004, "Username must be at least 3 characters and max 50 characteres"),
    USERNAME_PATTERN_INVALID(1005, "Only letters, numbers, and underscores are allowed — no special characters"),
    // Password
    PASSWORD_INVALID(1006, "Password must be at least 8 characters and max 255 characters"),
    PASSWORD_REQUIRED(1007, "Password required"),
    PASSWORD_PATTERN_INVALID(1008, "Password at least: 1 lowercase letter, 1 uppercase letter, 1 number, 1 special character"),
    // EMAIL
    EMAIL_EXISTED(1009, "User's email already existed"),
    EMAIL_REQUIRED(1010, "Email required"),
    EMAIL_INVALID(1011, "Invalid email format");

    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

}
