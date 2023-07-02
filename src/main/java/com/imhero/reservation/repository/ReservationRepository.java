package com.imhero.reservation.repository;

import com.imhero.reservation.domain.Reservation;
import com.imhero.reservation.dto.ReservationDao;
import com.imhero.reservation.dto.ReservationSellerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("SELECT s FROM Reservation s where s.id IN :ids")
    List<Reservation> findAllById(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.delYn = 'Y' WHERE r.id IN :ids AND r.delYn = 'N'")
    int updateDelYnByIds(@Param("ids") Set<Long> ids);

    @Query("SELECT" +
            " new com.imhero.reservation.dto.ReservationDao(r.user, r.seat.showDetail.show, r.seat.showDetail, r.seat, r)" +
            " FROM Reservation r" +
            " JOIN r.seat" +
            " JOIN r.user" +
            " JOIN r.seat.showDetail" +
            " JOIN r.seat.showDetail.show" +
            " where r.user.email = :email")
    List<ReservationDao> findAllReservationByEmail(@Param("email") String email);
}
