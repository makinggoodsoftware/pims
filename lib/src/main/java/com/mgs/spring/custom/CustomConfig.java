package com.mgs.spring.custom;

import com.mgs.spring.custom.db.MongoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MongoConfig.class
})
public class CustomConfig {
}
