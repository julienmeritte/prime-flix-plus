package com.etna.primeflixplus.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER,
    ROLE_SUPPORT,
    ROLE_AFFILIATE,
    ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
