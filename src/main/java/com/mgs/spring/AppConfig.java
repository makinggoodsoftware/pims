package com.mgs.spring;

import com.mgs.spring.pims.PimsConfig;
import com.mgs.spring.reflection.ReflectionsConfig;
import com.mgs.spring.text.TextConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PimsConfig.class,
        ReflectionsConfig.class,
        TextConfig.class
})
public class AppConfig {
}
