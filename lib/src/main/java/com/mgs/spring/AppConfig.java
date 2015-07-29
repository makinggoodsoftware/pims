package com.mgs.spring;

import com.mgs.spring.pims.MixersConfig;
import com.mgs.spring.reflection.ReflectionsConfig;
import com.mgs.spring.text.TextConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ReflectionsConfig.class,
        TextConfig.class,
        MixersConfig.class
})
public class AppConfig {
}
