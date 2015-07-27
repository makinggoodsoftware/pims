package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import com.mgs.text.PatternMatcher
import com.mgs.text.PatternMatchingResult
import spock.lang.Specification

import java.lang.reflect.Method

class PimsLinkerSpecification extends Specification{
    PimsLinker linker
    PimsMixersProvider pimsMixersProviderMock = Mock(PimsMixersProvider)
    PimsMixerSample pimsMixerSampleMock = Mock(PimsMixerSample)
    PimsMapEntities pimsMapEntitiesMock = Mock(PimsMapEntities)
    PatternMatcher patternMatcherMock = Mock(PatternMatcher)
    PatternMatchingResult success = new PatternMatchingResult(true, [:])
    PatternMatchingResult failure = new PatternMatchingResult(false, null)
    PimsParameters pimsParametersMock = Mock (PimsParameters)
    List onDoSomethingParamsMock = Mock (List)
    List onGetDomainMapParamsMock = Mock (List)
    List onGetValueMapParamsMock = Mock (List)
    List onIsMutableParamsMock = Mock (List)
    List onGetTypeParamsMock = Mock (List)

    def "setup" () {
        linker = new PimsLinker(new PimsMethodDelegatorFactory(patternMatcherMock, pimsParametersMock))
        pimsMixersProviderMock.from(PimsMixerSample) >> pimsMixerSampleMock
        pimsMixersProviderMock.from(PimsMapEntities) >> pimsMapEntitiesMock
        //noinspection GroovyAssignabilityCheck
        patternMatcherMock.match(_, _) >> {toMatch, pattern ->
            return (toMatch == pattern) ?
                    success :
                    failure
            ;
        }
        pimsParametersMock.parse (PimsMixerSample.getMethod("onDoSomething", PimsEntitySample)) >> onDoSomethingParamsMock
        pimsParametersMock.parse (PimsMapEntities.getMethod("onGetDomainMap", Map)) >> onGetDomainMapParamsMock
        pimsParametersMock.parse (PimsMapEntities.getMethod("onGetValueMap", Map)) >> onGetValueMapParamsMock
        pimsParametersMock.parse (PimsMapEntities.getMethod("onIsMutable", PimsEntityProxy)) >> onIsMutableParamsMock
        pimsParametersMock.parse (PimsMapEntities.getMethod("onGetType", PimsEntityProxy)) >> onGetTypeParamsMock

    }

    def "should link entity correctly" (){
        when:
        Map<Method, PimsMethodDelegator<PimsEntitySample>> result = linker.link(PimsEntitySample)

        then:
        result.size() == 5

        when:
        PimsMethodDelegator<PimsEntitySample> doSomethingDelegator = result[PimsEntitySample.getMethod("doSomething")]

        then:
        doSomethingDelegator.targetType == pimsMixerSampleMock
        doSomethingDelegator.delegatorMethod == PimsMixerSample.getMethod("onDoSomething", PimsEntitySample)
        doSomethingDelegator.pimsMethodParameterTypes == onDoSomethingParamsMock

        when:
        PimsMethodDelegator<PimsEntitySample> getDomainMapDelegator = result[PimsEntitySample.getMethod("getDomainMap")]

        then:
        getDomainMapDelegator.targetType == pimsMapEntitiesMock
        getDomainMapDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetDomainMap", Map)
        getDomainMapDelegator.pimsMethodParameterTypes == onGetDomainMapParamsMock

        when:
        PimsMethodDelegator<PimsEntitySample> getValueMapDelegator = result[PimsEntitySample.getMethod("getValueMap")]

        then:
        getValueMapDelegator.targetType == pimsMapEntitiesMock
        getValueMapDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetValueMap", Map)
        getValueMapDelegator.pimsMethodParameterTypes == onGetValueMapParamsMock

        when:
        PimsMethodDelegator<PimsEntitySample> isMutableDelegator = result[PimsEntitySample.getMethod("isMutable")]

        then:
        isMutableDelegator.targetType == pimsMapEntitiesMock
        isMutableDelegator.delegatorMethod == PimsMapEntities.getMethod("onIsMutable", PimsEntityProxy)
        isMutableDelegator.pimsMethodParameterTypes == onIsMutableParamsMock

        when:
        PimsMethodDelegator<PimsEntitySample> getTypeDelegator = result[PimsEntitySample.getMethod("getType")]

        then:
        getTypeDelegator.targetType == pimsMapEntitiesMock
        getTypeDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetType", PimsEntityProxy)
        getTypeDelegator.pimsMethodParameterTypes == onGetTypeParamsMock
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
