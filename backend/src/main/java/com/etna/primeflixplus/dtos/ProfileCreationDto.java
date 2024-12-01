package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ProfileCreationDto {

    @NotNull
    String pseudo;

    @NotNull
    String image;

    Boolean isYoung;
}
