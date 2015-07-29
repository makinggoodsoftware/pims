package com.mgs.spring.text;

import com.mgs.text.PatternMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextConfig {
    @Bean
    public PatternMatcher patternMatcher (){
        return new PatternMatcher();
    }
}
