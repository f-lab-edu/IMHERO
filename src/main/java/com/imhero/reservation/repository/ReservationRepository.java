package com.imhero.reservation.repository;

import com.imhero.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("SELECT s FROM Reservation s where s.id IN :ids")
    List<Reservation> findAllById(@Param("ids") List<Long> ids);
}
