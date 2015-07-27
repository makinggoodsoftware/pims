package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.text.PatternMatcher
import com.mgs.text.PatternMatchingResult
import spock.lang.Specification

import static com.mgs.pims.core.PimsMethodParameterType.DOMAIN_MAP
import static com.mgs.pims.core.PimsMethodParameterType.PLACEHOLDER

class PimsMethodDelegatorFactorySpecification extends Specification {
    PimsMethodDelegatorFactory testObj
    PimsMixersProvider pimsMixersProviderMock = Mock (PimsMixersProvider)
    PatternMatcher patternMatcherMock = Mock (PatternMatcher)
    PimsParameters pimsParametersMock = Mock (PimsParameters)
    NullMixer nullMixerMock = Mock (NullMixer)
    PatternMatchingResult failure = new PatternMatchingResult(false, null)
    PatternMatchingResult success = new PatternMatchingResult(true, [fieldName:'Name'])
    List parameterTypesMock = Mock(List)

    def "setup" (){
        pimsMixersProviderMock.from(NullMixer) >> nullMixerMock
        testObj = new PimsMethodDelegatorFactory(patternMatcherMock, pimsParametersMock)
        patternMatcherMock.match("getName", "getDomainMap") >> failure
        patternMatcherMock.match("getName", "getValueMap") >> failure
        patternMatcherMock.match("getName", "getType") >> failure
        patternMatcherMock.match("getName", "isMutable") >> failure
        patternMatcherMock.match("getName", "get{fieldName}") >> success

        pimsParametersMock.parse (PimsMapEntities.getMethod("onGetter", Map, String)) >> parameterTypesMock
    }

    def "should delegate method to parent if not present" (){
        when:
        PimsMethodDelegator delegator = testObj.link(PimsEntitySample, PimsEntitySample.getMethod("getName"))

        then:
        delegator.targetType == PimsMapEntities
        delegator.delegatorMethod == PimsMapEntities.getMethod("onGetter", Map, String)
        delegator.pimsEntityType == PimsEntitySample
        delegator.pimsMethodParameterTypes == parameterTypesMock
    }

    @PimsEntity
    private interface PimsEntitySample extends PimsMapEntity{
        String getName()
    }
}
