package com.imhero.show.repository;

import com.imhero.show.domain.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    Optional<Show> findShowByIdAndDelYn(Long id, String delYn);
    Page<Show> findAllByDelYn(Pageable pageable, String delYn);
}
