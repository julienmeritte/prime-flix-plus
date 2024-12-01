package com.etna.primeencoderplus.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CustomMessageException {

    CustomMessageException() {
    }

    // User
    public static final String ENCODER_INVALID_QUALITY = "An error as occured while determining quality.";

    public static final String ENCODER_GENERIC_FAILED_EXECUTE = "An error as occured while encoding.";

    public static final String BAD_REQUEST = "Bad Request.";
}
