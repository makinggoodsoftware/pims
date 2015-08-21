package com.mgs.pims.types.persistable;

import com.mgs.mongo.MongoDao;
import com.mgs.pims.core.Pims;
import com.mgs.pims.annotations.PimsEvent;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.serializable.PimsSerializable;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

import static com.mgs.pims.event.PimsEventType.INPUT_TRANSLATION;
import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.*;

@PimsMixer
public class PimsPersistables {
    private final Pims pims;
    private final MongoDao mongoDao;
    private final TypeParser typeParser;

    public PimsPersistables(Pims pims, MongoDao mongoDao, TypeParser typeParser) {
        this.pims = pims;
        this.mongoDao = mongoDao;
        this.typeParser = typeParser;
    }

    @PimsEvent(type = INPUT_TRANSLATION)
    public Map<String, Object> translate (Map<String, Object> input){
        if (input.containsKey("_id") && input.containsKey("version")) return input;
        if (!input.containsKey("_id") && !input.containsKey("version")) {
            input.put("_id", UUID.randomUUID().toString());
            input.put("version", 0);
            return input;

        }
        throw new IllegalStateException("The id and the version must be both specified, or not specified at all!");
    }


    @PimsMethod(pattern = "persist")
    public <T extends PimsSerializable> Object onPersist(
            @PimsParameter(type = SOURCE_TYPE) ParsedType persistableType,
            @PimsParameter(type = SOURCE_OBJECT) PimsPersistable source
    ) {
        PimsPersistableBuilder updater = builder(persistableType, source);
        //noinspection unchecked
        T withUpdatedVersion = (T) updater.updateVersion(incrementby1()).build();
        String collectionName = persistableType.getActualType().get().getSimpleName();
        mongoDao.persist(collectionName, withUpdatedVersion.getValueMap());
        return withUpdatedVersion;
    }

    private PimsPersistableBuilder builder(@PimsParameter(type = SOURCE_TYPE) ParsedType persistableType, @PimsParameter(type = SOURCE_OBJECT) PimsPersistable source) {
        ParsedType pimsPersistableType = persistableType.getSuperDeclarations().get(PimsPersistable.class);
        Declaration dataType = pimsPersistableType.getOwnDeclaration().getParameters().get("T");
        Map<String, Declaration> parameterTypes = new HashMap<>();
        parameterTypes.put("M", dataType);
        parameterTypes.put("P", persistableType.getOwnDeclaration());
        ParsedType newBuilderType = typeParser.parse(PimsPersistableBuilder.class, parameterTypes);
        return pims.update(newBuilderType, source);
    }

    private UnaryOperator<Integer> incrementby1() {
        return integer -> integer + 1;
    }
}
