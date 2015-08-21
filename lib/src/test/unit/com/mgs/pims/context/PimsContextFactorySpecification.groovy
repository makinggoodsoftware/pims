package com.mgs.pims.context
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.core.linker.PimsLink
import com.mgs.pims.core.linker.PimsLinker
import com.mgs.pims.core.metaData.MetaDataFactory
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.pims.types.metaData.PimsEntityMetaData
import com.mgs.reflections.ParsedType
import com.mgs.reflections.TypeParser
import spock.lang.Specification

class PimsContextFactorySpecification extends Specification {
    PimsContextFactory testObj
    TypeParser typeParserMock = Mock(TypeParser)
    ParsedType simpleTypeMock = Mock(ParsedType)
    PimsEntityMetaData metaDataMock= Mock(PimsEntityMetaData)
    PimsLinker pimsLinkerMock = Mock(PimsLinker)
    PimsLink linkMock = Mock(PimsLink)
    MetaDataFactory metaDataFactoryMock = Mock(MetaDataFactory)

    def "setup" (){
        testObj = new PimsContextFactory(typeParserMock, metaDataFactoryMock, pimsLinkerMock)

        typeParserMock.parse(SimpleEntity) >> simpleTypeMock
        metaDataFactoryMock.metadata(simpleTypeMock) >> metaDataMock
        pimsLinkerMock.link(SimpleEntity) >> linkMock
    }

    def "should create context" (){
        when:
        PimsContext context = testObj.create([SimpleEntity])

        then:
        context.get("SimpleEntity").type == simpleTypeMock
        context.get("SimpleEntity").metaData == metaDataMock
        context.get("SimpleEntity").links == linkMock
//        context.get("SimpleEntity").builder == Optional.empty()
//        context.get("SimpleEntity").provider == Optional.empty()
//        context.get("SimpleEntity").retriever == Optional.empty()
    }

    @PimsEntity
    public static interface SimpleEntity extends PimsMapEntity {
    }
}
