package com.imhero.reservation.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCancelRequest {
    private Long seatId;
    List<Long> ids = new ArrayList<>();
}
