package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProfileModificationDto {

    @NotNull
    Integer id;

    String pseudo;

    String image;

    Boolean receiveNewsletter;

    Boolean receiveNewSeries;

    Boolean receiveNewFilms;

    Boolean receiveNewSeasons;

}
