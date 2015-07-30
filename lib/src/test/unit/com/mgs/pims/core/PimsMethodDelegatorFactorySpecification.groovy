package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.linker.method.PimsMethodDelegator
import com.mgs.pims.linker.method.PimsMethodDelegatorFactory
import com.mgs.pims.linker.mixer.NullMixer
import com.mgs.pims.linker.mixer.PimsMixersProvider
import com.mgs.pims.linker.parameters.PimsParameters
import com.mgs.pims.types.entity.PimsMapEntities
import com.mgs.pims.types.entity.PimsMapEntity
import com.mgs.text.PatternMatcher
import com.mgs.text.PatternMatchingResult
import spock.lang.Specification

class PimsMethodDelegatorFactorySpecification extends Specification {
    PimsMethodDelegatorFactory testObj
    PimsMixersProvider pimsMixersProviderMock = Mock (PimsMixersProvider)
    PatternMatcher patternMatcherMock = Mock (PatternMatcher)
    PimsParameters pimsParametersMock = Mock (PimsParameters)
    NullMixer nullMixerMock = Mock (NullMixer)
    PatternMatchingResult failure = new PatternMatchingResult(false, null)
    PatternMatchingResult success = new PatternMatchingResult(true, [:])
    PatternMatchingResult successWithPlaceholders = new PatternMatchingResult(true, [fieldName:'Name'])
    List parameterTypesMock = Mock(List)

    def "setup" (){
        pimsMixersProviderMock.from(NullMixer) >> nullMixerMock
        testObj = new PimsMethodDelegatorFactory(patternMatcherMock, pimsParametersMock)
        pimsParametersMock.parse (_) >> parameterTypesMock
    }

    def "full matches should take precedence" (){
        given:
        patternMatcherMock.match("getDomainMap", "getDomainMap") >> success
        patternMatcherMock.match("getDomainMap", "getValueMap") >> failure
        patternMatcherMock.match("getDomainMap", "getType") >> failure
        patternMatcherMock.match("getDomainMap", "isMutable") >> failure
        patternMatcherMock.match("getDomainMap", "get{fieldName}") >> successWithPlaceholders


        when:
        PimsMethodDelegator delegator = testObj.link(PimsEntitySample, PimsEntitySample.getMethod("getDomainMap"))

        then:
        delegator.targetType == PimsMapEntities
        delegator.delegatorMethod == PimsMapEntities.getMethod("onGetDomainMap", Map)
        delegator.pimsEntityType == PimsEntitySample
        delegator.pimsMethodParameterTypes == parameterTypesMock
    }

    def "should delegate method to parent if not present" (){
        given:
        patternMatcherMock.match("getName", "getDomainMap") >> failure
        patternMatcherMock.match("getName", "getValueMap") >> failure
        patternMatcherMock.match("getName", "getType") >> failure
        patternMatcherMock.match("getName", "isMutable") >> failure
        patternMatcherMock.match("getName", "get{fieldName}") >> success

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