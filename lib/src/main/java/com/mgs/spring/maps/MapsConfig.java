package com.mgs.spring.maps;

import com.mgs.maps.MapFieldValueFactory;
import com.mgs.maps.MapWalker;
import com.mgs.spring.reflection.ReflectionsConfig;
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
    public MapFieldValueFactory mapFieldValueFactory() {
        return new MapFieldValueFactory(
                reflectionsConfig.reflections(),
                reflectionsConfig.typeParser()
        );
    }
}
