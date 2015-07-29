package com.mgs.spring.pims;

import com.mgs.pims.types.entity.PimsFactory;
import com.mgs.pims.linker.PimsLinker;
import com.mgs.pims.linker.method.PimsMethodCaller;
import com.mgs.pims.linker.method.PimsMethodDelegatorFactory;
import com.mgs.pims.linker.mixer.PimsMixersProvider;
import com.mgs.pims.linker.parameters.PimsParameters;
import com.mgs.spring.maps.MapsConfig;
import com.mgs.spring.reflection.ReflectionsConfig;
import com.mgs.spring.text.TextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MixersConfig.class,
        MapsConfig.class,
        ReflectionsConfig.class
})
public class PimsConfig {
    @Autowired
    private TextConfig textConfig;
    @Autowired
    private MapsConfig mapsConfig;
    @Autowired
    private ReflectionsConfig reflectionsConfig;


    @Bean
    public PimsFactory pimsFactory (){
        return new PimsFactory(
                pimsParameters(),
                pimsLinker(),
                mapsConfig.mapWalker(),
                mapsConfig.mapFieldValueFactory(),
                pimsMethodCaller(),
                reflectionsConfig.typeParser()
        );
    }

    @Bean
    public PimsLinker pimsLinker (){
        return new PimsLinker(pimsMethodDelegatorFactory());
    }

    @Bean
    public PimsMethodDelegatorFactory pimsMethodDelegatorFactory (){
        return new PimsMethodDelegatorFactory (
                textConfig.patternMatcher(),
                pimsParameters ());
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
        return new PimsMethodCaller(pimsParameters(), pimsMixersProvider());
    }
}
