package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import spock.lang.Specification

class PimsMethodCallerSpecification extends Specification {
    PimsMethodCaller testObj
    PimsParameters pimsParametersMock = Mock(PimsParameters)
    PimsEntitySample pimsEntitySampleMock = Mock(PimsEntitySample)
    PimsMixerSample pimsMixerSampleMock = Mock(PimsMixerSample)
    Map<PimsMethodParameterType, Object> callParameters = Mock(Map)

    def "setup" (){
        testObj = new PimsMethodCaller(pimsParametersMock)
    }

    def "should call delegated method with the proper parameters" (){
        given:
        Object[] actualParams = [pimsEntitySampleMock] as Object[]
        pimsParametersMock.apply([PimsMethodParameterType.PROXY_OBJECT], callParameters) >> actualParams
        PimsMethodDelegator delegator = new PimsMethodDelegator(
                PimsEntitySample,
                pimsMixerSampleMock,
                PimsMixerSample.getMethod("onDoSomething", PimsEntitySample),
                [PimsMethodParameterType.PROXY_OBJECT]
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
