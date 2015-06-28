package com.mgs.pims.spring;

import com.mgs.pims.core.Pims;
import com.mgs.pims.core.PimsFactory;
import com.mgs.pims.core.PimsLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MixersConfig.class)
public class PimsConfig {
    @Autowired
    private MixersConfig mixersConfig;


    @Bean
    public Pims pims (){
        return new Pims();
    }

    @Bean
    public PimsFactory pimsFactory (){
        return null;
    }

    @Bean
    public PimsLinker pimsLinker (){
        return new PimsLinker(mixersConfig.pimsMixersProvider());
    }
}
