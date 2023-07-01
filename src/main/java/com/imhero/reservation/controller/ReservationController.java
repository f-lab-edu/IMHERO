package com.imhero.reservation.controller;

import com.imhero.config.exception.Response;
import com.imhero.reservation.dto.ReservationCancelRequest;
import com.imhero.reservation.dto.ReservationRequest;
import com.imhero.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/api/v1/show/reservation")
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("")
    public Response<Set<Long>> save(@RequestBody ReservationRequest reservationRequest, HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        return Response.success(reservationService.save(context.getAuthentication().getName(), reservationRequest));
    }

    @DeleteMapping("")
    public Response<Void> cancel(@RequestBody ReservationCancelRequest reservationCancelRequest, HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        reservationService.cancel(context.getAuthentication().getName(), reservationCancelRequest);
        return Response.success();
    }
}
