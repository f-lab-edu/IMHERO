package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import com.imhero.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "shows")
public class Show extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime showFromDate;
    private LocalDateTime showToDate;
    private String delYn;

    public static Show of(String title, String artist, String place, User user, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        return new Show(title, artist, place, user, showFromDate, showToDate, delYn);
    }

    private Show(String title, String artist, String place, User user, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        this.title = title;
        this.artist = artist;
        this.place = place;
        this.user = user;
        this.showFromDate = showFromDate;
        this.showToDate = showToDate;
        this.delYn = delYn;
    }
}
