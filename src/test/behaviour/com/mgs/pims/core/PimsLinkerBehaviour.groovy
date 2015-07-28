package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource
import java.lang.reflect.Method

@ContextConfiguration(classes = [AppConfig.class])
class PimsLinkerBehaviour extends Specification{
    @Resource PimsLinker pimsLinker
    @Resource PimsMethodCaller caller
    @Resource PimsParameters pimsParameters

    def "should call PimsMapEntities.onGetValueMap for getValueMap" (){
        given:
        Map<Method, PimsMethodDelegator> linkedMethods = pimsLinker.link(PimsMapEntity)

        when:
        Map<String, String> valueMap = caller.delegate(
                linkedMethods.get(PimsMapEntity.getMethod("getValueMap")),
                pimsParameters.from(null, null, null, [value:'map'])
        )

        then:
        valueMap == [value:'map']
    }

    def "should call PimsMapEntities.onGetter correctly for getName" (){
        given:
        Map<Method, PimsMethodDelegator> linkedMethods = pimsLinker.link(MyEntity)

        when:
        String name = caller.delegate(
                linkedMethods.get(MyEntity.getMethod("getName")),
                pimsParameters.from(null, null, [Name:'Alberto'], null)
        )

        then:
        name == 'Alberto'
    }

    @PimsEntity
    private static interface MyEntity extends PimsMapEntity {
        String getName ()
    }
}
