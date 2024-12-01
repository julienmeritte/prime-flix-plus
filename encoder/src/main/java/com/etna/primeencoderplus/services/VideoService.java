package com.etna.primeencoderplus.services;

import com.etna.primeencoderplus.enums.FormatVideo;
import com.etna.primeencoderplus.enums.QualityVideo;
import com.etna.primeencoderplus.exception.CustomGlobalException;
import com.etna.primeencoderplus.exception.CustomMessageException;
import com.etna.primeencoderplus.utilities.Constants;
import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class VideoService {

    private final Environment env;

    public VideoService(Environment env) {
        this.env = env;
    }

    public Boolean manageVideoName(String id, MultipartFile source) throws CustomGlobalException {
        String path = Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + FormatVideo.MP4.format;
        try {
            File file = ResourceUtils.getFile(path);
            if (!file.exists()) {
                Files.copy(source.getInputStream(), Path.of(file.getCanonicalPath()));
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.ENCODER_GENERIC_FAILED_EXECUTE);
        }
    }

    @Async
    public void encodeVideo(String id) throws CustomGlobalException {
        try {
            String path = Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + id + FormatVideo.MP4.format;
            File file = ResourceUtils.getFile(path);
            String rootPath = file.getCanonicalPath();
            Stream stream = getVideoProbeStream(rootPath);
            Integer quality = getQuality(stream.getBitRate(), stream.getWidth(), stream.getHeight());
            switch (quality) {
                case 1080:
                    encode240(rootPath, id);
                    encode360(rootPath, id);
                    encode480(rootPath, id);
                    encode720(rootPath, id);
                    encode1080(rootPath, id);
                    file.delete();
                    break;
                case 720:
                    encode240(rootPath, id);
                    encode360(rootPath, id);
                    encode480(rootPath, id);
                    encode720(rootPath, id);
                    file.delete();
                    break;
                case 480:
                    encode240(rootPath, id);
                    encode360(rootPath, id);
                    encode480(rootPath, id);
                    file.delete();
                    break;
                case 360:
                    encode240(rootPath, id);
                    encode360(rootPath, id);
                    file.delete();
                    break;
                case 240:
                    encode240(rootPath, id);
                    file.delete();
                    break;
                default:
                    throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.ENCODER_INVALID_QUALITY);
            }
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.ENCODER_GENERIC_FAILED_EXECUTE);
        }
    }

    void encode240(String path, String id) throws JSONException, IOException {
        String subPath = path.substring(0, path.length() - 4);
        String concatenated = subPath + "_240.mp4";

        FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .setOverwriteOutput(true)
                .addArguments("-preset", "slow")
                .addArguments("-b:v", "426k")
                .addArguments("-vf", "scale=426:240")
                .addOutput(UrlOutput.toUrl(concatenated))
                .execute();
        log.info("240p - Video encoded");
        sendBackVideo(concatenated, id, QualityVideo.Q240);
    }

    void encode360(String path, String id) throws JSONException, IOException {
        String subPath = path.substring(0, path.length() - 4);
        String concatenated = subPath + "_360.mp4";

        FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .setOverwriteOutput(true)
                .addArguments("-preset", "slow")
                .addArguments("-b:v", "640k")
                .addArguments("-vf", "scale=640:360")
                .addOutput(UrlOutput.toUrl(concatenated))
                .execute();
        log.info("360p - Video encoded");
        sendBackVideo(concatenated, id, QualityVideo.Q360);
    }

    void encode480(String path, String id) throws JSONException, IOException {
        String subPath = path.substring(0, path.length() - 4);
        String concatenated = subPath + "_480.mp4";

        FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .setOverwriteOutput(true)
                .addArguments("-preset", "slow")
                .addArguments("-b:v", "854k")
                .addArguments("-vf", "scale=854:480")
                .addOutput(UrlOutput.toUrl(concatenated))
                .execute();
        log.info("480p - Video encoded");
        sendBackVideo(concatenated, id, QualityVideo.Q480);
    }

    void encode720(String path, String id) throws JSONException, IOException {
        String subPath = path.substring(0, path.length() - 4);
        String concatenated = subPath + "_720.mp4";

        FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .setOverwriteOutput(true)
                .addArguments("-preset", "slow")
                .addArguments("-b:v", "1280k")
                .addArguments("-vf", "scale=1280:720")
                .addOutput(UrlOutput.toUrl(concatenated))
                .execute();
        log.info("720p - Video encoded");
        sendBackVideo(concatenated, id, QualityVideo.Q720);
    }

    void encode1080(String path, String id) throws JSONException, IOException {
        String subPath = path.substring(0, path.length() - 4);
        String concatenated = subPath + "_1080.mp4";

        FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .setOverwriteOutput(true)
                .addArguments("-preset", "slow")
                .addArguments("-b:v", "1920k")
                .addArguments("-vf", "scale=1920:1080")
                .addOutput(UrlOutput.toUrl(concatenated))
                .execute();
        log.info("1080p - Video encoded");
        sendBackVideo(concatenated, id, QualityVideo.Q1080);
    }

    Integer getQuality(Integer bitrate, Integer width, Integer height) {
        if (bitrate > 1280000 && width >= 1920 && height >= 1080) {
            return 1080;
        } else if (bitrate > 854000 && width >= 1280 && height >= 720) {
            return 720;
        } else if (bitrate > 640000 && width >= 854 && height >= 480) {
            return 480;
        } else if (bitrate > 426000 && width >= 640 && height >= 360) {
            return 360;
        } else {
            return 240;
        }
    }

    Stream getVideoProbeStream(String path) {
        FFprobeResult result = FFprobe.atPath()
                .setShowStreams(true)
                .setSelectStreams(StreamType.VIDEO)
                .setInput(path)
                .execute();
        return result.getStreams().get(0);
    }

    void sendBackVideo(String path, String id, QualityVideo quality) throws JSONException, IOException {
        log.info("SENDING BACK VIDEO " + id + " - " + quality + " [...]");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("quality", quality.toString());

        File file = ResourceUtils.getFile(path);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        var value = new FileSystemResource(new File(file.getCanonicalPath()));
        map.add("source", value);
        map.add("video", String.valueOf(jsonObject));

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        var builder = UriComponentsBuilder.fromHttpUrl(env.getProperty("primeflix.backend.address") + "/video/quality");
        var restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode().value() == 201) {
            log.info("VIDEO " + id + " - " + quality + " SENT BACK SUCCESSFULLY.");
            file.delete();
        }
    }
}
