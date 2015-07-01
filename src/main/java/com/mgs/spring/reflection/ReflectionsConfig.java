package com.mgs.spring.reflection;

import com.mgs.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReflectionsConfig {
    @Bean
    public Reflections reflections (){
        return new Reflections();
    }
}
