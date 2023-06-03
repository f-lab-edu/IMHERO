package com.imhero

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.transaction.Transactional

@Transactional
@SpringBootTest
class ImheroApplicationTest extends Specification{

    def "contextLoads"() {
        expect:
        1==1
    }
}
