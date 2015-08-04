package com.mgs.spring.pims;

import com.mgs.pims.linker.mixer.NullMixer;
import com.mgs.pims.types.base.PimsBaseEntities;
import com.mgs.pims.types.builder.PimsBuilders;
import com.mgs.pims.types.map.PimsMapEntities;
import com.mgs.pims.types.persistable.PimsPersistables;
import com.mgs.pims.types.retriever.PimsRetrievers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@Import({
  PimsConfig.class
})
@Configuration
public class MixersConfig {
    @Resource
    private PimsConfig pimsConfig;

    @Bean
    public PimsBuilders pimsBuilders (){
        return new PimsBuilders(pimsConfig.pimsFactory());
    }

    @Bean
    public PimsPersistables pimsResources (){
        return new PimsPersistables();
    }

    @Bean
    public PimsMapEntities pimsMapEntities (){
        return new PimsMapEntities();
    }

    @Bean
    public PimsRetrievers pimsRetrievers (){
        return new PimsRetrievers();
    }

    @Bean
    public PimsBaseEntities pimsBaseEntities (){
        return new PimsBaseEntities();
    }

    @Bean
    public NullMixer nullMixer (){
        return new NullMixer();
    }

}
