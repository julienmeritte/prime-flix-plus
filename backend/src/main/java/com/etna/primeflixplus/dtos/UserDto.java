package com.etna.primeflixplus.dtos;

import lombok.Data;

@Data
public class UserDto {

    String mail;

    String password;

    Integer role;

    Boolean enabled;
}
