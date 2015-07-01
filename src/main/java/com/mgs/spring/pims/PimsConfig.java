package com.mgs.spring.pims;

import com.mgs.pims.core.PimsFactory;
import com.mgs.pims.core.PimsLinker;
import com.mgs.pims.core.PimsMethodDelegatorFactory;
import com.mgs.spring.reflection.ReflectionsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MixersConfig.class)
public class PimsConfig {
    @Autowired
    private MixersConfig mixersConfig;
    @Autowired
    private ReflectionsConfig reflectionsConfig;


    @Bean
    public PimsFactory pimsFactory (){
        return null;
    }

    @Bean
    public PimsLinker pimsLinker (){
        return new PimsLinker(pimsMethodDelegatorFactory());
    }

    @Bean
    public PimsMethodDelegatorFactory pimsMethodDelegatorFactory (){
        return new PimsMethodDelegatorFactory (mixersConfig.pimsMixersProvider());
    }
}
