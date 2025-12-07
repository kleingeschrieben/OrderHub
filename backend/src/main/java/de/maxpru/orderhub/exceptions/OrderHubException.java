package de.maxpru.orderhub.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class OrderHubException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    protected OrderHubException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
