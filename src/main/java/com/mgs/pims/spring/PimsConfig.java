package com.mgs.pims.spring;

import com.mgs.pims.core.Pims;
import com.mgs.pims.core.PimsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MixersConfig.class)
public class PimsConfig {
    @Bean
    Pims pims (){
        return new Pims();
    }

    @Bean
    PimsFactory pimsFactory (){
        return null;
    }
}
