package com.etna.primeflixplus.exception;

import com.etna.primeflixplus.utilities.Constants;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final HttpHeaders headers;

    public GlobalExceptionHandler() {
        this.headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE);
    }

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<Object> handleCustomResourceException(CustomGlobalException exception) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", exception.customStatus.value());
        jsonObject.put("message", exception.customMessage);
        return ResponseEntity.status(exception.customStatus).headers(headers).body(jsonObject.toString());
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", CustomMessageException.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(jsonObject.toString());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}