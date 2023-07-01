package com.imhero.reservation.repository

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.reservation.domain.Reservation
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.repository.SeatRepository
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Transactional
@SpringBootTest
class ReservationRepositoryTest extends Specification {

    @Autowired
    private ReservationRepository reservationRepository
    @Autowired
    private UserRepository userRepository
    @Autowired
    private SeatRepository seatRepository

    def "예약 생성"() {
        given:
        Reservation reservation = Reservation.of(getUser(), seat, "N")

        when:
        Reservation savedReservation = reservationRepository.save(reservation)

        then:
        reservation == savedReservation
    }

    def "예약 단건 조회"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation = Reservation.of(user, seat, "N")

        when:
        reservationRepository.save(reservation)
        Reservation findReservation = reservationRepository.findById(reservation.getId()).get()

        then:
        reservation == findReservation
        user == findReservation.getUser()
        seat == findReservation.getSeat()
    }

    def "없는 예약 단건 조회시"() {
        when:
        reservationRepository.findById(99L).get()

        then:
        NoSuchElementException e = thrown()
    }

    def "예약 전체 조회"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "N")

        when:
        reservationRepository.saveAll(List.of(reservation1, reservation2))
        List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId())

        then:
        reservations.size() == 2
    }

    def "예약 취소"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "N")

        when:
        Reservation savedReservation1 = reservationRepository.save(reservation1)
        Reservation savedReservation2 = reservationRepository.save(reservation2)
        int deletedCount = reservationRepository.updateDelYnByIds(Set.of(savedReservation1.getId(), savedReservation2.getId()))

        then:
        deletedCount == 2
    }

    def "예약이 이미 취소된 경우"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "Y")

        when:
        Reservation savedReservation1 = reservationRepository.save(reservation1)
        Reservation savedReservation2 = reservationRepository.save(reservation2)
        int deletedCount = reservationRepository.updateDelYnByIds(Set.of(savedReservation1.getId(), savedReservation2.getId()))

        then:
        deletedCount == 1
    }

    private Seat getSeat() {
        return seatRepository.save(Seat.of(null, Grade.A, 30))
    }

    private User getUser() {
        return userRepository.save(User.of("test@gmail.com", "password", "test", "N"))
    }
}