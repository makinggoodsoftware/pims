package com.mgs.spring;

import com.mgs.spring.custom.CustomConfig;
import com.mgs.spring.glue.GlueConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GlueConfig.class,
        CustomConfig.class,
})
public class AppConfig {
}
