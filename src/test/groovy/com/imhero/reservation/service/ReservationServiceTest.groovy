package com.imhero.reservation.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.fixture.Fixture
import com.imhero.reservation.domain.Reservation
import com.imhero.reservation.dto.ReservationCancelRequest
import com.imhero.reservation.dto.ReservationRequest
import com.imhero.reservation.repository.ReservationRepository
import com.imhero.show.domain.Seat
import com.imhero.show.service.SeatService
import com.imhero.user.service.UserService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDateTime

@Transactional
@SpringBootTest
class ReservationServiceTest extends Specification {

    def "예매 생성"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService)

        userService.getUserByEmailOrElseThrow(_) >> Fixture.getUser()
        seatService.getSeatByIdOrElseThrow(_) >> Fixture.getSeat()

        ReservationRequest reservationRequest = getReservationRequest()

        Reservation reservation1 = Mock(Reservation.class)
        reservation1.getId() >> 1L
        Reservation reservation2 = Mock(Reservation.class)
        reservation2.getId() >> 2L

        (reservationRepository.save(_)) >>> [reservation1, reservation2]

        when:
        def ids = reservationService.save("test@gmail.com", reservationRequest)

        then:
        ids.size() == 2
    }

    def "예매 취소"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService)
        Reservation reservation = Fixture.getReservation()
        Seat seat = Fixture.getSeat()

        int count = 3
        int before = seat.totalQuantity - seat.reserve(count)

        reservation.getDelYn()
        userService.getUserByEmailOrElseThrow(_) >> reservation.getUser()
        seatService.getSeatByIdOrElseThrow(_) >> seat
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(), Fixture.getReservation()]
        reservationRepository.updateDelYnByIds(_) >> count

        when:
        def reservationCancelRequest = getReservationCancelRequest()
        reservationService.cancel("test", reservationCancelRequest)

        then:
        seat.getCurrentQuantity() == before + reservationCancelRequest.getIds().size()
    }

    def "예매 취소시 유저가 같지 않은 경우"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService)
        Reservation reservation = Fixture.getReservation()

        userService.getUserByEmailOrElseThrow(_) >> Fixture.getNewUser()
        Seat seat = Fixture.getSeat()
        seat.reserve(3)
        seatService.getSeatByIdOrElseThrow(_) >> seat
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(), Fixture.getReservation()]

        when:
        def reservationCancelRequest = getReservationCancelRequest()
        reservationService.cancel("test", reservationCancelRequest)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.UNAUTHORIZED_BEHAVIOR
    }

    private ReservationCancelRequest getReservationCancelRequest() {
        return new ReservationCancelRequest(1L, Set.of(1L, 2L, 3L))
    }

    private getSeatService() {
        return Mock(SeatService.class)
    }

    private getUserService() {
        return Mock(UserService.class)
    }

    private getReservationRepository() {
        return Mock(ReservationRepository.class)
    }

    private getReservationRequest() {
        def given = LocalDateTime.now()
        return new ReservationRequest(1L, 2, given)
    }
}
