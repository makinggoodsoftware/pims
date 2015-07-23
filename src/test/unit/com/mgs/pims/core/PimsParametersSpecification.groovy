package com.mgs.pims.core

import spock.lang.Specification

import static com.mgs.pims.core.PimsMethodParameterType.METHOD_PARAMETERS
import static com.mgs.pims.core.PimsMethodParameterType.SOURCE_OBJECT

class PimsParametersSpecification extends Specification{
    PimsParameters testObj = new PimsParameters()
    Object sourceObjectMock = Mock(Object)
    Object parameter1Mock = Mock(Object)
    Object parameter2Mock = Mock(Object)

    def "should return the correct type of arguments" (){
        when:
        Object[] result = testObj.parse(
            [SOURCE_OBJECT, METHOD_PARAMETERS],
            new PimsMethodCallParameters(
                sourceObjectMock,
                [parameter1Mock, parameter2Mock] as Object []
            )
        )

        then:
        result == [sourceObjectMock, parameter1Mock, parameter2Mock] as Object[]
    }
}
