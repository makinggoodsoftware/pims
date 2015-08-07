package com.mgs.spring.glue.reflection;

import com.mgs.reflections.BeanNamingExpert;
import com.mgs.reflections.FieldAccessorParser;
import com.mgs.reflections.Reflections;
import com.mgs.reflections.TypeParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReflectionsConfig {
    @Bean
    public Reflections reflections (){
        return new Reflections();
    }

    @Bean
    public FieldAccessorParser fieldAccessorParser() {
        return new FieldAccessorParser(
                beanNamingExpert(),
                typeParser()
        );
    }

    @Bean
    public TypeParser typeParser() {
        return new TypeParser();
    }

    @Bean
    public BeanNamingExpert beanNamingExpert() {
        return new BeanNamingExpert();
    }
}
