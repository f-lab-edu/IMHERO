package com.imhero.reservation.dto;

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
public class ReservationSellerDao {
    private Show show;
    private ShowDetail showDetail;
    private User user;
    private Seat seat;
}
