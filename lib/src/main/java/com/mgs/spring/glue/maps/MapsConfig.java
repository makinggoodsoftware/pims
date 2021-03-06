package com.mgs.spring.glue.maps;

import com.mgs.maps.MapTransformer;
import com.mgs.maps.MapUtils;
import com.mgs.maps.MapWalker;
import com.mgs.spring.glue.reflection.ReflectionsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@Configuration
@Import(ReflectionsConfig.class)
public class MapsConfig {
    @Resource ReflectionsConfig reflectionsConfig;

    @Bean
    public MapWalker mapWalker() {
        return new MapWalker(
                reflectionsConfig.fieldAccessorParser(),
                reflectionsConfig.beanNamingExpert(),
                reflectionsConfig.reflections()
        );
    }

    @Bean
    public MapTransformer mapTransformer() {
        return new MapTransformer(
                mapWalker(),
                reflectionsConfig.reflections(),
                reflectionsConfig.typeParser()
        );
    }

    @Bean
    public MapUtils mapUtils (){
        return new MapUtils();
    }
}
