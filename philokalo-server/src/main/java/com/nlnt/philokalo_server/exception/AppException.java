package com.nlnt.philokalo_server.exception;

/**
 *
 * @author nghia
 */
public class AppException extends RuntimeException {
    
    private ErrorCode errorCode;
    
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * @return the errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
}
