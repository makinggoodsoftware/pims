package com.mgs.pims.core

import com.mgs.pims.Pims
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.pims.types.persistable.PimsPersistable
import com.mgs.pims.types.persistable.PimsPersistableBuilder
import com.mgs.pims.types.retriever.PimsRetriever
import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import sun.dc.pr.PRError

import javax.annotation.Resource

@ContextConfiguration(classes = [AppConfig.class])
class PimsPersistableSpecification extends Specification{
    @Resource Pims pims

    def "should create a persistable" () {
        when:
        Person alberto = pims.newPersistedEntityBuilder(PersonBuilder, PersonDataBuilder, {dataBuilder ->
            dataBuilder.withName('Alberto')
        }).build()

        then:
        alberto.data.name == 'Alberto'

        when:
        alberto.persist()
        PersonRetriever personRetriever = pims.stateless(PersonRetriever)
        List<Person> albertos = personRetriever.byName("Alberto")

        then:
        albertos == [alberto]
    }

    @PimsEntity
    private static interface Person extends PimsPersistable<PersonData>{
    }

    @PimsEntity
    private static interface PersonBuilder extends PimsPersistableBuilder<PersonData, Person> {
    }

    @PimsEntity
    private static interface PersonData extends PimsMapEntity{
        String getName()
    }

    @PimsEntity
    private static interface PersonDataBuilder extends PimsBuilder<PersonData>{
        PersonDataBuilder withName(String name)
    }

    @PimsEntity
    private static interface PersonRetriever extends PimsRetriever<PersonData, Person> {
        List<Person> byName (String name)
    }
}
