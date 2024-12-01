package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenDto {

    @NotNull
    String token;

    @NotNull
    String refresh;
}
