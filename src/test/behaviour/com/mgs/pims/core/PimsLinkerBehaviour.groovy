package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.linker.method.LinkedMethod
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.pims.linker.PimsLinker
import com.mgs.pims.linker.method.PimsMethodCaller
import com.mgs.pims.linker.method.PimsMethodDelegator
import com.mgs.pims.linker.parameters.PimsParameters
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
        when:
        Map<Method, PimsMethodDelegator> linkedMethods = pimsLinker.link(PimsMapEntity)
        PimsMethodDelegator getValueMap = linkedMethods.get(PimsMapEntity.getMethod("getValueMap"))

        then:
        getValueMap.delegatorMethod.getName() == "onGetValueMap"


        when:
        Map<String, String> valueMap = caller.delegate(
                getValueMap,
                pimsParameters.from(new Object(), null, [:], [value:'map'])
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
                pimsParameters.from(new Object(), null, [name:'Alberto'], [:])
        )

        then:
        name == 'Alberto'
    }

    @PimsEntity
    private static interface MyEntity extends PimsMapEntity {
        String getName ()
    }
}
