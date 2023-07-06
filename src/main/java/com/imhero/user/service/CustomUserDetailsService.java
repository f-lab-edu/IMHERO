package com.imhero.user.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.user.domain.User;
import com.imhero.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.EMAIL_NOT_FOUND));
        return new CustomUserDetails(user);
    }

    public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

        private User user;

        private CustomUserDetails(User user) {
            super(user.getEmail(), user.getPassword(), new ArrayList<>());
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}