package com.mgs.spring.pims;

import com.mgs.pims.linker.mixer.NullMixer;
import com.mgs.pims.types.builder.PimsBuilders;
import com.mgs.pims.types.entity.PimsMapEntities;
import com.mgs.pims.types.resource.PimsResources;
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
    public PimsResources pimsResources (){
        return new PimsResources();
    }

    @Bean
    public PimsMapEntities pimsMapEntities (){
        return new PimsMapEntities();
    }

    @Bean
    public NullMixer nullMixer (){
        return new NullMixer();
    }

}
