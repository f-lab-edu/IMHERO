package com.imhero.reservation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSeatResponse {
    private Long seatId;
    private String grade;
    private int price;
    private int currentQuantity;
    private int totalQuantity;
    private int count = 0;

    public ReservationSeatResponse(Long seatId, String grade, int price, int currentQuantity, int totalQuantity) {
        this.seatId = seatId;
        this.grade = grade;
        this.price = price;
        this.currentQuantity = currentQuantity;
        this.totalQuantity = totalQuantity;
    }

    public ReservationSeatResponse(Long seatId) {
        this.seatId = seatId;
    }

    public static ReservationSeatResponse of(Long seatId, String grade, int price, int currentQuantity, int totalQuantity) {
        return new ReservationSeatResponse(seatId, grade, price, currentQuantity, totalQuantity);
    }

    public void addCount() {
        this.count += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationSeatResponse that = (ReservationSeatResponse) o;

        return getSeatId().equals(that.getSeatId());
    }

    @Override
    public int hashCode() {
        return getSeatId().hashCode();
    }
}
