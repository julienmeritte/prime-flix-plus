package com.etna.primeencoderplus.utilities;

import com.etna.primeencoderplus.enums.FormatVideo;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.List;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {
    }

    public static JSONObject jsonifyVideoMp4(String name) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("path", name + FormatVideo.MP4.format);
        return jsonObject;
    }
}
