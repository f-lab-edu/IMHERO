package com.imhero.reservation.dto.response;

import com.imhero.reservation.dto.ReservationDao;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationResponse {
    private String email;
    private List<ReservationShowResponse> reservationShowResponses = new ArrayList<>();

    public ReservationResponse(String email) {
        this.email = email;
    }

    public static ReservationResponse of(String email, List<ReservationDao> reservationDaos) {
        ReservationResponse reservationResponse = new ReservationResponse(email);
        reservationDaos.forEach(reservationResponse::addShowResponse);
        return reservationResponse;
    }

    public void addShowResponse(ReservationDao reservationDao) {
        ReservationShowResponse now = new ReservationShowResponse(reservationDao.getShow().getId());
        if (reservationShowResponses.contains(now)) {
            reservationShowResponses
                    .get(reservationShowResponses.indexOf(now))
                    .addShowDetailResponse(reservationDao);
        } else {
            ReservationShowResponse showResponse = ReservationShowResponse.of(
                    reservationDao.getShow().getId(),
                    reservationDao.getShow().getTitle(),
                    reservationDao.getShow().getArtist(),
                    reservationDao.getShow().getPlace(),
                    reservationDao.getShow().getShowFromDate(),
                    reservationDao.getShow().getShowToDate(),
                    reservationDao.getShow().getDelYn());
            reservationShowResponses.add(showResponse);

            showResponse.addShowDetailResponse(reservationDao);
        }
    }
}
