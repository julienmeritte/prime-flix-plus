package com.etna.primeflixplus.entities;

import com.etna.primeflixplus.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Profile> profiles;

    // TODO: Subscription enum

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    String verificationCode;

    Boolean enabled = Boolean.FALSE;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    public User() {
        LocalDateTime time = LocalDateTime.now();
        if (this.creationDate == null)
            this.creationDate = time;
        if (this.updatedDate == null)
            this.updatedDate = time;
    }
}
