package com.mgs.spring.glue;

import com.mgs.spring.glue.mongo.MongoConfig;
import com.mgs.spring.glue.pims.MixersConfig;
import com.mgs.spring.glue.reflection.ReflectionsConfig;
import com.mgs.spring.glue.text.TextConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ReflectionsConfig.class,
        TextConfig.class,
        MixersConfig.class,
        MongoConfig.class
})
public class GlueConfig {
}
