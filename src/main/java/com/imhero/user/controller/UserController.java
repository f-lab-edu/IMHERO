package com.imhero.user.controller;

import com.imhero.user.dto.UserDto;
import com.imhero.user.dto.request.UserRequest;
import com.imhero.user.dto.response.LoginResponse;
import com.imhero.user.dto.response.UserResponse;
import com.imhero.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder bCrypt;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserResponse> join(@RequestBody UserRequest userRequest) {
        String rawPassword = userRequest.getPassword();
        String rawPasswordCheck = userRequest.getPasswordCheck();
        userRequest.setPassword(bCrypt.encode(rawPassword));
        userRequest.setPasswordCheck(bCrypt.encode(rawPasswordCheck));

        UserDto savedUserDto = userService.save(userRequest);
        return ResponseEntity.ok(new UserResponse(savedUserDto.getId()));
    }

    @PostMapping("/api/v1/users/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성
            HttpSession session = request.getSession();
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            UserDto userDto = userService.findUserByEmail(userRequest.getEmail());
            LoginResponse loginResponse = new LoginResponse(userDto.getId());
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
