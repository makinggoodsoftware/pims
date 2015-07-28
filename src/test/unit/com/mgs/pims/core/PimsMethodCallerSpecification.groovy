package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import spock.lang.Specification

import static com.mgs.pims.core.ParameterResolution.simple
import static com.mgs.pims.core.PimsMethodParameterType.PROXY_OBJECT

class PimsMethodCallerSpecification extends Specification {
    PimsMethodCaller testObj
    PimsParameters pimsParametersMock = Mock(PimsParameters)
    PimsEntitySample pimsEntitySampleMock = Mock(PimsEntitySample)
    PimsMixerSample pimsMixerSampleMock = Mock(PimsMixerSample)
    Map<PimsMethodParameterType, Object> callParameters = Mock(Map)
    PimsMixersProvider pimsMixersProviderMock = Mock (PimsMixersProvider)

    def "setup" (){
        testObj = new PimsMethodCaller(pimsParametersMock, pimsMixersProviderMock)
        pimsMixersProviderMock.from(PimsMixerSample) >> pimsMixerSampleMock
    }

    def "should call delegated method with the proper parameters" (){
        given:
        Object[] actualParams = [pimsEntitySampleMock] as Object[]
        pimsParametersMock.apply(
                [simple(PROXY_OBJECT)],
                callParameters
        ) >> actualParams
        PimsMethodDelegator delegator = new PimsMethodDelegator(
                PimsEntitySample,
                PimsMixerSample,
                PimsMixerSample.getMethod("onDoSomething", PimsEntitySample),
                [simple(PROXY_OBJECT)]
        )


        when:
        testObj.delegate(delegator, callParameters)

        then:
        1 * pimsMixerSampleMock.onDoSomething(pimsEntitySampleMock)
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
