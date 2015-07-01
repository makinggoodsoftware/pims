package com.mgs.pims.core

import com.mgs.spring.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [AppConfig.class])
class SpringPimsBehaviour extends Specification{
    @Autowired PimsMapEntities pimsMapEntities
    @Autowired PimsMixersProvider pimsMixersProvider
    @Autowired PimsLinker pimsLinker

    def "pimsMapEntities is created" (){
        expect:
        pimsMapEntities != null
    }

    def "pimsMixersProvider is created" (){
        expect:
        pimsMixersProvider != null
        pimsMixersProvider.from(PimsMapEntities).is(pimsMapEntities)
    }

    def "pimsLinker is created" (){
        expect:
        pimsLinker != null
    }
}
