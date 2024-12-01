package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VideoDto {
    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    String creator;

    @NotNull
    String distribution;

    @NotNull
    String age;

    @NotNull
    String date;

    @NotNull
    List<String> categories;

    @NotNull
    Integer groupId;

    Integer season;
    Integer previousVideo;
    Integer nextVideo;
}
