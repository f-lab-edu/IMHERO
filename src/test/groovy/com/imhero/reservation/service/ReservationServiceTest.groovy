package com.imhero.reservation.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.fixture.Fixture
import com.imhero.reservation.domain.Reservation
import com.imhero.reservation.dto.ReservationDao
import com.imhero.reservation.dto.request.ReservationCancelRequest
import com.imhero.reservation.dto.request.ReservationRequest
import com.imhero.reservation.dto.response.ReservationResponse
import com.imhero.reservation.repository.ReservationRepository
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.show.repository.SeatRepository
import com.imhero.show.repository.ShowDetailRepository
import com.imhero.show.repository.ShowRepository
import com.imhero.show.service.SeatService
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import com.imhero.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDateTime

@Transactional
@SpringBootTest
class ReservationServiceTest extends Specification {
    @Autowired
    private ReservationRepository reservationRepository
    @Autowired
    private UserRepository userRepository
    @Autowired
    private SeatRepository seatRepository

    @Autowired
    private ShowRepository showRepository
    @Autowired
    private ShowDetailRepository showDetailRepository

    def "예매 생성"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService)

        userService.getUserByEmailOrElseThrow(_) >> Fixture.getUser()
        seatService.getSeatByIdOrElseThrow(_) >> Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))

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
        Reservation reservation = Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))
        Seat seat = Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))

        int count = 3
        int before = seat.totalQuantity - seat.reserve(count)

        reservation.getDelYn()
        userService.getUserByEmailOrElseThrow(_) >> reservation.getUser()
        seatService.getSeatByIdOrElseThrow(_) >> seat
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))), Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))]
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
        Reservation reservation = Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))

        userService.getUserByEmailOrElseThrow(_) >> Fixture.getNewUser()
        Seat seat = Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))
        seat.reserve(3)
        seatService.getSeatByIdOrElseThrow(_) >> seat
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))), Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))]

        when:
        def reservationCancelRequest = getReservationCancelRequest()
        reservationService.cancel("test", reservationCancelRequest)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.UNAUTHORIZED_BEHAVIOR
    }

    def "회원 이메일로 모든 예약 조회"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService)

        when:
        User user = Fixture.getUser()
        Show show = Fixture.getShow(user)
        ShowDetail showDetail = Fixture.getShowDetail(show)
        Seat seat = Fixture.getSeat(showDetail)
        Reservation reservation = Fixture.getReservation(user, seat)

        Seat seat2 = Seat.of(showDetail, Grade.VIP, 100)
        Reservation reservation2 = Fixture.getReservation(user, seat2)

        userRepository.save(user)
        showRepository.save(show)
        showDetailRepository.save(showDetail)
        seatRepository.save(seat)
        reservationRepository.save(reservation)

        seatRepository.save(seat2)
        reservationRepository.save(reservation2)

        ReservationDao reservationDao = new ReservationDao(user, show, showDetail, seat, reservation)
        ReservationDao reservationDao2 = new ReservationDao(user, show, showDetail, seat, reservation)
        reservationRepository.findAllReservationByEmail(_) >> List.of(reservationDao, reservationDao2)
        ReservationResponse reservationResponse = reservationService.findAllReservationByEmail("test")

        then:
        reservationResponse.reservationShowResponses.size() == 1
        reservationResponse.reservationShowResponses.get(0).reservationShowDetailResponses.size() == 1
        reservationResponse.reservationShowResponses.get(0).reservationShowDetailResponses.get(0).reservationSeatResponses.size() == 1
        reservationResponse.reservationShowResponses.get(0).reservationShowDetailResponses.get(0).reservationSeatResponses.get(0).count == 2
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
