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

    def "setup" (){
        testObj = new PimsMethodCaller(pimsParametersMock)
    }


    def "should call delegated method with the proper parameters" (){
        given:
        PimsMethodCallParameters callParameters = new PimsMethodCallParameters(
                pimsEntitySampleMock,
                [] as Object[]
        )
        Object[] actualParams = [pimsEntitySampleMock] as Object []
        pimsParametersMock.parse([PimsMethodParameterType.SOURCE_OBJECT], callParameters) >> actualParams
        PimsMethodDelegator delegator = new PimsMethodDelegator(
                PimsEntitySample,
                pimsMixerSampleMock,
                PimsMixerSample.getMethod("onDoSomething", PimsEntitySample),
                [PimsMethodParameterType.SOURCE_OBJECT]
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
