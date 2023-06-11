package com.imhero.config;

import com.imhero.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginUserAuditorAware implements AuditorAware<Long> {

    private final HttpSession httpSession;

    @Override
    public Optional<Long> getCurrentAuditor() {
        User user = (User) httpSession.getAttribute("user");
        if(user == null) {
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }
}