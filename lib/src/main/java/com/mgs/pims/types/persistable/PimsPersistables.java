package com.mgs.pims.types.persistable;

import com.mgs.mongo.MongoDao;
import com.mgs.pims.Pims;
import com.mgs.pims.annotations.PimsEvent;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.builder.PimsBuilder;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.reflections.ParsedType;

import java.util.Map;

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION;
import static com.mgs.pims.linker.parameters.PimsMethodParameterType.*;

@PimsMixer
public class PimsPersistables {
    private final Pims pims;
    private final MongoDao mongoDao;

    public PimsPersistables(Pims pims, MongoDao mongoDao) {
        this.pims = pims;
        this.mongoDao = mongoDao;
    }

    @PimsEvent(type = INPUT_TRANSLATION)
    public Map<String, Object> translate (Map<String, Object> input){
        if (input.containsKey("id") && input.containsKey("version")) return input;
        if (!input.containsKey("id") && !input.containsKey("version"))
            throw new IllegalStateException("The id and the version must be both specified, or not specified at all!");
        return input;
    }


    @PimsMethod(pattern = "persist")
    public <T extends PimsMapEntity> Object onPersist(
            @PimsParameter(type = SOURCE_TYPE) ParsedType parsedType,
            @PimsParameter(type = SOURCE_OBJECT) PimsPersistable source
    ) {
        PimsPersistableBuilder updater = pims.update(PimsPersistableBuilder.class, source);
        PimsMapEntity withUpdatedVersion = updater.updateVersion(version -> version + 1).build();
        String collectionName = parsedType.getActualType().get().getName();
        mongoDao.persist(collectionName, withUpdatedVersion.getValueMap());
        return withUpdatedVersion;
    }
}
