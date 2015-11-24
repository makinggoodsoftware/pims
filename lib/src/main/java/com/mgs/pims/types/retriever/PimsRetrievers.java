package com.mgs.pims.types.retriever;

import com.google.common.collect.ImmutableMap;
import com.mgs.mongo.MongoDao;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.core.Pims;
import com.mgs.pims.types.persistable.PimsPersistable;
import com.mgs.pims.types.serializable.PimsSerializable;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;
import com.mongodb.DBCursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.*;

@PimsMixer
public class PimsRetrievers {
    private final MongoDao mongoDao;
    private final Pims pims;

    public PimsRetrievers(MongoDao mongoDao, Pims pims) {
        this.mongoDao = mongoDao;
        this.pims = pims;
    }

    @PimsMethod(pattern = "byField")
    public <Z extends PimsSerializable, T extends PimsPersistable<Z>>
    List<T> onByField(
            @PimsParameter(type = SOURCE_TYPE) ParsedType type,
            @PimsParameter(type = METHOD_PARAMETERS) String fieldName,
            Object fieldValue
    ) {
        ParsedType pimsRetriever = type.getSuperDeclarations().get(PimsRetriever.class);
        Declaration persistableTypeDeclaration = pimsRetriever.getOwnDeclaration().getParameters().get("T");
        Class persistableType = persistableTypeDeclaration.getActualType().get();
        Map<String, Object> query = new ImmutableMap.Builder<String, Object>().
                put("data", new ImmutableMap.Builder<String, Object>().
                        put(fieldName, fieldValue).
                        build()).
                build();
        DBCursor dbMatches = mongoDao.findLiteral(persistableType.getSimpleName(), query);
        List<T> matches = new ArrayList<>();
        while (dbMatches.hasNext()) {
            //noinspection unchecked
            Map<String, Object> valueMap = dbMatches.next().toMap();
            //noinspection unchecked
            matches.add((T) pims.newEntity(persistableType, valueMap));
        }
        return matches;


    }

    @PimsMethod(pattern = "by{fieldName}")
    public <Z extends PimsSerializable, T extends PimsPersistable<Z>>
    List<T> onByFieldInterceptor(
            @PimsParameter(type = SOURCE_OBJECT) PimsRetriever<Z, T> retriever,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName,
            @PimsParameter(type = METHOD_PARAMETERS) Object fieldValue
    ) {
        return retriever.byField(fieldName, fieldValue);
    }
}
