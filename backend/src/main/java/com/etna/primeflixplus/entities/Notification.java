package com.etna.primeflixplus.entities;

import com.etna.primeflixplus.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue
    private Integer id;

    String description;

    Boolean seen;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProfile", referencedColumnName = "id", nullable = false)
    private Profile profile;

    public Notification() {

    }
}
