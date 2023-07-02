package com.imhero.reservation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSeatResponse {
    private Long seatId;
    private String grade;
    private int price;
    private int count = 0;

    public ReservationSeatResponse(Long seatId, String grade, int price) {
        this.seatId = seatId;
        this.grade = grade;
        this.price = price;
    }

    public ReservationSeatResponse(Long seatId) {
        this.seatId = seatId;
    }

    public static ReservationSeatResponse of(Long seatId, String grade, int price) {
        return new ReservationSeatResponse(seatId, grade, price);
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
