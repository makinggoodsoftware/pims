package com.mgs.maps;

import com.mgs.reflections.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class MapTransformer {
    private final MapWalker mapWalker;
    private final Reflections reflections;
    private final TypeParser typeParser;

    public MapTransformer(MapWalker mapWalker, Reflections reflections, TypeParser typeParser) {
        this.mapWalker = mapWalker;
        this.reflections = reflections;
        this.typeParser = typeParser;
    }

    public Map<String, Object> objectify (ParsedType template, Map<String, Object> from, BiFunction<ParsedType, Map<String, Object>, Object> onMapFieldCallback){
        Map<String, Object> result = new HashMap<>();
        mapWalker.walk(template, from, (fieldAccessor, mapValue) -> {
            Object transformed = transform(fieldAccessor.getReturnType(), mapValue, onMapFieldCallback);
            result.put(fieldAccessor.getFieldName(), transformed);
        });
        return result;
    }

    public Object transform(ParsedType type, Object value, BiFunction<ParsedType, Map<String, Object>, Object> onMapFieldCallback) {
        Class<?> declaredType = type.getActualType().get();
        if (reflections.isSimple(declaredType)) return value;
        if (reflections.isCollection(declaredType)) {
            List listOfValues = (List) value;
            Declaration typeOfCollection = type.getOwnDeclaration().getParameters().values().iterator().next();
            //noinspection unchecked
            return listOfValues.stream().
                    map((old) ->
                                transform(typeParser.parse(typeOfCollection), old, onMapFieldCallback)
                    ).collect(
                    toList()
            );
        }
        if (reflections.isAssignableTo(declaredType, Optional.class)) {
            if (value == null) return Optional.empty();
            Declaration typeOfOptional = type.getOwnDeclaration().getParameters().values().iterator().next();
            return Optional.of(
                    transform(typeParser.parse(typeOfOptional), value, onMapFieldCallback)
            );
        }

        if (Map.class.isAssignableFrom(value.getClass())) {
            //noinspection unchecked
            Map<String, Object> castedMap = (Map<String, Object>) value;
            return onMapFieldCallback.apply(type, objectify(type, castedMap, onMapFieldCallback));
        }
        throw new IllegalStateException("Invalid data in the map: " + value);
    }

}
