package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.types.entity.PimsFactory
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.spring.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [AppConfig.class])
class PimsFactoryBehaviour extends Specification {
    @Autowired PimsFactory pimsFactory

    def "should create pims entity" (){
        when:
        MyPimsEntity alberto = pimsFactory.immutable(MyPimsEntity, [name: 'Alberto'])

        then:
Rea        alberto.name == 'Alberto'
    }

    @PimsEntity
    private static interface MyPimsEntity extends PimsMapEntity{
        String getName()
    }
}
