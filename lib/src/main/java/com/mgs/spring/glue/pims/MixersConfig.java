package com.mgs.spring.glue.pims;

import com.mgs.pims.context.Pims;
import com.mgs.pims.core.PimsRawDataProvider;
import com.mgs.pims.core.linker.mixer.NullMixer;
import com.mgs.pims.types.base.PimsBaseEntities;
import com.mgs.pims.types.builder.PimsBuilders;
import com.mgs.pims.types.map.PimsMapEntities;
import com.mgs.pims.types.persistable.PimsPersistables;
import com.mgs.pims.types.retriever.PimsRetrievers;
import com.mgs.pims.types.serializable.PimsSerializables;
import com.mgs.spring.bean.MongoDbDef;
import com.mgs.spring.glue.mongo.MongoConfig;
import com.mgs.spring.glue.reflection.ReflectionsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@Import({
  PimsConfig.class
})
@Configuration
public class MixersConfig {
    @Resource
    private PimsConfig pimsConfig;
    @Resource
    ReflectionsConfig reflectionsConfig;
    @Resource
    MongoConfig mongoConfig;

    @Bean
    public PimsBuilders pimsBuilders(Pims pims) {
        return new PimsBuilders(
                pimsConfig.proxyFactory(),
                reflectionsConfig.typeParser(),
                pims);
    }

    @Bean
    public PimsPersistables pimsResources(MongoDbDef mongoDbDef, PimsRawDataProvider pimsRawDataProvider) {
        return new PimsPersistables(
                pimsConfig.pims(),
                mongoConfig.mongoDaoFactory().mongoDao(
                        mongoDbDef.getHost(),
                        mongoDbDef.getPort(),
                        mongoDbDef.getDbName()
                ),
                reflectionsConfig.typeParser());
    }

    @Bean
    public PimsMapEntities pimsMapEntities (){
        return new PimsMapEntities();
    }

    @Bean
    public PimsRetrievers pimsRetrievers(MongoDbDef mongoDbDef, PimsRawDataProvider pimsRawDataProvider) {
        return new PimsRetrievers(
                mongoConfig.mongoDaoFactory().mongoDao(
                        mongoDbDef.getHost(),
                        mongoDbDef.getPort(),
                        mongoDbDef.getDbName()
                ),
                pimsConfig.pims()
        );
    }

    @Bean
    public PimsBaseEntities pimsBaseEntities (){
        return new PimsBaseEntities();
    }

    @Bean
    public NullMixer nullMixer (){
        return new NullMixer();
    }

    @Bean
    public PimsSerializables pimsSerializables (){
        return new PimsSerializables();
    }
}
