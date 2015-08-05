package com.mgs.pims.core

import com.mgs.pims.Pims
import com.mgs.pims.annotations.PimsEntity
import com.mgs.pims.annotations.PimsEvent
import com.mgs.pims.annotations.PimsMixer
import com.mgs.pims.types.builder.PimsBuilder
import com.mgs.pims.types.map.PimsMapEntity
import com.mgs.spring.AppConfig
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION

@ContextConfiguration(classes = [AppConfig.class])
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

    @PimsEntity(managedBy = WithEnventsManager)
    private static interface WithEvents extends PimsMapEntity {
        String getName ()
    }

    @PimsMixer
    private static class WithEnventsManager {
        @PimsEvent(type = INPUT_TRANSLATION)
        public Map<String, Object> translate (Map<String, Object> input){
            input.put("name", ((String)input.get("name")).toLowerCase())
            return input;
        }
    }

    @PimsEntity
    private static interface MyInterface extends PimsMapEntity {
        String getName ()
    }

    @PimsEntity
    private static interface MyInterfaceBuilder extends MyInterface, PimsBuilder<MyInterface> {
        MyInterfaceBuilder withName (String name)
    }
}
