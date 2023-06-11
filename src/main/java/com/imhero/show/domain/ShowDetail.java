package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShowDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    private Integer sequence;
    private LocalDateTime showFromDt;
    private LocalDateTime showToDt;
    private LocalDateTime reservationFromDt;
    private LocalDateTime reservationToDt;
    private String delYn;

    private static ShowDetail of(Show show, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
        return new ShowDetail(show, sequence, showFromDt, showToDt, reservationFromDt, reservationToDt, delYn);
    }

    private ShowDetail(Show show, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
        this.show = show;
        this.sequence = sequence;
        this.showFromDt = showFromDt;
        this.showToDt = showToDt;
        this.reservationFromDt = reservationFromDt;
        this.reservationToDt = reservationToDt;
        this.delYn = delYn;
    }
}
