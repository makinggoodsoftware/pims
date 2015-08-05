package com.mgs.mongo

import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = [AppConfig.class])
class MongoDaoBehaviour extends Specification {
    @Resource
    MongoDaoFactory mongoDaoFactory
    MongoDao mongoDao
    String randomBit

    def "setup" (){
        mongoDao = mongoDaoFactory.mongoDao("localhost", 27017, "testDb")
        randomBit = UUID.randomUUID();
    }

    def "should perform CRUD operations" (){
        when:
        mongoDao.persist("testCol" , [_id:randomBit])

        then:
        [_id:randomBit] == mongoDao.findLiteral("testCol", [_id:randomBit]).next()
    }
}
