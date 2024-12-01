package com.etna.primeflixplus.services;


import com.etna.primeflixplus.dtos.QualityDto;
import com.etna.primeflixplus.dtos.VideoDto;
import com.etna.primeflixplus.dtos.VideoGroupCreationDto;
import com.etna.primeflixplus.entities.Video;
import com.etna.primeflixplus.entities.VideoEncoding;
import com.etna.primeflixplus.entities.VideoGroup;
import com.etna.primeflixplus.enums.FormatVideo;
import com.etna.primeflixplus.enums.VideoAge;
import com.etna.primeflixplus.enums.VideoCategory;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.VideoEncodingRepository;
import com.etna.primeflixplus.repositories.VideoGroupRepository;
import com.etna.primeflixplus.repositories.VideoProfileRepository;
import com.etna.primeflixplus.repositories.VideoRepository;
import com.etna.primeflixplus.utilities.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    private final VideoGroupRepository videoGroupRepository;

    private final VideoEncodingRepository videoEncodingRepository;

    private final VideoProfileRepository videoProfileRepository;

    private final Environment env;

    public VideoService(VideoRepository videoRepository, Environment env, VideoGroupRepository videoGroupRepository, VideoEncodingRepository videoEncodingRepository, VideoProfileRepository videoProfileRepository) {
        this.videoRepository = videoRepository;
        this.env = env;
        this.videoGroupRepository = videoGroupRepository;
        this.videoEncodingRepository = videoEncodingRepository;
        this.videoProfileRepository = videoProfileRepository;
    }

    public VideoDto mapVideoDtoFromJson(String json) throws CustomGlobalException {
        VideoDto videoDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            videoDto = objectMapper.readValue(json, VideoDto.class);
            return videoDto;
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.BAD_REQUEST, CustomMessageException.VIDEO_ROUTE_WRONG_PARAMETERS);
        }
    }

    public QualityDto mapQualityDtoFromJson(String json) throws CustomGlobalException {
        QualityDto qualityDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            qualityDto = objectMapper.readValue(json, QualityDto.class);
            return qualityDto;
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.BAD_REQUEST, CustomMessageException.VIDEO_ROUTE_WRONG_PARAMETERS);
        }
    }

    public Video addVideo(VideoDto videoDto) throws CustomGlobalException {
        VideoGroup group = getVideoGroupById(videoDto.getGroupId());
        Video video = new Video();
        video.setName(videoDto.getName());
        video.setDescription(videoDto.getDescription());
        video.setCreator(videoDto.getCreator());
        video.setDistribution(videoDto.getDistribution());
        video.setDate(videoDto.getDate());
        video.setGroup(group);
        if (EnumUtils.isValidEnum(VideoAge.class, videoDto.getAge())) {
            video.setAge(EnumUtils.getEnum(VideoAge.class, videoDto.getAge()));
        } else
            throw new CustomGlobalException(HttpStatus.BAD_REQUEST, CustomMessageException.VIDEO_ROUTE_WRONG_PARAMETERS);
        List<VideoCategory> categories = new ArrayList<>();
        if(!videoDto.getCategories().isEmpty()) {
            for (String cat : videoDto.getCategories()) {
                if (EnumUtils.isValidEnum(VideoCategory.class, cat)) {
                    categories.add(EnumUtils.getEnum(VideoCategory.class, cat));
                }
            }
            if (!categories.isEmpty()) {
                video.setCategories(categories);
            }
        }

        return videoRepository.save(video);
    }

    public void encodeVideo(MultipartFile source, String id, MultipartFile image) throws CustomGlobalException, IOException {
        String path = Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + "_SOURCE" + FormatVideo.MP4.format;
        String pathImage = Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.images") + id + ".png";
        File file;
        File fileImage;
        try {
            file = ResourceUtils.getFile(path);
            log.info(file.getCanonicalPath());
            Files.copy(source.getInputStream(), Path.of(file.getCanonicalPath()));

            fileImage = ResourceUtils.getFile(pathImage);
            log.info(fileImage.getCanonicalPath());
            Files.copy(image.getInputStream(), Path.of(fileImage.getCanonicalPath()));

            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            var value = new FileSystemResource(new File(file.getCanonicalPath()));
            map.add("source", value);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            var builder = UriComponentsBuilder.fromHttpUrl(env.getProperty("primeflix.encoder.address") + "/encoder");
            builder.queryParam("id", id);
            var restTemplate = new RestTemplate();
            restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_FAILED_DOWNLOAD);
        }
    }

    public VideoGroup createVideoGroup(VideoGroupCreationDto videoGroupCreationDto) {
        VideoGroup group = new VideoGroup();
        group.setName(videoGroupCreationDto.getName());
        group.setIsSerie(videoGroupCreationDto.getIsSerie());
        return videoGroupRepository.save(group);
    }

    public VideoGroup getVideoGroupById(Integer idGroup) throws CustomGlobalException {
        Optional<VideoGroup> group = videoGroupRepository.findById(idGroup);
        if (group.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.VIDEO_GROUP_GET_ONE_FAILED);
        return group.get();
    }

    public List<VideoGroup> getAllVideoGroup() {
        return videoGroupRepository.findAll();
    }

    public List<Video> getAllVideoByGroupId(Integer id) throws CustomGlobalException {
        VideoGroup groupe = getVideoGroupById(id);
        List<Video> videos = videoRepository.findAllByGroup(groupe);
        if (videos.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.VIDEO_LIST_EMPTY);
        return videos;
    }

    public void deleteVideoGroup(Integer id) throws CustomGlobalException {
        VideoGroup groupe = getVideoGroupById(id);
        if (!groupe.getVideos().isEmpty()) {
            for (Video video : groupe.getVideos()) {
                if (!video.getVideoEncodings().isEmpty()) {
                    for (VideoEncoding encoding : video.getVideoEncodings()) {
                        try {
                            File file = ResourceUtils.getFile(encoding.getPath());
                            file.delete();
                        } catch (Exception e) {
                            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);
                        }
                        videoEncodingRepository.delete(encoding);
                    }
                }
                if (!video.getVideoProfiles().isEmpty())
                    videoProfileRepository.deleteAll(video.getVideoProfiles());
                videoRepository.delete(video);
            }
        }
        videoGroupRepository.delete(groupe);
    }

    public void deleteVideo(Integer id) throws CustomGlobalException {
        Video video = getVideoById(id);
        if (!video.getVideoEncodings().isEmpty()) {
            for (VideoEncoding encoding : video.getVideoEncodings()) {
                try {
                    File file = ResourceUtils.getFile(encoding.getPath());
                    file.delete();
                } catch (Exception e) {
                    throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.VIDEO_QUALITY_FAILED);
                }
                videoEncodingRepository.delete(encoding);
            }
        }
        if (!video.getVideoProfiles().isEmpty())
            videoProfileRepository.deleteAll(video.getVideoProfiles());
        videoRepository.delete(video);
    }

    public VideoGroup modifyGroupe(Integer id, VideoGroupCreationDto videoGroupCreationDto) throws CustomGlobalException {
        VideoGroup groupe = getVideoGroupById(id);
        groupe.setName(videoGroupCreationDto.getName());
        groupe.setUpdatedDate(LocalDateTime.now());
        return videoGroupRepository.save(groupe);
    }

    public Video getVideoById(Integer id) throws CustomGlobalException {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.VIDEO_GROUP_GET_ONE_FAILED);
        return video.get();
    }

    public Video updateVideo(Integer id, VideoDto videoDto) throws CustomGlobalException {
        Video video = getVideoById(id);
        video.setName(videoDto.getName());
        video.setDescription(videoDto.getDescription());
        video.setCreator(videoDto.getCreator());
        video.setDistribution(videoDto.getDistribution());
        if (EnumUtils.isValidEnum(VideoAge.class, videoDto.getAge()))
            video.setAge(EnumUtils.getEnum(VideoAge.class, videoDto.getAge()));
        video.setDate(videoDto.getDate());
        video.setUpdatedDate(LocalDateTime.now());
        return videoRepository.save(video);
    }

    public List<Video> sortAllRecent() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("date").ascending());
        Page<Video> videoSort = videoRepository.findAll(paging);
        return videoSort.getContent();
    }

    public List<Video> sortSerieRecent() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("date").ascending());
        Page<Video> videoSort = videoRepository.findAll(paging);
        return videoSort.getContent();
    }
}
