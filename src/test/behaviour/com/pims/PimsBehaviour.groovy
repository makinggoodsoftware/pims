package com.pims

import com.pims.core.Pims
import com.pims.core.PimsMapEntities
import com.pims.spring.PimsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@ContextConfiguration(classes = [PimsConfig.class])
class PimsBehaviour extends Specification{
    @Autowired Pims pims
    @Autowired PimsMapEntities pimsMapEntities

    def "pims is created" (){
        expect:
        pims != null
    }

    def "pimsMapEntities is created" (){
        expect:
        pimsMapEntities != null
    }
}
