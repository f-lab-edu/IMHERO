package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

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

    private Seat(ShowDetail showDetail, Grade grade, int totalQuantity) {
        this.showDetail = showDetail;
        this.gradeDetails = new GradeDetails(grade);
        this.totalQuantity = totalQuantity;
        this.currentQuantity = totalQuantity;
    }

    public static Seat of(ShowDetail showDetail, Grade grade, int totalQuantity) {
        return new Seat(showDetail, grade, totalQuantity);
    }

    public Seat modify(Grade grade, int totalQuantity) {
        if (totalQuantity >= 0) {
            this.totalQuantity = totalQuantity;
        }
        if (!ObjectUtils.isEmpty(grade)) {
            this.gradeDetails = new GradeDetails(grade);
        }
        return this;
    }

    public int reserve(int count) {
        if (currentQuantity < count) {
            throw new IllegalArgumentException("잔여 티켓 수량이 부족합니다.");
        }
        currentQuantity -= count;
        return count;
    }
    public int cancel(int count) {
        int total = currentQuantity + count;
        if (total > totalQuantity) {
            throw new IllegalArgumentException("전체 티켓 수량을 초과하는 티켓은 취소할 수 없습니다.");
        }
        currentQuantity = total;
        return count;
    }
}
