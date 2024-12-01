package com.etna.primeencoderplus.utilities;

public final class Constants {

    private Constants() {
    }

    public static final String HEADER_CONTENT_TYPE = "application/json; charset=UTF-8";

    public static final String STATIC_VIDEO_PATH = "file:";

    public static final String FFMPEG_GET_WIDTH = "ffprobe -v error -select_streams v:0 -show_entries stream=width -of default=noprint_wrappers=1 ";
    public static final String FFMPEG_GET_HEIGHT = "ffprobe -v error -select_streams v:0 -show_entries stream=height -of default=noprint_wrappers=1 ";
    public static final String FFMPEG_GET_BITRATE = "ffprobe -v error -select_streams v:0 -show_entries stream=bit_rate -of default=noprint_wrappers=1 ";

    public static final String FFMPEG_ENCODE_240 = "ffmpeg -i %s -preset slow -b:v 426k -vf scale=426:240 ";
    public static final String FFMPEG_ENCODE_360 = "ffmpeg -i %s -preset slow -b:v 640k -vf scale=640:360 ";
    public static final String FFMPEG_ENCODE_480 = "ffmpeg -i %s -preset slow -b:v 854k -vf scale=854:480 ";
    public static final String FFMPEG_ENCODE_720 = "ffmpeg -i %s -preset slow -b:v 1280k -vf scale=scale=1280:720 ";
    public static final String FFMPEG_ENCODE_1080 = "ffmpeg -i %s -preset slow -b:v 1920k -vf scale=1920:1080 ";
}
