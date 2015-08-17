package com.mgs.pims.web.config;

import com.google.common.collect.ImmutableMap;
import com.mgs.pims.Pims;
import com.mgs.pims.types.retriever.PimsRetriever;
import com.mgs.pims.web.PimsContext;
import com.mgs.pims.web.retriever.PersonRetriever;
import com.mgs.spring.bean.MongoDbDef;
import com.mgs.spring.glue.mongo.MongoConfig;
import com.mgs.spring.glue.pims.MixersConfig;
import com.mgs.spring.glue.pims.PimsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@Configuration
@Import({
        MixersConfig.class,
        MongoConfig.class
})
public class PimsWebConfig {
    @Resource
    private Pims pims;

    @Bean
    public PimsContext pimsContext() {
        return new PimsContext(new ImmutableMap.Builder<String, PimsRetriever>().
                put("person", pims.stateless(PersonRetriever.class)).
                build());
    }

    @Bean
    public MongoDbDef mongoDbDef() {
        return new MongoDbDef("localhost", 27017, "testDb");
    }
}
