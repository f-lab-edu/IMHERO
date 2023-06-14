package com.imhero.user.service;

import com.imhero.user.domain.Role;
import com.imhero.user.domain.User;
import com.imhero.user.dto.UserDto;
import com.imhero.user.dto.request.UserRequest;
import com.imhero.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    // TODO : password encoder setting
    // TODO : session setting
    public UserDto save(UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 같지 않습니다.");
        }
        // TODO : Unique Constraint
        userRepository.findUserByEmail(userRequest.getEmail()).ifPresent(it -> {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        });

        return UserDto.from(
                userRepository.save(User.of(
                        userRequest.getEmail(),
                        userRequest.getPassword(),
                        userRequest.getUsername(),
                        "N"
                )));
    }

    @Transactional(readOnly = true)
    public UserDto findUserByEmail(String email) {
        return UserDto.from(getUserByEmailOrElseThrow(email));
    }

    public UserDto update(UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 같지 않습니다.");
        }

        return UserDto
                .from(getUserByEmailOrElseThrow(userRequest.getEmail())
                        .modify(userRequest.getEmail(),
                                userRequest.getPassword(),
                                userRequest.getUsername(),
                                Role.USER));
    }

    public boolean withdraw(String email) {
        if (StringUtils.trimAllWhitespace(email).length() == 0) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        User user = getUserByEmailOrElseThrow(email);
        return user.withdraw();
    }

    @Transactional(readOnly = true)
    public User getUserByEmailOrElseThrow(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
    }


}
