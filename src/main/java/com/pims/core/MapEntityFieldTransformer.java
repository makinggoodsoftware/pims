package com.pims.core;

import com.reflections.Declaration;
import com.reflections.ParsedType;
import com.reflections.Reflections;
import com.reflections.TypeParser;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class MapEntityFieldTransformer {
	private final Reflections reflections;
	private final TypeParser typeParser;

	public MapEntityFieldTransformer(Reflections reflections, TypeParser typeParser) {
		this.reflections = reflections;
		this.typeParser = typeParser;
	}

	public Object transform(ParsedType valueType, Object valueRepresentation, OnMapEntityProcessor onMapEntityProcessor) {
		assertParsedTypeIsResolved(valueType);
		assertNoOptionalFieldIsSet(valueType, valueRepresentation);

		Class<?> declaredType = valueType.getActualType().get();
		if (reflections.isSimple(declaredType)) return valueRepresentation;
		if (reflections.isAssignableTo(declaredType, PimsMapEntity.class)) {
			return onMapEntityProcessor.process(valueType, valueRepresentation);
		}
		if (reflections.isCollection(declaredType)) {
			List listOfValues = (List) valueRepresentation;
			Declaration typeOfCollection = valueType.getOwnDeclaration().getParameters().values().iterator().next();
			//noinspection unchecked
			return listOfValues.stream().
				map((old) ->
					transform(typeParser.parse(typeOfCollection), old, onMapEntityProcessor)
				).collect(
					toList()
				);
		}
		if (reflections.isAssignableTo(declaredType, Optional.class)) {
			if (valueRepresentation == null) return Optional.empty();
			Declaration typeOfOptional = valueType.getOwnDeclaration().getParameters().values().iterator().next();
			return Optional.of(transform(typeParser.parse(typeOfOptional), valueRepresentation, onMapEntityProcessor));
		}
		throw new IllegalStateException("Invalid data in the map: " + valueRepresentation);
	}

	private void assertParsedTypeIsResolved(ParsedType returnType) {
		if (! returnType.getActualType().isPresent()) {
			throw new IllegalStateException("Can't map into a type which is not resolved");
		}
	}

	private void assertNoOptionalFieldIsSet(ParsedType returnType, Object rawValue) {
		if (!isOptional(returnType)) {
			if (rawValue == null) throw new IllegalStateException("Receiving null for a field that is not Optional" + returnType);
		}
	}

	private boolean isOptional(ParsedType parsedType) {
		Optional<Class> actualType = parsedType.getActualType();
		return actualType.isPresent() && actualType.get().equals(Optional.class);
	}
}
