package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import spock.lang.Specification

import static com.mgs.pims.core.PimsMethodParameterType.*
import static com.mgs.pims.core.PimsMethodParameterType.METHOD_PARAMETERS

class PimsParametersSpecification extends Specification{
    PimsParameters testObj = new PimsParameters()
    Object sourceObjectMock = Mock(Object)
    Object parameter1Mock = Mock(Object)
    Object parameter2Mock = Mock(Object)
    Map domainMapMock = Mock (Map)
    Map valueMapMock = Mock (Map)

    def "should return the correct type of arguments" (){
        when:
        Object[] result = testObj.apply(
            [PROXY_OBJECT, METHOD_PARAMETERS],
            [
                (PROXY_OBJECT) : sourceObjectMock,
                (METHOD_PARAMETERS) : [parameter1Mock, parameter2Mock] as Object []
            ]
        )

        then:
        result == [sourceObjectMock, parameter1Mock, parameter2Mock] as Object[]
    }

    def "when it creates a map of params, should have a value for each param" (){
        when:
        Map<PimsMethodParameterType, Object> params = testObj.from(
                sourceObjectMock,
                [parameter1Mock, parameter2Mock] as Object [],
                domainMapMock,
                valueMapMock
        )

        then:
        params.size() == PimsMethodParameterType.values().length
        params[PROXY_OBJECT] == sourceObjectMock
        params[METHOD_PARAMETERS] == [parameter1Mock, parameter2Mock]
        params[DOMAIN_MAP] == domainMapMock
        params[VALUE_MAP] == valueMapMock
    }

    def "if there is only one non annotated parameter, its assumed to be the proxy" (){
        expect:
        testObj.parse(PimsMixerSample.getMethod("onDoSomething", PimsEntitySample)) == [PROXY_OBJECT]
    }

    def "if there is only one annotated parameter, it must return that type" (){
        expect:
        testObj.parse(PimsMapEntities.getMethod("onGetDomainMap", Map)) == [DOMAIN_MAP]

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