package com.mgs.maps

import com.mgs.reflections.ParsedType
import com.mgs.reflections.TypeParser
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spring.TestContext

import javax.annotation.Resource

@ContextConfiguration(classes = [TestContext.class])
class MapTransformerBehaviour extends Specification {
    @Resource TypeParser typeParser
    @Resource MapTransformer mapTransformer

    def "should transform basic type" (){
        given:
        ParsedType type = typeParser.parse(BaseType)

        when:
        Map<String, Object> objectified = mapTransformer.objectify(
                type,
                [name:'Alberto'],
                {valueType, mapValue -> mapValue}
        )

        then:
        objectified['name'] == 'Alberto'
    }

    def "should transform composed type" (){
        given:
        ParsedType type = typeParser.parse(ComposedType)

        when:
        Map<String, Object> objectified = mapTransformer.objectify(
                type,
                [complexType:
                     [otherName: 'Alberto']
                ],
                {valueType, mapValue -> new ComplexType(mapValue['otherName'] as String)}
        )

        then:
        objectified['complexType'].class == ComplexType
        (objectified['complexType'] as ComplexType).otherName == 'Alberto'
    }

    def "should transform composed type and ignore map" (){
        given:
        ParsedType type = typeParser.parse(ComposedTypeWithMap)

        when:
        Map<String, Object> objectified = mapTransformer.objectify(
                type,
                [
                    complexType:[
                            otherName: 'Alberto'
                    ],
                    plainMap:[
                            a: '1',
                            b: '2'
                    ]
                ],
                {valueType, mapValue -> new ComplexType(mapValue['otherName'] as String)}
        )

        then:
        objectified['complexType'].class == ComplexType
        (objectified['complexType'] as ComplexType).otherName == 'Alberto'
        (objectified['plainMap'] as Map) == [a: '1', b: '2']
    }

    static interface BaseType {
        String getName ()
    }

    static interface ComposedType {
        ComplexType getComplexType ()
    }

    static interface ComposedTypeWithMap {
        ComplexType getComplexType ()

        Map<String, String> getPlainMap ()
    }

    static class ComplexType {
        private final String otherName;

        ComplexType(String otherName) {
            this.otherName = otherName
        }

        String getOtherName (){
            return otherName;
        }
    }
}
