package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShowDetail showDetail;

    @Embedded
    private GradeDetails gradeDetails;

    private int totalQuantity;
    private int currentQuantity;

    public static Seat of(ShowDetail showDetail, Grade grade, int totalQuantity, int currentQuantity) {
        return new Seat(showDetail, grade, totalQuantity, currentQuantity);
    }

    private Seat(ShowDetail showDetail, Grade grade, int totalQuantity, int currentQuantity) {
        this.showDetail = showDetail;
        this.gradeDetails = new GradeDetails(grade);
        this.totalQuantity = totalQuantity;
        this.currentQuantity = currentQuantity;
    }
}
