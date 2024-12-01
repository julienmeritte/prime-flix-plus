package com.etna.primeflixplus.entities;

import com.etna.primeflixplus.enums.VideoAge;
import com.etna.primeflixplus.enums.VideoCategory;
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
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue
    Integer id;

    String name;

    String description;

    String creator;

    String distribution;

    Integer season;

    @Enumerated(EnumType.STRING)
    VideoAge age;

    String date;

    String image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
    List<VideoProfile> videoProfiles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
    List<VideoEncoding> videoEncodings;

    @OneToOne
    @JoinColumn(name = "previousVideo", referencedColumnName = "id")
    Video previousVideo;

    @OneToOne
    @JoinColumn(name = "nextVideo", referencedColumnName = "id")
    Video nextVideo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGroup", referencedColumnName = "id", nullable = false)
    private VideoGroup group;

    @ElementCollection(fetch = FetchType.EAGER)
    List<VideoCategory> categories;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    public Video() {
        LocalDateTime time = LocalDateTime.now();
        if (this.creationDate == null)
            this.creationDate = time;
        if (this.updatedDate == null)
            this.updatedDate = time;
    }
}
