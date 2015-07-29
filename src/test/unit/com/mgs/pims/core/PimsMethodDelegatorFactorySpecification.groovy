package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.core.entity.PimsMapEntities
import com.mgs.pims.core.entity.PimsMapEntity
import com.mgs.pims.core.linker.mixer.NullMixer
import com.mgs.pims.core.linker.method.PimsMethodDelegator
import com.mgs.pims.core.linker.method.PimsMethodDelegatorFactory
import com.mgs.pims.core.linker.mixer.PimsMixersProvider
import com.mgs.pims.core.linker.parameters.PimsParameters
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
    PatternMatchingResult success = new PatternMatchingResult(true, [fieldName:'Name'])
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
        patternMatcherMock.match("getDomainMap", "getHasPlaceholders") >> failure
        patternMatcherMock.match("getDomainMap", "isMutable") >> failure
        patternMatcherMock.match("getDomainMap", "get{fieldName}") >> success


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
        patternMatcherMock.match("getName", "getHasPlaceholders") >> failure
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
