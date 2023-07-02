package com.imhero.reservation.dto.response;

import com.imhero.reservation.dto.ReservationDao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationShowResponse {
    private Long showId;
    private String title;
    private String artist;
    private String place;
    private LocalDateTime showFromDate;
    private LocalDateTime showToDate;
    private String delYn;
    List<ReservationShowDetailResponse> reservationShowDetailResponses = new ArrayList<>();

    public ReservationShowResponse(Long showId, String title, String artist, String place, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        this.showId = showId;
        this.title = title;
        this.artist = artist;
        this.place = place;
        this.showFromDate = showFromDate;
        this.showToDate = showToDate;
        this.delYn = delYn;
    }

    public ReservationShowResponse(Long showId) {
        this.showId = showId;
    }

    public static ReservationShowResponse of(Long showId, String title, String artist, String place, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        return new ReservationShowResponse(showId, title, artist, place, showFromDate, showToDate, delYn);
    }

    public void addShowDetailResponse(ReservationDao reservationDao) {
        ReservationShowDetailResponse now = new ReservationShowDetailResponse(reservationDao.getShowDetail().getId());
        if (reservationShowDetailResponses.contains(now)) {
            reservationShowDetailResponses
                    .get(reservationShowDetailResponses.indexOf(now))
                    .addSeatResponse(reservationDao);
        } else {
            ReservationShowDetailResponse showDetailResponse = ReservationShowDetailResponse.of(
                    reservationDao.getShowDetail().getId(),
                    reservationDao.getShowDetail().getSequence(),
                    reservationDao.getShowDetail().getShowFromDt(),
                    reservationDao.getShowDetail().getShowToDt(),
                    reservationDao.getShowDetail().getReservationFromDt(),
                    reservationDao.getShowDetail().getReservationToDt(),
                    reservationDao.getShowDetail().getDelYn());
            reservationShowDetailResponses.add(showDetailResponse);

            showDetailResponse.addSeatResponse(reservationDao);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationShowResponse that = (ReservationShowResponse) o;

        return getShowId().equals(that.getShowId());
    }

    @Override
    public int hashCode() {
        return getShowId().hashCode();
    }
}
