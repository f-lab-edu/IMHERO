package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public static ShowDetail of(Show show, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
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

    public ShowDetail modify(Show show, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
        if (!ObjectUtils.isEmpty(show)) {
            this.show = show;
        }
        if (!ObjectUtils.isEmpty(sequence)) {
            this.sequence = sequence;
        }
        if (!ObjectUtils.isEmpty(showFromDt)) {
            this.showFromDt = showFromDt;
        }
        if (!ObjectUtils.isEmpty(showToDt)) {
            this.showToDt = showToDt;
        }
        if (!ObjectUtils.isEmpty(reservationFromDt)) {
            this.reservationFromDt = reservationFromDt;
        }
        if (!ObjectUtils.isEmpty(reservationToDt)) {
            this.reservationToDt = reservationToDt;
        }
        if (StringUtils.hasText(delYn)) {
            this.delYn = delYn;
        }
        return this;
    }

    public boolean cancel() {
        if (this.delYn.equals("Y")) {
            return false;
        }

        this.delYn = "Y";
        return true;
    }
}
