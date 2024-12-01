package com.etna.primeflixplus.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {

    @NotNull
    String mail;

    @NotNull
    String password;
}
