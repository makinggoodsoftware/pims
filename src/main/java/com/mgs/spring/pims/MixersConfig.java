package com.mgs.spring.pims;

import com.mgs.pims.core.entity.PimsMapEntities;
import com.mgs.pims.core.linker.mixer.NullMixer;
import com.mgs.pims.core.linker.method.PimsMethodCaller;
import com.mgs.pims.core.linker.mixer.PimsMixersProvider;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MixersConfig {
    @Bean
    public PimsMapEntities pimsMapEntities (){
        return new PimsMapEntities();
    }

    @Bean
    public NullMixer nullMixer (){
        return new NullMixer();
    }

}
