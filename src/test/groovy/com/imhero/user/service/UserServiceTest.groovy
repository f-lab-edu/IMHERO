package com.imhero.user.service

import com.imhero.user.domain.Role
import com.imhero.user.domain.User
import com.imhero.user.dto.UserDto
import com.imhero.user.dto.request.UserRequest
import com.imhero.user.repository.UserRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

@Transactional
@SpringBootTest
class UserServiceTest extends Specification {

    // spring security
    // User details Service
    // authentication check
    // repository -> user, service -> dto, controller -> response 응답
    @Shared String email = "email@gamil.com"
    @Shared String password = "password1!!1"
    @Shared String passwordCheck = "password1!!1"
    @Shared String username = "username"

    def "회원 가입"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "N")
        UserRequest userRequest = new UserRequest(email, password,  username)

        when:
        UserDto userResult = userService.save(userRequest)

        then:
        userRepository.save(_) >> user
        userRepository.findUserByEmail(_) >> Optional.empty()
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 가입시 이미 가입된 회원인 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        UserRequest userRequest = new UserRequest(email, password, username)
        User user = User.of(email, password, username, "N")

        when:
        userService.save(userRequest)

        then:
        userRepository.findUserByEmail(_) >> Optional.of(user)
        IllegalArgumentException e = thrown()
        e.getMessage() == "이미 가입된 회원입니다."
    }

    def "회원 조회"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "N")

        when:
        UserDto userResult = userService.findUserByEmail(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.of(user)
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getPassword() == passwordCheck
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 조회시 회원이 없는 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)

        when:
        userService.findUserByEmail(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }

    def "회원 수정"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "N")
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        UserDto userResult = userService.update(userRequest)

        then:
        userRepository.findUserByEmail(_) >> Optional.of(user)
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 수정시 회원이 없는 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        userService.update(userRequest)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }

    def "회원 탈퇴"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "N")

        when:
        boolean result = userService.withdraw(email)

        then:
        result
        userRepository.findUserByEmail(_) >> Optional.of(user)
        user.getDelYn() == "Y"
    }

    def "회원 탈퇴시 이미 탈퇴된 회원인 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "Y")

        when:
        boolean result = userService.withdraw(email)

        then:
        !result
        userRepository.findUserByEmail(_) >> Optional.of(user)
        user.getDelYn() == "Y"
    }

    def "회원 탈퇴시 잘못된 요청일 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)

        when:
        userService.withdraw("               ")

        then:
        IllegalArgumentException e = thrown()
        e.getMessage() == "잘못된 요청입니다."
    }

    def "회원 탈퇴시 회원이 없는 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository)
        User user = User.of(email, password, username, "N")

        when:
        userService.withdraw(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }
}
