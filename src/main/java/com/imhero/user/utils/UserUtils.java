package com.imhero.user.utils;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.user.domain.User;
import com.imhero.user.service.CustomUserDetailsService.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public final class UserUtils {

    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new ImheroApplicationException(ErrorCode.UNAUTHORIZED_BEHAVIOR);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
