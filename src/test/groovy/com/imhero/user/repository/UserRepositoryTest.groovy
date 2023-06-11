package com.imhero.user.repository

import com.imhero.user.domain.Role
import com.imhero.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager

@Transactional
@SpringBootTest
class UserRepositoryTest extends Specification {

    @Autowired private UserRepository userRepository;
    @Autowired EntityManager em;


    def "회원 가입"() {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)

        then:
        user == savedUser
    }

    def "회원 조회" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        User findUser = userRepository.findById(user.getId()).get()

        then:
        savedUser == findUser
    }

    def "회원 수정" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.modify("modify@gmail.com", "modify", "modifyUsername", Role.ADMIN)
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getEmail() == "modify@gmail.com"
        findUser.getPassword() == "modify"
        findUser.getUsername() == "modifyUsername"
        findUser.getRole() == Role.ADMIN
        findUser.getRole().getName() == Role.ADMIN.getName()
    }

    def "회원 부분 수정" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.modify(null, null, null, null)
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getEmail() == savedUser.getEmail()
        findUser.getPassword() == savedUser.getPassword()
        findUser.getUsername() == savedUser.getUsername()
        findUser.getDelYn() == savedUser.getDelYn()
        findUser.getRole() == savedUser.getRole()
    }

    def "회원 탈퇴" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.withdraw()
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getDelYn() == "Y"
    }
}
