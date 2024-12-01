package com.etna.primeencoderplus.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;

@Getter
public class CustomGlobalException extends ServletException {
    private static final long serialVersionUID = 1L;

    final HttpStatus customStatus;
    final String customMessage;

    public CustomGlobalException(HttpStatus status, String message) {
        this.customStatus = status;
        this.customMessage = message;
    }
}