package com.etna.primeflixplus.utilities;

import com.etna.primeflixplus.dtos.TokenDto;
import com.etna.primeflixplus.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {
    }

    /*    *//**
     * Jsonify delete Boolean
     *
     * @param success -> Boolean
     * @return jsonObject -> JSONObject
     * @throws JSONException -> default error
     *//*
    public static JSONObject jsonifyDeleteBoolean(Boolean success) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", success.toString());
        return jsonObject;
    }

    */

    /**
     * Jsonify token response
     *
     * @param token -> String
     * @return jsonObject -> JSONObject
     * @throws JSONException -> default error
     */
    public static JSONObject jsonifyToken(TokenDto token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token.getToken());
        jsonObject.put("refresh", token.getRefresh());
        return jsonObject;
    }

    /**
     * Jsonify Address response
     *
     * @param address -> Address
     * @return jsonObject -> JSONObject
     * @throws JSONException -> default error
     *//*
    public static JSONObject jsonifyAddress(Address address) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("street", address.getStreet());
        jsonObject.put("postalCode", address.getPostalCode());
        jsonObject.put("city", address.getCity());
        jsonObject.put("country", address.getCountry());
        return jsonObject;
    }

    *//**
     * Jsonify Address List response
     *
     * @param addresses -> List<Address>
     * @return jsonObject -> JSONObject
     * @throws JSONException -> default error
     *//*
    public static JSONArray jsonifyAddressList(List<Address> addresses) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Address address : addresses) {
            jsonArray.put(jsonifyAddress(address));
        }
        return jsonArray;
    }

    */

    /**
     * Jsonify UserDetails Model
     *
     * @param user -> User
     * @return jsonObject -> JSONObject
     * @throws JSONException -> default error
     */
    public static JSONObject jsonifyUser(User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("mail", user.getMail());
        jsonObject.put("role", user.getRole().toString());
        jsonObject.put("created", user.getCreationDate().toString());
        jsonObject.put("updated", user.getUpdatedDate().toString());
        jsonObject.put("enabled", user.getEnabled().booleanValue());
        return jsonObject;
    }

    public static JSONObject jsonifyVideoGroup(VideoGroup group) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", group.getId());
        jsonObject.put("name", group.getName());
        jsonObject.put("isSerie", group.getIsSerie().booleanValue());
        jsonObject.put("created", group.getCreationDate().toString());
        jsonObject.put("updated", group.getUpdatedDate().toString());
        return jsonObject;
    }

    public static JSONArray jsonifyVideoGroupList(List<VideoGroup> groupes) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (VideoGroup groupe : groupes) {
            jsonArray.put(jsonifyVideoGroup(groupe));
        }
        return jsonArray;
    }

    public static JSONArray jsonifyUserList(List<User> users) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            jsonArray.put(jsonifyUser(user));
        }
        return jsonArray;
    }

    public static JSONObject jsonifyUserWithProfiles(User user) throws JSONException {
        JSONObject jsonObject = jsonifyUser(user);
        jsonObject.put("profiles", jsonifyProfileList(user.getProfiles()));
        return jsonObject;
    }

    public static JSONObject jsonifyProfile(Profile profile) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", profile.getId());
        jsonObject.put("pseudo", profile.getPseudo());
        jsonObject.put("isMainProfile", profile.getIsMainProfile().booleanValue());
        jsonObject.put("isYoung", profile.getIsYoung().booleanValue());
        jsonObject.put("image", profile.getImage());
        jsonObject.put("receiveNewsletter", profile.getReceiveNewsletter().booleanValue());
        jsonObject.put("receiveNewSeries", profile.getReceiveNewSeries().booleanValue());
        jsonObject.put("receiveNewFilms", profile.getReceiveNewFilms().booleanValue());
        jsonObject.put("receiveNewSeasons", profile.getReceiveNewSeasons().booleanValue());
        jsonObject.put("created", profile.getCreationDate());
        jsonObject.put("updated", profile.getUpdatedDate());
        return jsonObject;
    }

    public static JSONArray jsonifyProfileList(List<Profile> profiles) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Profile profile : profiles) {
            jsonArray.put(jsonifyProfile(profile));
        }
        return jsonArray;
    }

    public static JSONObject jsonifyVideo(Video video) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", video.getId());
        jsonObject.put("name", video.getName());
        jsonObject.put("description", video.getDescription());
        jsonObject.put("creator", video.getCreator());
        jsonObject.put("distribution", video.getDistribution());
        jsonObject.put("age", video.getAge());
        jsonObject.put("date", video.getDate());
        jsonObject.put("groupe", video.getGroup().getId());
        if (video.getVideoEncodings() == null)
            jsonObject.put("formats", "");
        else
            jsonObject.put("formats", jsonifyEncodingList(video.getVideoEncodings()));
        if (video.getSeason() != null)
            jsonObject.put("season", video.getSeason());
        if (video.getPreviousVideo() != null)
            jsonObject.put("previous", video.getPreviousVideo());
        if (video.getNextVideo() != null)
            jsonObject.put("next", video.getNextVideo());
        jsonObject.put("categorie", video.getCategories());
        return jsonObject;
    }

    public static JSONObject jsonifyEncoding(VideoEncoding video) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", video.getId());
        jsonObject.put("path", video.getPath());
        jsonObject.put("quality", video.getQuality());
        return jsonObject;
    }

    public static JSONArray jsonifyEncodingList(List<VideoEncoding> videos) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (VideoEncoding video : videos) {
            jsonArray.put(jsonifyEncoding(video));
        }
        return jsonArray;
    }

    public static JSONArray jsonifyVideoList(List<Video> videos) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Video video : videos) {
            jsonArray.put(jsonifyVideo(video));
        }
        return jsonArray;
    }

    /**
     * Jsonify ParamMissingException, showing missing required params
     *
     * @param exception -> MissingServletRequestParameterException
     * @return jsonResponse -> JSONObject
     * @throws JSONException -> default error
     *//*
    public static JSONObject jsonifyParamMissing(MissingServletRequestParameterException exception) throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", exception.getMessage());
        jsonResponse.put("required parameter type", exception.getParameterType());
        jsonResponse.put("required parameter name", exception.getParameterName());
        return jsonResponse;
    }*/
}
