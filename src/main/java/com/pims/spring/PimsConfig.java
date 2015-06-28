package com.pims.spring;

import com.pims.core.Pims;
import com.pims.core.PimsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.pims")
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
