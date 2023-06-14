package com.imhero.show.repository;

import com.imhero.show.domain.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
