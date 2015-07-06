package com.mgs.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import com.mgs.text.PatternMatcher
import com.mgs.text.PatternMatchingResult
import spock.lang.Specification

import java.lang.reflect.Method

import static com.mgs.pims.core.PimsMethodParameterType.*

class PimsLinkerSpecification extends Specification{
    PimsLinker linker
    PimsMixersProvider pimsMixersProviderMock = Mock(PimsMixersProvider)
    PimsMixerSample pimsMixerSampleMock = Mock(PimsMixerSample)
    PimsMapEntities pimsMapEntitiesMock = Mock(PimsMapEntities)
    PatternMatcher patternMatcherMock = Mock(PatternMatcher)
    PatternMatchingResult success = new PatternMatchingResult(true)
    PatternMatchingResult failure = new PatternMatchingResult(false)

    def "setup" () {
        linker = new PimsLinker(new PimsMethodDelegatorFactory(pimsMixersProviderMock, patternMatcherMock))
        pimsMixersProviderMock.from(PimsMixerSample) >> pimsMixerSampleMock
        pimsMixersProviderMock.from(PimsMapEntities) >> pimsMapEntitiesMock
        //noinspection GroovyAssignabilityCheck
        patternMatcherMock.match(_, _) >> {toMatch, pattern ->
            return (toMatch == pattern) ?
                    success :
                    failure
            ;
        }
    }

    def "should link entity correctly" (){
        when:
        Map<Method, PimsMethodDelegator<PimsEntitySample>> result = linker.link(PimsEntitySample)

        then:
        result.size() == 5

        when:
        PimsMethodDelegator<PimsEntitySample> doSomethingDelegator = result[PimsEntitySample.getMethod("doSomething")]

        then:
        doSomethingDelegator.delegator == pimsMixerSampleMock
        doSomethingDelegator.delegatorMethod == PimsMixerSample.getMethod("onDoSomething", PimsEntitySample)
        doSomethingDelegator.pimsMethodParameterTypes == [SOURCE_OBJECT]

        when:
        PimsMethodDelegator<PimsEntitySample> getDomainMapDelegator = result[PimsEntitySample.getMethod("getDomainMap")]

        then:
        getDomainMapDelegator.delegator == pimsMapEntitiesMock
        getDomainMapDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetDomainMap", Map)
        getDomainMapDelegator.pimsMethodParameterTypes == [DOMAIN_MAP]

        when:
        PimsMethodDelegator<PimsEntitySample> getValueMapDelegator = result[PimsEntitySample.getMethod("getValueMap")]

        then:
        getValueMapDelegator.delegator == pimsMapEntitiesMock
        getValueMapDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetValueMap", Map)
        getValueMapDelegator.pimsMethodParameterTypes == [VALUE_MAP]

        when:
        PimsMethodDelegator<PimsEntitySample> isMutableDelegator = result[PimsEntitySample.getMethod("isMutable")]

        then:
        isMutableDelegator.delegator == pimsMapEntitiesMock
        isMutableDelegator.delegatorMethod == PimsMapEntities.getMethod("onIsMutable", PimsEntityProxy)
        isMutableDelegator.pimsMethodParameterTypes == [PROXY_OBJECT]

        when:
        PimsMethodDelegator<PimsEntitySample> getTypeDelegator = result[PimsEntitySample.getMethod("getType")]

        then:
        getTypeDelegator.delegator == pimsMapEntitiesMock
        getTypeDelegator.delegatorMethod == PimsMapEntities.getMethod("onGetType", PimsEntityProxy)
        getTypeDelegator.pimsMethodParameterTypes == [PROXY_OBJECT]
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
