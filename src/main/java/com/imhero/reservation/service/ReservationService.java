package com.imhero.reservation.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.reservation.domain.Reservation;
import com.imhero.reservation.domain.ReservationCancelRequest;
import com.imhero.reservation.dto.ReservationRequest;
import com.imhero.reservation.repository.ReservationRepository;
import com.imhero.show.domain.Seat;
import com.imhero.show.service.SeatService;
import com.imhero.user.domain.User;
import com.imhero.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final SeatService seatService;

    @Transactional
    public Set<Long> save(String userName, ReservationRequest reservationRequest) {
        Set<Long> ids = new HashSet<>();
        User user = getUser(userName);
        Seat seat = seatService.getSeatByIdOrElseThrow(reservationRequest.getSeatId());
        for (int i = 0; i < reservationRequest.getCount(); i++) {
            Long id = reservationRepository.save(Reservation.of(user, seat, "N")).getId();
            ids.add(id);
        }
        seat.reserve(ids.size());
        return ids;
    }

    @Transactional
    public void cancel(String userName, ReservationCancelRequest reservationCancelRequest) {
        User user = getUser(userName);
        Seat seat = seatService.getSeatByIdOrElseThrow(reservationCancelRequest.getSeatId());

        List<Reservation> reservations = reservationRepository.findAllById(reservationCancelRequest.getIds());
        if (!reservations.get(0).getUser().getEmail().equals(user.getEmail())) {
            throw new ImheroApplicationException(ErrorCode.UNAUTHORIZED_BEHAVIOR);
        }

        List<Long> reservationIds = reservations.stream()
                                                .map(Reservation::getId)
                                                .collect(Collectors.toList());

        reservationRepository.updateDelYnByIds(reservationIds);
        seat.cancel(reservations.size());
    }

    private User getUser(String userName) {
        return userService.getUserByEmailOrElseThrow(userName);
    }
}
