package com.mgs.spring.pims;

import com.mgs.pims.core.PimsMapEntities;
import com.mgs.pims.core.PimsMixersProvider;
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
}
