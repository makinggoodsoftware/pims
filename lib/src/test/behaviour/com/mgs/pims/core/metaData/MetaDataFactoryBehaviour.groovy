package com.mgs.pims.core.metaData

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.context.PimsContextFactorySpecification
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.reflections.TypeParser
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spring.TestContext

import javax.annotation.Resource

@ContextConfiguration(classes = [TestContext.class])
class MetaDataFactoryBehaviour extends Specification {
    @Resource
    MetaDataFactory metaDataFactory
    @Resource
    TypeParser typeParser

    def "should create metadata"() {
        expect:
        metaDataFactory.metadata(typeParser.parse(PimsContextFactorySpecification.SimpleEntity)) != null
    }

    @PimsEntity
    public static interface SimpleEntity extends PimsMapEntity {
        String getName()
    }
}
