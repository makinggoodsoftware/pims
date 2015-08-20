package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.core.linker.PimsLinker
import com.mgs.pims.core.linker.method.PimsMethodCaller
import com.mgs.pims.core.linker.method.PimsMethodDelegator
import com.mgs.pims.core.linker.parameters.PimsParameters
import com.mgs.pims.proxy.PimsEntityProxy
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.reflections.FieldAccessor
import com.mgs.reflections.ParsedType
import com.mgs.reflections.TypelessMethod
import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = [AppConfig.class])
class PimsLinkerBehaviour extends Specification{
    @Resource PimsLinker pimsLinker
    @Resource PimsMethodCaller caller
    @Resource PimsParameters pimsParameters

    PimsMapEntity pimsMapEntityMock = Mock (PimsMapEntity)
    PimsEntityProxy pimsEntityProxyMock = Mock (PimsEntityProxy)
    Map<String, FieldAccessor> fieldAccessorMapMock = Mock(Map)

    ParsedType parsedTypeMock = Mock (ParsedType)

    def "should call PimsMapEntities.onGetValueMap for getValueMap" (){
        when:
        Map<TypelessMethod, PimsMethodDelegator> linkedMethods = pimsLinker.link(PimsMapEntity).getMethods()
        PimsMethodDelegator getValueMap = linkedMethods.get(TypelessMethod.fromMethod(PimsMapEntity.getMethod("getValueMap")))

        then:
        getValueMap.delegatorMethod.getName() == "onGetValueMap"


        when:
        Map<String, String> valueMap = caller.delegate(
                getValueMap,
                pimsParameters.from(
                        parsedTypeMock,
                        pimsMapEntityMock,
                        pimsEntityProxyMock,
                        null,
                        fieldAccessorMapMock,
                        [:],
                        [value: 'map']
                )
        ) as Map<String, String>

        then:
        valueMap == [value:'map']
    }

    def "should call PimsMapEntities.onGetter correctly for getName" (){
        given:
        Map<TypelessMethod, PimsMethodDelegator> linkedMethods = pimsLinker.link(MyEntity).getMethods()

        when:
        String name = caller.delegate(
                linkedMethods.get(TypelessMethod.fromMethod(MyEntity.getMethod("getName"))),
                pimsParameters.from(
                        parsedTypeMock,
                        pimsMapEntityMock,
                        pimsEntityProxyMock,
                        null,
                        fieldAccessorMapMock,
                        [name: 'Alberto'],
                        [:]
                )
        )

        then:
        name == 'Alberto'
    }

    @PimsEntity
    private static interface MyEntity extends PimsMapEntity {
        String getName ()
    }
}
