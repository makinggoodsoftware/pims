package com.mgs.spring.glue.pims;

import com.mgs.pims.core.Pims;
import com.mgs.pims.context.PimsContextFactory;
import com.mgs.pims.core.linker.PimsLinker;
import com.mgs.pims.core.linker.method.PimsMethodCaller;
import com.mgs.pims.core.linker.method.PimsMethodDelegatorFactory;
import com.mgs.pims.core.linker.mixer.PimsMixersProvider;
import com.mgs.pims.core.linker.parameters.PimsParameters;
import com.mgs.pims.core.metaData.MetaDataFactory;
import com.mgs.pims.core.metaData.MetaMetaDataFactory;
import com.mgs.pims.types.ProxyFactory;
import com.mgs.pims.types.metaData.PimsEntityMetaData;
import com.mgs.pims.types.metaData.PimsEntityMetaDataBuilder;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;
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
    public Pims pims() {
        return new Pims(
                proxyFactory(),
                reflectionsConfig.typeParser(),
                mapsConfig.mapUtils()
        );
    }

    @Bean
    public PimsContextFactory pimsContextFactory() {
        return new PimsContextFactory(
                reflectionsConfig.typeParser(),
                metaDataFactory(),
                pimsLinker()
        );
    }

    @Bean
    public ProxyFactory proxyFactory() {
        return new ProxyFactory(
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

    @Bean
    public MetaDataFactory metaDataFactory() {
        TypeParser typeParser = reflectionsConfig.typeParser();
        ParsedType metaDataType = typeParser.parse(PimsEntityMetaData.class);
        return new MetaDataFactory(
                reflectionsConfig.fieldAccessorParser(),
                proxyFactory(),
                metaDataType,
                typeParser.parse(PimsEntityMetaDataBuilder.class),
                metaMetaDataFactory().metaMetadata(metaDataType));
    }

    @Bean
    public MetaMetaDataFactory metaMetaDataFactory() {
        return new MetaMetaDataFactory(reflectionsConfig.fieldAccessorParser());
    }
}
