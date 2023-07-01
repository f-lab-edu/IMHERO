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

    public static Show getShow() {
        return Show.of("title", "artist", "place", getUser(), LocalDateTime.now(), LocalDateTime.now(), "N");
    }

    public static ShowDetail getShowDetail() {
        LocalDateTime now = LocalDateTime.now();
        return ShowDetail.of(getShow(), 1, now, now, now, now, "N");
    }

    public static Seat getSeat() {
        return Seat.of(getShowDetail(), Grade.A, 30);
    }

    public static Reservation getReservation() {
        return Reservation.of(getUser(), getSeat(), "N");
    }
}
