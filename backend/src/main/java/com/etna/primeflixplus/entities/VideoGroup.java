package com.etna.primeflixplus.entities;

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
@Table(name = "video_group")
public class VideoGroup {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    String name;

    Boolean isSerie = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Video> videos;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    public VideoGroup() {
        LocalDateTime time = LocalDateTime.now();
        if (this.creationDate == null)
            this.creationDate = time;
        if (this.updatedDate == null)
            this.updatedDate = time;
    }
}
