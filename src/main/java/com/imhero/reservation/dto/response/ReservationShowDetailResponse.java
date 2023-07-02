package com.imhero.reservation.dto.response;

import com.imhero.reservation.dto.ReservationDao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationShowDetailResponse {
    private Long showDetailId;
    private Integer sequence;
    private LocalDateTime showFromDt;
    private LocalDateTime showToDt;
    private LocalDateTime reservationFromDt;
    private LocalDateTime reservationToDt;
    private String delYn;
    private List<ReservationSeatResponse> reservationSeatResponses = new ArrayList<>();

    public ReservationShowDetailResponse(Long showDetailId, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
        this.showDetailId = showDetailId;
        this.sequence = sequence;
        this.showFromDt = showFromDt;
        this.showToDt = showToDt;
        this.reservationFromDt = reservationFromDt;
        this.reservationToDt = reservationToDt;
        this.delYn = delYn;
    }

    public ReservationShowDetailResponse(Long showDetailId) {
        this.showDetailId = showDetailId;
    }

    public static ReservationShowDetailResponse of(Long showDetailId, Integer sequence, LocalDateTime showFromDt, LocalDateTime showToDt, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, String delYn) {
        return new ReservationShowDetailResponse(showDetailId, sequence, showFromDt, showToDt, reservationFromDt, reservationToDt, delYn);
    }

    public void addSeatResponse(ReservationDao reservationDao) {
        ReservationSeatResponse now = new ReservationSeatResponse(reservationDao.getSeat().getId());
        if (reservationSeatResponses.contains(now)) {
            reservationSeatResponses
                    .get(reservationSeatResponses.indexOf(now))
                    .addCount();
        } else {
            ReservationSeatResponse seatResponse = ReservationSeatResponse.of(
                    reservationDao.getSeat().getId(),
                    reservationDao.getSeat().getGradeDetails().getGrade(),
                    reservationDao.getSeat().getGradeDetails().getPrice());
            reservationSeatResponses.add(seatResponse);

            seatResponse.addCount();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationShowDetailResponse that = (ReservationShowDetailResponse) o;

        return getShowDetailId().equals(that.getShowDetailId());
    }

    @Override
    public int hashCode() {
        return getShowDetailId().hashCode();
    }
}
