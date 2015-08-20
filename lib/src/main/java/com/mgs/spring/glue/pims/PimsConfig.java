package com.mgs.spring.glue.pims;

import com.mgs.pims.core.Pims;
import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.linker.method.PimsMethodCaller;
import com.mgs.pims.core.linker.method.PimsMethodDelegatorFactory;
import com.mgs.pims.core.linker.mixer.PimsMixersProvider;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import com.mgs.pims.types.PimsFactory;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.metaData.PimsEntityMetaDataBuilder;
import com.mgs.spring.glue.maps.MapsConfig;
import com.mgs.spring.glue.reflection.ReflectionsConfig;
import com.mgs.spring.glue.text.TextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MapsConfig.class,
        ReflectionsConfig.class,
        TextConfig.class
})
public class PimsConfig {
    @Autowired
    private TextConfig textConfig;
    @Autowired
    private MapsConfig mapsConfig;
    @Autowired
    private ReflectionsConfig reflectionsConfig;


    @Bean
    public Pims pims(){
        return new Pims(
                pimsFactory(),
                reflectionsConfig.typeParser(),
                mapsConfig.mapUtils());
    }

    @Bean
    public PimsFactory pimsFactory (){
        return new PimsFactory(
                mapsConfig.mapTransformer(),
                pimsParameters(),
                pimsLinker(),
                pimsMethodCaller(),
                reflectionsConfig.fieldAccessorParser(),
                reflectionsConfig.typeParser().parse(PimsEntityMetaData.class),
                reflectionsConfig.typeParser().parse(PimsEntityMetaDataBuilder.class));
    }

    @Bean
    public PimsLinker pimsLinker (){
        return new PimsLinker(pimsMethodDelegatorFactory());
    }

    @Bean
    public PimsMethodDelegatorFactory pimsMethodDelegatorFactory (){
        return new PimsMethodDelegatorFactory (
                textConfig.patternMatcher(),
                pimsParameters(),
                reflectionsConfig.reflections()
        );
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
