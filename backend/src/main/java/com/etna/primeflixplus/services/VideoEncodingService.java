package com.etna.primeflixplus.services;

import com.etna.primeflixplus.dtos.QualityDto;
import com.etna.primeflixplus.entities.Video;
import com.etna.primeflixplus.entities.VideoEncoding;
import com.etna.primeflixplus.enums.FormatVideo;
import com.etna.primeflixplus.enums.VideoQuality;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.VideoEncodingRepository;
import com.etna.primeflixplus.repositories.VideoRepository;
import com.etna.primeflixplus.utilities.Constants;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class VideoEncodingService {

    private final VideoEncodingRepository videoEncodingRepository;

    private final VideoRepository videoRepository;

    private Environment env;

    public VideoEncodingService(VideoEncodingRepository videoEncodingRepository, VideoRepository videoRepository, Environment env) {
        this.videoEncodingRepository = videoEncodingRepository;
        this.videoRepository = videoRepository;
        this.env = env;
    }

    public void saveSourceDatabase(Video video) {
        VideoEncoding encoding = new VideoEncoding();
        encoding.setQuality(VideoQuality.QSOURCE);
        encoding.setVideo(video);
        encoding.setPath(Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + video.getId() + "_SOURCE" + FormatVideo.MP4.format);
        videoEncodingRepository.save(encoding);
    }


    public void saveQuality(MultipartFile source, QualityDto qualityDto) throws CustomGlobalException, IOException {
        String path;
        VideoQuality quality;
        if (EnumUtils.isValidEnum(VideoQuality.class, qualityDto.getQuality()))
            quality = EnumUtils.getEnum(VideoQuality.class, qualityDto.getQuality());
        else
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);
        path = getPathFromQuality(quality, qualityDto.getId());

        Optional<Video> video = videoRepository.findById(qualityDto.getId());
        if (video.isPresent()) {
            File file = ResourceUtils.getFile(path);
            if (!file.exists())
                Files.copy(source.getInputStream(), Path.of(file.getCanonicalPath()));
            else
                throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);

            VideoEncoding videoEncoding = new VideoEncoding();
            videoEncoding.setQuality(quality);
            videoEncoding.setPath(path);
            videoEncoding.setVideo(video.get());
            videoEncodingRepository.save(videoEncoding);
        } else
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);
    }

    String getPathFromQuality(VideoQuality quality, Integer id) throws CustomGlobalException {
        switch (quality) {
            case Q240:
                return Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_240" + FormatVideo.MP4.format;
            case Q360:
                return Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_360" + FormatVideo.MP4.format;
            case Q480:
                return Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_480" + FormatVideo.MP4.format;
            case Q720:
                return Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_720" + FormatVideo.MP4.format;
            case Q1080:
                return Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_1080" + FormatVideo.MP4.format;
            default:
                throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);
        }
    }
}
