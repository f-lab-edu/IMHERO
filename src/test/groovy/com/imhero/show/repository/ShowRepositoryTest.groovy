package com.imhero.show.repository

import com.imhero.show.domain.Show
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class ShowRepositoryTest extends Specification {

    @Autowired private ShowRepository showRepository
    @Autowired private UserRepository userRepository
    @Autowired private EntityManager em;

    User user
    User modifyUser

    def setup() {
        user = User.of("email", "password", "useranme", "N")
        modifyUser = User.of("modify", "modify", "modify", "N")

        userRepository.save(user)
        userRepository.save(modifyUser)
    }

    def "공연 단건 조회"() {
        given:
        Show show = Show.of("title", "artist", "place", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)

        then:
        show == savedShow
    }

    def "공연 전체 조회"() {
        given:
        Show show1 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show2 = Show.of("title2", "artist2", "place2", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        showRepository.saveAll(List.of(show1, show2))

        then:
        showRepository.findAll().size() == 2
    }

    def "공연 등록"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        savedShow == findShow
    }

    def "공연 수정"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        LocalDateTime modifiedTime = LocalDateTime.now()

        when:
        Show savedShow = showRepository.save(show)
        show.modify("modify", "modify", "modify", modifyUser, modifiedTime, modifiedTime)
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        findShow.getTitle() == "modify"
        findShow.getArtist() == "modify"
        findShow.getPlace() == "modify"
        findShow.getUser() == modifyUser
        findShow.getShowFromDate() == modifiedTime
        findShow.getShowToDate() == modifiedTime
    }

    def "공연 부분 수정"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        LocalDateTime modifiedTime = LocalDateTime.now()

        when:
        Show savedShow = showRepository.save(show)
        show.modify(null, null, null, null, null, null)
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        findShow.getTitle() == savedShow.getTitle()
        findShow.getArtist() == savedShow.getArtist()
        findShow.getPlace() == savedShow.getPlace()
        findShow.getUser() == savedShow.getUser()
        findShow.getShowFromDate() == savedShow.getShowFromDate()
        findShow.getShowToDate() == savedShow.getShowToDate()
    }

    def "공연 취소"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)
        boolean result = savedShow.cancel()
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        result
        findShow.getDelYn() == "Y"
    }

    def "공연이 이미 취소되어 있는 경우"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "Y")

        when:
        Show savedShow = showRepository.save(show)
        boolean result = savedShow.cancel()
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        !result
        findShow.getDelYn() == "Y"
    }
}
