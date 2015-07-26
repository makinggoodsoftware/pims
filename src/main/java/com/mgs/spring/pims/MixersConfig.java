package com.mgs.spring.pims;

import com.mgs.pims.core.PimsMapEntities;
import com.mgs.pims.core.PimsMethodCaller;
import com.mgs.pims.core.PimsMixersProvider;
import com.mgs.pims.core.PimsParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MixersConfig {
    @Bean
    public PimsMapEntities pimsMapEntities (){
        return new PimsMapEntities();
    }

    @Bean
    public PimsMixersProvider pimsMixersProvider (){
        return new PimsMixersProvider();
    }

    @Bean
    public PimsParameters pimsParameters() {
        return new PimsParameters();
    }

    @Bean
    public PimsMethodCaller pimsMethodCaller() {
        return new PimsMethodCaller(pimsParameters());
    }
}
