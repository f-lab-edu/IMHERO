package com.imhero.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER(HttpStatus.CONFLICT, "User is duplicated"),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "email not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    ;

    private HttpStatus status;
    private String message;
}
