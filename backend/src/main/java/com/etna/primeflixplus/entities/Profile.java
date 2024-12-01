package com.etna.primeflixplus.entities;

import com.etna.primeflixplus.utilities.Constants;
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
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String pseudo;

    private Boolean isMainProfile = Boolean.FALSE;

    private Boolean isYoung = Boolean.FALSE;

    private String image = Constants.BASE_PROFILE_IMAGE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<VideoProfile> videos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private List<Notification> notifications;

    private Boolean receiveNewsletter = Boolean.TRUE;

    private Boolean receiveNewSeries = Boolean.TRUE;

    private Boolean receiveNewFilms = Boolean.TRUE;

    private Boolean receiveNewSeasons = Boolean.TRUE;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    public Profile() {
        LocalDateTime time = LocalDateTime.now();
        if (creationDate == null)
            creationDate = time;
        if (updatedDate == null)
            updatedDate = time;
    }
}
