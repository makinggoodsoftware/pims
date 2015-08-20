package com.mgs.spring;

import com.mgs.spring.glue.GlueConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GlueConfig.class
})
public class AppConfig {
}
