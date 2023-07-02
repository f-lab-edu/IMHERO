package com.imhero.reservation.controller;

import com.imhero.config.exception.Response;
import com.imhero.reservation.dto.request.ReservationCancelRequest;
import com.imhero.reservation.dto.request.ReservationRequest;
import com.imhero.reservation.dto.response.ReservationResponse;
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

    @GetMapping("")
    public Response<ReservationResponse> findAllReservationByEmail(HttpSession session) {
        return Response.success(reservationService.findAllReservationByEmail(getUserName(session)));
    }

    @PostMapping("")
    public Response<Set<Long>> save(@RequestBody ReservationRequest reservationRequest, HttpSession session) {
        return Response.success(reservationService.save(getUserName(session), reservationRequest));
    }

    @DeleteMapping("")
    public Response<Void> cancel(@RequestBody ReservationCancelRequest reservationCancelRequest, HttpSession session) {

        reservationService.cancel(getUserName(session), reservationCancelRequest);
        return Response.success();
    }

    private String getUserName(HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        return context.getAuthentication().getName();
    }
}
