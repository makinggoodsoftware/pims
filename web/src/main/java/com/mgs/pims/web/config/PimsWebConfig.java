package com.mgs.pims.web.config;

import com.mgs.pims.web.PimsContext;
import com.mgs.spring.glue.pims.PimsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PimsConfig.class
})
public class PimsWebConfig {
    @Bean
    public PimsContext pimsContext() {
        return new PimsContext();
    }
}
