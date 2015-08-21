package com.mgs.pims.types.builder;

import com.mgs.maps.Mapping;
import com.mgs.pims.annotations.PimsMethod;
import com.mgs.pims.annotations.PimsMixer;
import com.mgs.pims.annotations.PimsParameter;
import com.mgs.pims.types.ProxyFactory;
import com.mgs.pims.types.map.PimsMapEntity;
import com.mgs.pims.types.serializable.PimsSerializable;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.FieldAccessor;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.TypeParser;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.mgs.pims.core.linker.parameters.PimsMethodParameterType.*;

@PimsMixer
public class PimsBuilders {
    private final ProxyFactory proxyFactory;
    private final TypeParser typeParser;

    public PimsBuilders(ProxyFactory proxyFactory, TypeParser typeParser) {
        this.proxyFactory = proxyFactory;
        this.typeParser = typeParser;
    }

    @PimsMethod(pattern = "update{fieldName}")
    public Object onUpdate(
            @PimsParameter(type = SOURCE_TYPE) ParsedType builderType,
            @PimsParameter(type = SOURCE_OBJECT) PimsMapEntity entity,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap,
            @PimsParameter(type = FIELD_ACCESSORS) Map<String, FieldAccessor> fieldAccessors,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName,
            @PimsParameter(type = METHOD_PARAMETERS) UnaryOperator updater
    ) {
        Object currentValue = domainMap.get(fieldName);
        //noinspection unchecked
        Object updatedValue = updater.apply(currentValue);
        return onWith(
                builderType,
                entity,
                domainMap,
                valueMap,
                fieldAccessors,
                fieldName,
                updatedValue
        );

    }

    @PimsMethod(pattern = "with{fieldName}")
    public Object onWith(
            @PimsParameter(type = SOURCE_TYPE) ParsedType builderType,
            @PimsParameter(type = SOURCE_OBJECT) PimsMapEntity entity,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap,
            @PimsParameter(type = FIELD_ACCESSORS) Map<String, FieldAccessor> fieldAccessors,
            @PimsParameter(type = PLACEHOLDER, name = "fieldName") String fieldName,
            @PimsParameter(type = METHOD_PARAMETERS) Object value
    ) {
        String actualFieldName = actualFieldName(fieldName, fieldAccessors);
        if (isSerializable (builderType)){
            valueMap.put(actualFieldName, serializableValue(value));
        }
        domainMap.put(actualFieldName, value);
        return entity;
    }

    @PimsMethod(pattern = "build")
    public Object onBuild(
            @PimsParameter(type = SOURCE_TYPE) ParsedType sourceType,
            @PimsParameter(type = DOMAIN_MAP) Map<String, Object> domainMap,
            @PimsParameter(type = VALUE_MAP) Map<String, Object> valueMap
    ) {
        Declaration pimsBuilderDeclaration = sourceType.getSuperDeclarations().get(PimsBuilder.class).getOwnDeclaration();
        Declaration toBuildType = pimsBuilderDeclaration.getParameters().get("T");
        boolean isSerializable = PimsSerializable.class.isAssignableFrom(toBuildType.getActualType().get());

        if (isSerializable){
            return proxyFactory.immutable(
                    typeParser.parse(toBuildType),
                    valueMap,
                    null
            );
        }else{
            return proxyFactory.domainEntity(
                    typeParser.parse(toBuildType),
                    domainMap,
                    null
            );
        }
    }

    private Object serializableValue(Object value) {
        if (PimsSerializable.class.isAssignableFrom(value.getClass())){
            PimsSerializable castedValue = (PimsSerializable) value;
            return castedValue.getValueMap();
        } else {
            return value;
        }
    }

    private boolean isSerializable(ParsedType builderType) {
        ParsedType specificBuilderType = builderType.getSuperDeclarations().get(PimsBuilder.class);
        Declaration toBuildType = specificBuilderType.getOwnDeclaration().getParameters().get("T");
        return PimsSerializable.class.isAssignableFrom(toBuildType.getActualType().get());
    }

    private String actualFieldName(String fieldName, Map<String, FieldAccessor> fieldAccessors) {
        FieldAccessor fieldAccessor = fieldAccessors.get(fieldName);
        if (fieldAccessor == null) {
            throw new IllegalStateException();
        }
        Annotation[] annotations = fieldAccessor.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Mapping.class) {
                Mapping mapping = (Mapping) annotation;
                return mapping.mapFieldName();
            }

        }
        return fieldName;
    }

}
