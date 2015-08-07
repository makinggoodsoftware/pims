package com.mgs.spring.custom.db;

import com.mgs.spring.bean.MongoDbDef;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public MongoDbDef mongoDbDef (){
        return new MongoDbDef("localhost", 27017, "testDb");
    }
}
