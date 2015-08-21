package com.mgs.maps;

import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;
import com.mgs.reflections.Reflections;
import com.mgs.reflections.TypeParser;

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
        boolean isValueMap = Map.class.isAssignableFrom(value.getClass());
        boolean parsingMap = Map.class.isAssignableFrom(type.getActualType().get());
        if (reflections.isSimple(declaredType) || (isValueMap && parsingMap)) return value;
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
            Declaration typeOfOptional = type.getOwnDeclaration().getParameters().values().iterator().next();
            return Optional.of(
                    transform(typeParser.parse(typeOfOptional), value, onMapFieldCallback)
            );
        }

        if (isValueMap) {
            //noinspection unchecked
            Map<String, Object> castedMap = (Map<String, Object>) value;
            return onMapFieldCallback.apply(type, objectify(type, castedMap, onMapFieldCallback));
        }
        throw new IllegalStateException("Can't transform the following value :" + value);
    }

}
