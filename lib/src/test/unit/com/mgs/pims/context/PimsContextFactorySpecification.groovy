package com.mgs.pims.context
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.core.linker.PimsLink
import com.mgs.pims.core.linker.PimsLinker
import com.mgs.pims.core.metaData.MetaDataFactory
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.pims.types.metaData.PimsEntityMetaData
import com.mgs.reflections.ParsedType
import com.mgs.reflections.TypeParser
import spock.lang.Specification

class PimsContextFactorySpecification extends Specification {
    PimsContextFactory testObj
    TypeParser typeParserMock = Mock(TypeParser)
    PimsLinker pimsLinkerMock = Mock(PimsLinker)
    MetaDataFactory metaDataFactoryMock = Mock(MetaDataFactory)

    ParsedType simpleTypeMock = Mock(ParsedType)
    PimsEntityMetaData simpleTypeMetaDataMock = Mock(PimsEntityMetaData)
    PimsLink simpleTypeLinkMock = Mock(PimsLink)
    PimsEntityRelationshipDescriptor builderRelationshipMock = Mock (PimsEntityRelationshipDescriptor)

    ParsedType simpleTypeBuilderMock = Mock(ParsedType)
    PimsEntityMetaData simpleTypeBuilderMetaDataMock = Mock(PimsEntityMetaData)
    PimsLink simpleTypeBuilderLinkMock = Mock(PimsLink)

    def "setup" (){
        testObj = new PimsContextFactory(
                typeParserMock,
                metaDataFactoryMock,
                pimsLinkerMock
        )

        typeParserMock.parse(SimpleEntity) >> simpleTypeMock
        metaDataFactoryMock.metadata(simpleTypeMock) >> simpleTypeMetaDataMock
        pimsLinkerMock.link(SimpleEntity) >> simpleTypeLinkMock

        typeParserMock.parse(SimpleEntityBuilder) >> simpleTypeBuilderMock
        metaDataFactoryMock.metadata(simpleTypeBuilderMock) >> simpleTypeBuilderMetaDataMock
        pimsLinkerMock.link(SimpleEntityBuilder) >> simpleTypeBuilderLinkMock
    }

    def "should create context" (){
        when:
        PimsContext context = testObj.context([builderRelationshipMock], [SimpleEntity, SimpleEntityBuilder])

        then:
        context.get("SimpleEntity").staticDescriptor.type == simpleTypeMock
        context.get("SimpleEntity").staticDescriptor.metaData == simpleTypeMetaDataMock
        context.get("SimpleEntity").staticDescriptor.links == simpleTypeLinkMock

        context.get("SimpleEntityBuilder").staticDescriptor.type == simpleTypeBuilderMock
        context.get("SimpleEntityBuilder").staticDescriptor.metaData == simpleTypeBuilderMetaDataMock
        context.get("SimpleEntityBuilder").staticDescriptor.links == simpleTypeBuilderLinkMock

//        context.get("SimpleEntity").builder.get() == context.get("SimpleEntityBuilder")
//        context.get("SimpleEntity").provider == Optional.empty()
//        context.get("SimpleEntity").retriever == Optional.empty()
    }

    @PimsEntity
    public static interface SimpleEntity extends PimsMapEntity {
        String getName()
    }

    @PimsEntity
    public static interface SimpleEntityBuilder extends PimsBuilder<SimpleEntity> {
        SimpleEntityBuilder withName(String name)
    }

}
