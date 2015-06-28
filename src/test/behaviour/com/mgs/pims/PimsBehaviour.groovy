package com.mgs.pims

import com.mgs.pims.core.Pims
import com.mgs.pims.core.PimsLinker
import com.mgs.pims.core.PimsMapEntities
import com.mgs.pims.core.PimsMixersProvider
import com.mgs.pims.spring.PimsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@ContextConfiguration(classes = [PimsConfig.class])
class PimsBehaviour extends Specification{
    @Autowired Pims pims
    @Autowired PimsMapEntities pimsMapEntities
    @Autowired PimsMixersProvider pimsMixersProvider
    @Autowired PimsLinker pimsLinker

    def "pims is created" (){
        expect:
        pims != null
    }

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
