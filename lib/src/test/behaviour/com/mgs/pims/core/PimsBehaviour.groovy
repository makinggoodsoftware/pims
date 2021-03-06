package com.mgs.pims.core

import com.mgs.maps.Mapping
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.context.PimsEntityRelationshipDescriptor
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.pims.types.provider.PimsProvider
import com.mgs.pims.types.retriever.PimsRetriever
import com.mgs.spring.glue.pims.PimsCustomConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spring.TestContext
import spring.testMixers.WithEventsManager

import javax.annotation.Resource
import java.util.function.UnaryOperator

@ContextConfiguration(classes = [TestContext.class, CustomConfig.class])
class PimsBehaviour extends Specification{
    @Resource Pims pims

    def "should create a simple builder with to string" (){
        when:
        MyInterfaceBuilder myInterfaceBuilder = pims.newBuilder(MyInterfaceBuilder)
        myInterfaceBuilder.withName('Alberto')

        then:
        myInterfaceBuilder.name == 'Alberto'

        when:
        MyInterface alberto = myInterfaceBuilder.build()

        then:
        alberto.name == 'Alberto'

        when:
        String toString = alberto.toString()

        then:
        toString != null
    }

    def "should fire and process the input translation process" (){
        when:
        WithEvents result = pims.newEntity(WithEvents, [name: 'ALBERTO'])

        then:
        result.name == 'alberto'
    }

    def "should create simple pims entity"() {
        when:
        MyPimsEntity alberto = pims.newEntity(
                MyPimsEntity,
                [name: 'Alberto']
        )

        then:
        alberto.valueMap == [name: 'Alberto']
        alberto.name == 'Alberto'
        !alberto.mutable
        alberto.type.actualType.get() == MyPimsEntity
        alberto.type.ownDeclaration.parameters.size() == 0
    }

    def "should create complex pims entity"() {
        when:
        ComplexPimsEntity complex = pims.newEntity(
                ComplexPimsEntity,
                [
                        child: [name: 'Alberto']
                ]
        )

        then:
        //noinspection GrEqualsBetweenInconvertibleTypes
        complex.valueMap == [
                child: [name: 'Alberto']
        ]
        complex.child.name == 'Alberto'
        !complex.mutable
        complex.type.actualType.get() == ComplexPimsEntity
        complex.type.ownDeclaration.parameters.size() == 0
    }

    def "should create parameterized pims entity"() {
        when:
        WithParameter withParameter = pims.newEntity(WithParameter, [parameter: 'Alberto'])

        then:
        withParameter.valueMap == [parameter: 'Alberto']
        withParameter.parameter == 'Alberto'
        !withParameter.mutable
        withParameter.type.actualType.get() == WithParameter
        withParameter.type.ownDeclaration.parameters.size() == 0
        withParameter.type.superDeclarations.get(WithParameterBase).ownDeclaration.parameters.get("T").actualType.get() == String
    }

    def "should update field" (){
        when:
        MyInterfaceBuilder builder = pims.newBuilder(MyInterfaceBuilder)
        builder.withName('Alberto')

        then:
        builder.name == 'Alberto'

        when:
        builder.updateName ({s -> s.toUpperCase()} as UnaryOperator<String>)

        then:
        builder.name == 'ALBERTO'
    }

    def "should allow update of an entity through a builder" (){
        when:
        MyInterface alberto1 = pims.newBuilder(MyInterfaceBuilder).withName("Alberto").build()
        MyInterfaceBuilder alberto2 = pims.update(MyInterfaceBuilder, alberto1);

        then:
        alberto1 == alberto2

        when:
        alberto2.updateName ({s -> s.toUpperCase()} as UnaryOperator<String>)

        then:
        alberto1 != alberto2
    }

    def "should read the Mapping annotations to name the fields of the value map"() {
        when:
        PimsEntityWithNamedField result = pims.newEntity(PimsEntityWithNamedField, [differentName: 'a'])

        then:
        result.named == 'a'

        when:
        result = pims.newBuilder(PimsEntityWithNamedFieldBuilder).withNamed('a').build()

        then:
        result.named == 'a'
        result.domainMap.containsKey("named")
        !result.domainMap.containsKey("differentName")

        !result.valueMap.containsKey("named")
        result.valueMap.containsKey("differentName")
    }

    @Configuration
    public static class CustomConfig implements PimsCustomConfig {

        @Override
        @Bean
        public List<PimsEntityRelationshipDescriptor> relationshipDescriptors() {
            List<PimsEntityRelationshipDescriptor> relationshipDescriptors = new ArrayList<>();
            relationshipDescriptors.add(new PimsEntityRelationshipDescriptor(PimsBuilder.class, "T"));
            relationshipDescriptors.add(new PimsEntityRelationshipDescriptor(PimsRetriever.class, "Z"));
            relationshipDescriptors.add(new PimsEntityRelationshipDescriptor(PimsProvider.class, "T"));
            return relationshipDescriptors;
        }

        @Override
        @Bean
        public List<Class<? extends PimsMapEntity>> entitites() {
            return [
                    PimsEntityWithNamedField,
                    PimsEntityWithNamedFieldBuilder,
                    MyPimsEntity,
                    ComplexPimsEntity,
                    WithParameter,
                    WithEvents,
                    MyInterface,
                    MyInterfaceBuilder
            ]
        }
    }

    @PimsEntity
    private static interface PimsEntityWithNamedField extends PimsMapEntity {
        @Mapping(mapFieldName = "differentName")
        String getNamed()
    }

    @PimsEntity
    private static interface PimsEntityWithNamedFieldBuilder extends PimsBuilder<PimsEntityWithNamedField> {
        PimsEntityWithNamedFieldBuilder withNamed(String name)
    }


    @PimsEntity
    private static interface MyPimsEntity extends PimsMapEntity {
        String getName()
    }

    @PimsEntity
    private static interface ComplexPimsEntity extends PimsMapEntity {
        MyPimsEntity getChild()
    }

    @PimsEntity
    private static interface WithParameterBase<T> extends PimsMapEntity {
        T getParameter()
    }

    @PimsEntity
    private static interface WithParameter extends WithParameterBase<String> {
    }

    @PimsEntity(managedBy = WithEventsManager)
    private static interface WithEvents extends PimsMapEntity {
        String getName ()
    }


    @PimsEntity
    private static interface MyInterface extends PimsMapEntity {
        String getName ()
    }

    @PimsEntity
    private static interface MyInterfaceBuilder extends MyInterface, PimsBuilder<MyInterface> {
        MyInterfaceBuilder withName (String name)

        MyInterfaceBuilder updateName (UnaryOperator<String> updater)
    }
}
