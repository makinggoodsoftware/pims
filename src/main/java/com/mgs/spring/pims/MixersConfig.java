package com.mgs.spring.pims;

import com.mgs.pims.core.*;
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
    public NullMixer nullMixer (){
        return new NullMixer();
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
