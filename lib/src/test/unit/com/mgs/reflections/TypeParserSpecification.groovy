package com.mgs.reflections

import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.pims.types.persistable.PimsPersistable
import com.mgs.pims.types.persistable.PimsPersistableBuilder
import spock.lang.Specification

class TypeParserSpecification extends Specification {
    TypeParser testObj = new TypeParser()

    def "should parse simple object" (){
        when:
        ParsedType result = testObj.parse(String)

        then:
        result.actualType.get() == String
        result.ownDeclaration.parameters.size() == 0
        ! result.ownDeclaration.typeResolution.genericName.isPresent()
        ! result.ownDeclaration.typeResolution.parameterizedType.isPresent()
        result.superDeclarations.size() == 1
        result.superDeclarations.get(Comparable).ownDeclaration.parameters.get("T").actualType.get() == String
    }

    def "should parse complex scenario" (){
        when:
        ParsedType result = testObj.parse(PersonBuilder)

        then:
        result.actualType.get() == PersonBuilder
        result.ownDeclaration.parameters.size() == 0
        ! result.ownDeclaration.typeResolution.genericName.isPresent()
        ! result.ownDeclaration.typeResolution.parameterizedType.isPresent()
        result.superDeclarations.size() == 2
        result.superDeclarations.get(PimsBuilder) != null
        result.superDeclarations.get(PimsBuilder).ownDeclaration.parameters.containsKey("T")
        result.superDeclarations.get(PimsBuilder).ownDeclaration.parameters.get("T").actualType.get() == Person
    }

    @PimsEntity
    private static interface Person extends PimsPersistable<PersonData> {
    }

    @PimsEntity
    private static interface PersonBuilder extends PimsPersistableBuilder<PersonData, Person> {
    }

    @PimsEntity
    private static interface PersonData extends PimsMapEntity{
        String getName()
    }

    @PimsEntity
    private static interface PersonDataBuilder extends PimsBuilder<PersonData> {
        PersonDataBuilder withName(String name)
    }

}
