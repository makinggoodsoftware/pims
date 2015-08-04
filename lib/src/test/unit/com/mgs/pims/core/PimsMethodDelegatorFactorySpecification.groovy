package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import com.mgs.pims.linker.method.PimsMethodDelegator
import com.mgs.pims.linker.method.PimsMethodDelegatorFactory
import com.mgs.pims.linker.mixer.NullMixer
import com.mgs.pims.linker.mixer.PimsMixersProvider
import com.mgs.pims.linker.parameters.PimsParameters
import com.mgs.pims.types.map.PimsMapEntities
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.reflections.ParsedType
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
        patternMatcherMock.match("getDomainMap", "toString") >> failure


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
        patternMatcherMock.match("getName", "toString") >> failure

        when:
        PimsMethodDelegator delegator = testObj.link(PimsEntitySample, PimsEntitySample.getMethod("getName"))

        then:
        delegator.targetType == PimsMapEntities
        delegator.delegatorMethod == PimsMapEntities.getMethod("onGetter", Map, String)
        delegator.pimsEntityType == PimsEntitySample
        delegator.pimsMethodParameterTypes == parameterTypesMock
    }

    def "should match methods distributed in many interfaces" (){
        given:
        patternMatcherMock.match("getName", "getDomainMap") >> failure
        patternMatcherMock.match("getName", "getValueMap") >> failure
        patternMatcherMock.match("getName", "getType") >> failure
        patternMatcherMock.match("getName", "isMutable") >> failure
        patternMatcherMock.match("getName", "get{fieldName}") >> successWithPlaceholders
        patternMatcherMock.match("getName", "toString") >> failure

        patternMatcherMock.match("getAge", "getDomainMap") >> failure
        patternMatcherMock.match("getAge", "getValueMap") >> failure
        patternMatcherMock.match("getAge", "getType") >> failure
        patternMatcherMock.match("getAge", "isMutable") >> failure
        patternMatcherMock.match("getAge", "get{fieldName}") >> successWithPlaceholders
        patternMatcherMock.match("getAge", "getAge") >> success
        patternMatcherMock.match("getAge", "toString") >> failure

        when:
        PimsMethodDelegator delegator = testObj.link(CombinedEntity, CombinedEntity.getMethod("getAge"))

        then:
        delegator.targetType == CombinedManager
        delegator.delegatorMethod == CombinedManager.getMethod("onGetAge")
        delegator.pimsEntityType == CombinedEntity
        delegator.pimsMethodParameterTypes == parameterTypesMock

        when:
        delegator = testObj.link(CombinedEntity, CombinedEntity.getMethod("getName"))

        then:
        delegator.targetType == PimsMapEntities
        delegator.delegatorMethod == PimsMapEntities.getMethod("onGetter", Map, String)
        delegator.pimsEntityType == CombinedEntity
        delegator.pimsMethodParameterTypes == parameterTypesMock
    }

    def "should link method overriden from Object" (){
        given:
        patternMatcherMock.match("toString", "getDomainMap") >> failure
        patternMatcherMock.match("toString", "getValueMap") >> failure
        patternMatcherMock.match("toString", "getType") >> failure
        patternMatcherMock.match("toString", "isMutable") >> failure
        patternMatcherMock.match("toString", "get{fieldName}") >> failure
        patternMatcherMock.match("toString", "toString") >> success

        when:
        PimsMethodDelegator delegator = testObj.link(PimsEntitySample, PimsEntitySample.getMethod("toString"))

        then:
        delegator.targetType == PimsMapEntities
        delegator.delegatorMethod == PimsMapEntities.getMethod("onToString", Map, ParsedType)
        delegator.pimsEntityType == PimsEntitySample
        delegator.pimsMethodParameterTypes == parameterTypesMock
    }


    @PimsEntity
    private interface PimsEntitySample extends PimsMapEntity{
        String getName()
    }

    @PimsEntity (managedBy = CombinedManager)
    private interface PimsEntitySampleII extends PimsMapEntity{
        Integer getAge()
    }

    @PimsEntity
    private interface CombinedEntity extends PimsEntitySample, PimsEntitySampleII{
        Integer getAge()
    }

    @PimsMixer
    private class CombinedManager {
        @PimsMethod(pattern = "getAge")
        Integer onGetAge() {
            return 15;
        }
    }
}
