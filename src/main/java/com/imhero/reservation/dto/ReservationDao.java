package com.imhero.reservation.dto;

import com.imhero.reservation.domain.Reservation;
import com.imhero.show.domain.Seat;
import com.imhero.show.domain.Show;
import com.imhero.show.domain.ShowDetail;
import com.imhero.user.domain.User;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDao {
    private User user;
    private Show show;
    private ShowDetail showDetail;
    private Seat seat;
    private Reservation reservation;
}
