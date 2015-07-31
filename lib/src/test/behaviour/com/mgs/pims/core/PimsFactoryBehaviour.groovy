package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.types.PimsFactory
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.spring.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [AppConfig.class])
class PimsFactoryBehaviour extends Specification {
    @Autowired PimsFactory pimsFactory

    def "should create siple pims entity" (){
        when:
        MyPimsEntity alberto = pimsFactory.immutable(
                MyPimsEntity,
                [name: 'Alberto']
        )

        then:
        alberto.valueMap == [name: 'Alberto']
        alberto.name == 'Alberto'
        ! alberto.mutable
        alberto.type.actualType.get() == MyPimsEntity
        alberto.type.ownDeclaration.parameters.size() == 0
    }

    def "should create complex pims entity" (){
        when:
        ComplexPimsEntity complex = pimsFactory.immutable(
                ComplexPimsEntity,
                [
                    child: [name: 'Alberto']
                ]
        )

        then:
        complex.valueMap == [
                child: [name: 'Alberto']
        ]
        complex.child.name == 'Alberto'
        ! complex.mutable
        complex.type.actualType.get() == ComplexPimsEntity
        complex.type.ownDeclaration.parameters.size() == 0
    }

    @PimsEntity
    private static interface MyPimsEntity extends PimsMapEntity{
        String getName()
    }

    @PimsEntity
    private static interface ComplexPimsEntity extends PimsMapEntity{
        MyPimsEntity getChild()
    }
}
