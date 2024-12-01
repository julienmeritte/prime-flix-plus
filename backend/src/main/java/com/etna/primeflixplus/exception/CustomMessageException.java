package com.etna.primeflixplus.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CustomMessageException {

    CustomMessageException() {
    }

    // User
    public static final String USER_REGISTER_ERROR = "An error as occured while registering.";
    public static final String USER_REGISTER_ALREADY_EXISTS = "User already exists.";
    public static final String USER_SEND_MAIL_VERIFICATION = "Failed to send mail verification.";
    public static final String USER_MAIL_VALIDATION = "Failed to validate mail.";
    public static final String USER_MAIL_VALIDATION_NOT_FOUND = "Failed to validate mail with this code.";
    public static final String USER_MAIL_VALIDATION_ALREADY_ACTIVATED = "Failed to validate mail to an already validated account.";
    public static final String USER_AUTH_FAILED = "Authentication failed.";
    public static final String USER_INVALID_TOKEN = "Token invalid";
    public static final String USER_GET_ALL_FAILED = "Failed to retrieve users.";
    public static final String USER_GET_ONE_FAILED = "Failed to retrieve user.";
    public static final String USER_ACCESS_DENIED = "User can't be modified without necessary rights.";


    public static final String USER_MODIF_NO_MODIF = "No modification to do.";

    public static final String PROFILE_ADD_FAILED = "Failed to create profile.";
    public static final String PROFILE_ADD_MAIN_FAILED = "Main profile already exists.";
    public static final String PROFILE_GET_FAILED = "Failed to get profiles.";
    public static final String PROFILE_GET_NOTHING = "Failed to get profiles, 0 entries";
    public static final String PROFILE_GET_ONE_FAILED = "Failed to retrieve profile.";

    public static final String VIDEO_ROUTE_WRONG_PARAMETERS = "Failed to retreive video parameters.";
    public static final String VIDEO_QUALITY_FAILED = "Failed to save video quality.";
    public static final String VIDEO_FAILED_DOWNLOAD = "Failed to download video.";
    public static final String VIDEO_LIST_EMPTY = "No videos in list";

    public static final String VIDEO_GROUP_GET_ONE_FAILED = "Failed to retrieve video group.";

    public static final String BAD_REQUEST = "Bad Request.";

    public static final String REFRESH_TOKEN_INVALID = "Invalid refresh token";
}
