package com.nlnt.philokalo_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

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
    USERNAME_PATTERN_INVALID(
            1005, "Only letters, numbers, and underscores are allowed — no special characters", HttpStatus.BAD_REQUEST),
    // Password
    PASSWORD_INVALID(1006, "Password must be at least 8 characters and max 255 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1007, "Password required", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN_INVALID(
            1008,
            "Password at least: 1 lowercase letter, 1 uppercase letter, 1 number, 1 special character",
            HttpStatus.BAD_REQUEST),
    // EMAIL
    EMAIL_EXISTED(1009, "User's email already existed", HttpStatus.CONFLICT),
    EMAIL_REQUIRED(1010, "Email required", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1011, "Invalid email and at least {min} characters", HttpStatus.BAD_REQUEST),
    // Permission
    PERMISSION_NAME_EXISTED(1012, "Permission has been named", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTS(1014, "Permission not exists", HttpStatus.BAD_REQUEST),
    // Role
    ROLE_NAME_EXISTED(1015, "Role has been named", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTS(1016, "Role not exists", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED(1017, "Role name required", HttpStatus.BAD_REQUEST),
    // Size
    SIZE_ERROR(1013, "Max size is 50", HttpStatus.BAD_REQUEST),
    // File
    FILE_EMPTY(1020, "File can not blank", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_SUPPORTED(1021, "Type not supported", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1022, "File to large (< 10MB)", HttpStatus.BAD_REQUEST),
    UPLOAD_FAILED(1023, "Upload fail", HttpStatus.BAD_REQUEST),
    DELETE_FAILED(1024, "Delete fail", HttpStatus.BAD_REQUEST),
    // Artwork
    ARTWORK_TITLE_REQUIRED(1030, "Artwork title required", HttpStatus.BAD_REQUEST),
    ARTWORK_TITLE_INVALID(1031, "Artwork title is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_DESCRIPTION_INVALID(1032, "Artwork description is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_MEDIUM_INVALID(1033, "Artwork medium is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_STATUS_REQUIRED(1034, "Artwork status required", HttpStatus.BAD_REQUEST),
    ARTWORK_STATUS_INVALID(1035, "Artwork status is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_PRICE_INVALID(1036, "Artwork price is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_CURRENCY_INVALID(1037, "Artwork currency must contain exactly 3 characters", HttpStatus.BAD_REQUEST),
    ARTWORK_ID_REQUIRED(1038, "Artwork id required", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_ORIGINAL_URL_REQUIRED(1039, "Original image URL required", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_THUMB_URL_REQUIRED(1040, "Thumbnail image URL required", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_BLUR_HASH_INVALID(1041, "Blur hash is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_WIDTH_INVALID(1042, "Image width is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_HEIGHT_INVALID(1043, "Image height is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_FILE_SIZE_INVALID(1044, "Image file size is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_FORMAT_INVALID(1045, "Image format is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_IMAGE_SORT_ORDER_INVALID(1046, "Image sort order is invalid", HttpStatus.BAD_REQUEST),
    ARTWORK_COMMENT_CONTENT_REQUIRED(1047, "Comment content required", HttpStatus.BAD_REQUEST),
    ARTWORK_COMMENT_CONTENT_INVALID(1048, "Comment content is invalid", HttpStatus.BAD_REQUEST),
    // Category
    CATEGORY_NAME_REQUIRED(1050, "Category name required", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_INVALID(1051, "Category name is invalid", HttpStatus.BAD_REQUEST),
    CATEGORY_SLUG_REQUIRED(1052, "Category slug required", HttpStatus.BAD_REQUEST),
    CATEGORY_SLUG_INVALID(1053, "Category slug is invalid", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    private ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
