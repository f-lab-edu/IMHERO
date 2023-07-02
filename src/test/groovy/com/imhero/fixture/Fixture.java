package com.imhero.fixture;

import com.imhero.reservation.domain.Reservation;
import com.imhero.show.domain.Grade;
import com.imhero.show.domain.Seat;
import com.imhero.show.domain.Show;
import com.imhero.show.domain.ShowDetail;
import com.imhero.user.domain.User;

import java.time.LocalDateTime;

public class Fixture {

    public static User getUser() {
        return User.of("test@gmail.com", "12345678", "test", "N");
    }

    public static User getNewUser() {
        return User.of("newTest@gmail.com", "12345678", "newTest", "N");
    }

    public static Show getShow(User user) {
        return Show.of("title", "artist", "place", user, LocalDateTime.now(), LocalDateTime.now(), "N");
    }

    public static ShowDetail getShowDetail(Show show) {
        LocalDateTime now = LocalDateTime.now();
        return ShowDetail.of(show, 1, now, now, now, now, "N");
    }

    public static Seat getSeat(ShowDetail showDetail) {
        return Seat.of(showDetail, Grade.A, 30);
    }

    public static Reservation getReservation(User user, Seat seat) {
        return Reservation.of(user, seat, "N");
    }
}
