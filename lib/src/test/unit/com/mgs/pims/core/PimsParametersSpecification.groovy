package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import com.mgs.pims.linker.method.LinkedMethod
import com.mgs.pims.linker.parameters.PimsMethodParameterType
import com.mgs.pims.linker.parameters.PimsParameters
import com.mgs.pims.proxy.PimsEntityProxy
import com.mgs.pims.types.entity.PimsMapEntities
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.reflections.ParsedType
import spock.lang.Specification

import static PimsMethodParameterType.METHOD_PARAMETERS
import static com.mgs.pims.linker.parameters.ParameterResolution.placeholder
import static com.mgs.pims.linker.parameters.ParameterResolution.simple
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.*

class PimsParametersSpecification extends Specification{
    PimsParameters testObj = new PimsParameters()
    Object parameter1Mock = Mock(Object)
    Object parameter2Mock = Mock(Object)
    Map domainMapMock = Mock (Map)
    Map valueMapMock = Mock (Map)
    ParsedType parsedTypeMock = Mock(ParsedType)

    PimsMapEntity pimsMapEntityMock = Mock (PimsMapEntity)
    PimsEntityProxy pimsEntityProxyMock = Mock (PimsEntityProxy)

    def "should return the correct type of arguments" (){
        when:
        Object[] result = testObj.apply(
            [simple(PROXY_OBJECT), placeholder('fieldName'), simple(METHOD_PARAMETERS)],
            [
                (PROXY_OBJECT) : pimsEntityProxyMock,
                (METHOD_PARAMETERS) : [parameter1Mock, parameter2Mock] as Object []
            ]
        )

        then:
        //noinspection GroovyAssignabilityCheck
        result == [pimsEntityProxyMock, 'fieldName', parameter1Mock, parameter2Mock] as Object[]
    }

    def "when it creates a map of params, should have a value for each param" (){
        when:
        Map<PimsMethodParameterType, Object> params = testObj.from(
                parsedTypeMock,
                pimsMapEntityMock,
                pimsEntityProxyMock,
                [parameter1Mock, parameter2Mock] as Object [],
                domainMapMock,
                valueMapMock
        )

        then:
        params.size() == 6
        params[SOURCE_TYPE] == parsedTypeMock
        params[SOURCE_OBJECT] == pimsMapEntityMock
        params[PROXY_OBJECT] == pimsEntityProxyMock
        params[METHOD_PARAMETERS] == [parameter1Mock, parameter2Mock]
        params[DOMAIN_MAP] == domainMapMock
        params[VALUE_MAP] == valueMapMock
    }

    def "if there is only one non annotated parameter, its assumed to be the proxy" (){
        expect:
        testObj.parse(
                new LinkedMethod(PimsMixerSample.getMethod("onDoSomething", PimsEntitySample), [:])
        ) == [simple(PROXY_OBJECT)]
    }

    def "if there is only one annotated parameter, it must return that type" (){
        expect:
        testObj.parse(
                new LinkedMethod(PimsMapEntities.getMethod("onGetDomainMap", Map), [:])
        ) == [simple(DOMAIN_MAP)]
    }

    def "it should parse placeholders correctly" (){
        expect:
        testObj.parse(
                new LinkedMethod(PimsMapEntities.getMethod("onGetter", Map, String), [fieldName: 'name'])
        ) == [simple(DOMAIN_MAP), placeholder('name')]
    }


    @PimsEntity (managedBy = PimsMixerSample)
    public interface PimsEntitySample extends PimsMapEntity{
        void doSomething ()
    }


    @PimsMixer
    public class PimsMixerSample {
        @PimsMethod(pattern = "doSomething")
        void onDoSomething (PimsEntitySample pimsEntitySample) {}
    }
}
