package com.mgs.pims.core

import com.mgs.pims.Pims
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = [AppConfig.class])
class PimsBehaviour extends Specification{
    @Resource Pims pims

    def "should create a simple builder" (){
        when:
        MyInterfaceBuilder myInterfaceBuilder = pims.newBuilder(MyInterfaceBuilder)
        myInterfaceBuilder.withName('Alberto')

        then:
        myInterfaceBuilder.name == 'Alberto'

        when:
        MyInterface alberto = myInterfaceBuilder.build()

        then:
        alberto.name == 'Alberto'
    }

    @PimsEntity
    private static interface MyInterface extends PimsMapEntity {
        String getName ()
    }

    @PimsEntity
    private static interface MyInterfaceBuilder extends MyInterface, PimsBuilder<MyInterface> {
        MyInterfaceBuilder withName (String name)
    }
}
