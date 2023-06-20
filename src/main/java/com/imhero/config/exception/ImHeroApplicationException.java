package com.imhero.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImHeroApplicationException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public ImHeroApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        }
        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
