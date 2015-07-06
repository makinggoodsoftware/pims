package com.ms.pims.core

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsMethod
import com.mgs.pims.annotations.PimsMixer
import com.mgs.pims.core.PimsEntityProxy
import com.mgs.pims.core.PimsLinker
import com.mgs.pims.core.PimsMapEntities
import com.mgs.pims.core.PimsMapEntity
import com.mgs.pims.core.PimsMethodDelegator
import com.mgs.pims.core.PimsMethodDelegatorFactory
import com.mgs.pims.core.PimsMixersProvider
import com.mgs.reflections.Reflections
import com.mgs.spring.AppConfig
import com.mgs.text.PatternMatcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.lang.reflect.Method

import static com.mgs.pims.core.PimsMethodParameterType.DOMAIN_MAP
import static com.mgs.pims.core.PimsMethodParameterType.PROXY_OBJECT
import static com.mgs.pims.core.PimsMethodParameterType.SOURCE_OBJECT
import static com.mgs.pims.core.PimsMethodParameterType.VALUE_MAP

@ContextConfiguration(classes = [AppConfig.class])
class PimsLinkerAndMethodCallerBehaviour extends Specification{
    PimsLinker linker
    PimsMixersProvider pimsMixersProviderMock = Mock(PimsMixersProvider)
    @Autowired Reflections reflections;
    PimsMixerSample pimsMixerSampleMock = Mock(PimsMixerSample)
    PimsMapEntities pimsMapEntitiesMock = Mock(PimsMapEntities)
    @Autowired PatternMatcher patternMatcher;

    def "setup" () {
        linker = new PimsLinker(new PimsMethodDelegatorFactory(pimsMixersProviderMock, patternMatcher))
        pimsMixersProviderMock.from(PimsMixerSample) >> pimsMixerSampleMock
        pimsMixersProviderMock.from(PimsMapEntities) >> pimsMapEntitiesMock
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
