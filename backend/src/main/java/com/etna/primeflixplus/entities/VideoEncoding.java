package com.etna.primeflixplus.entities;

import com.etna.primeflixplus.enums.VideoQuality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "video_encoding")
public class VideoEncoding {

    @Id
    @GeneratedValue
    private Integer id;

    String path;

    @Enumerated(EnumType.STRING)
    VideoQuality quality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVideo", referencedColumnName = "id", nullable = false)
    private Video video;

    public VideoEncoding() {

    }
}
