package com.etna.primeflixplus.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "video_profile")
public class VideoProfile {

    @Id
    @GeneratedValue
    private Integer id;

    Boolean favorite = Boolean.FALSE;

    Boolean watchLater = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProfile", referencedColumnName = "id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVideo", referencedColumnName = "id", nullable = false)
    private Video video;

    LocalDateTime timestamp;

    public VideoProfile() {

    }
}
