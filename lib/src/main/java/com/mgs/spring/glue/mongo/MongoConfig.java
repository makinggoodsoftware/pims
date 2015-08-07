package com.mgs.spring.glue.mongo;

import com.mgs.mongo.MongoDaoFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public MongoDaoFactory mongoDaoFactory (){
        return new MongoDaoFactory();
    }
}
