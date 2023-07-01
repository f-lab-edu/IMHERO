package com.imhero.reservation.repository;

import com.imhero.reservation.domain.Reservation;
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
}
