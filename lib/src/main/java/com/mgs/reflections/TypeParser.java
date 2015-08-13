package com.mgs.reflections;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;

public class TypeParser {
    public ParsedType parse(Type type) {
        return parse(type, new HashMap<>());
    }

    public ParsedType parse(Type type, Map<String, Declaration> effectiveParameters) {
        TypeResolution typeResolution = typeResolution(type);
        return parse(typeResolution, effectiveParameters);
    }

    private ParsedType parse(TypeResolution typeResolution, Map<String, Declaration> effectiveParameters) {
        Declaration ownDeclaration = null;
        Optional<String> genericName = typeResolution.getGenericName();

        if (genericName.isPresent()) {
            ownDeclaration = effectiveParameters.get(genericName.get());
        }

        if (ownDeclaration == null) {
            ownDeclaration = new Declaration(typeResolution, effectiveParameters);
        }

        return parse(ownDeclaration);
    }

    public ParsedType parse(Declaration declaration) {
        Optional<Class> specificClass = declaration.getTypeResolution().getSpecificClass();
        if (!specificClass.isPresent()) {
            return new ParsedType(declaration, new HashMap<>());
        }

        return doParse(declaration, specificClass);
    }

    private ParsedType doParse(Declaration declaration, Optional<Class> specificClass) {
        Map<String, Declaration> thisEffectiveParameters = declaration.getParameters();
        Type[] superInterfaces = specificClass.get().getGenericInterfaces();

        Map<Class, ParsedType> superDeclarations = new HashMap<>();
        for (Type genericInterface : superInterfaces) {
            TypeResolution superTypeResolution = typeResolution(genericInterface);
            if (superTypeResolution.getParameterizedType().isPresent()) {
                Map<String, Declaration> superParameters = extractParameters(superTypeResolution, thisEffectiveParameters);
                ParsedType superInterface = parse(superTypeResolution, superParameters);
                superDeclarations.put(superTypeResolution.getSpecificClass().get(), superInterface);
            }
        }
        return new ParsedType(declaration, flatten(superDeclarations));
    }

    private Map<Class, ParsedType> flatten(Map<Class, ParsedType> superDeclarations) {
        Map<Class, ParsedType> flat = new HashMap<>();
        for (Map.Entry<Class, ParsedType> superDeclarationsEntry : superDeclarations.entrySet()) {
            ParsedType parsedType = superDeclarationsEntry.getValue();
            Class key = superDeclarationsEntry.getKey();

            Map<Class, ParsedType> flatParent = flatten(parsedType.getSuperDeclarations());
            flat.putAll(flatParent);
            flat.put(key, parsedType);
        }
        return flat;
    }

    public GenericMethods parseMethods(ParsedType type) {
        Class theClass = type.getOwnDeclaration().getTypeResolution().getSpecificClass().get();
        Method[] methods = theClass.getMethods();
        Map<String, GenericMethod> asMap = new HashMap<>();
        for (Method method : methods) {
            asMap.put(method.getName(), parseMethod(type, method));
        }
        return new GenericMethods(asMap);
    }

    public GenericMethod parseMethod(ParsedType type, Method method) {
        TypeResolution genericTypeResolution = typeResolution(method.getGenericReturnType());
        ParsedType returnType;
        if (method.getDeclaringClass().equals(type.getOwnDeclaration().getActualType().get())) {
            Map<String, Declaration> parameters = extractParameters(genericTypeResolution, type.getOwnDeclaration().getParameters());
            returnType = parse(genericTypeResolution, parameters);
        } else {
            ParsedType superParsedType = type.getSuperDeclarations().get(method.getDeclaringClass());
            if (superParsedType != null) {
                Map<String, Declaration> parameters = extractParameters(genericTypeResolution, superParsedType.getOwnDeclaration().getParameters());
                returnType = parse(genericTypeResolution, parameters);
            } else {
                returnType = parse(genericTypeResolution, new HashMap<>());
            }
        }
        return new GenericMethod(returnType, method);
    }

    private Map<String, Declaration> extractParameters(TypeResolution typeResolution, Map<String, Declaration> effectiveParameters) {
        Map<String, Declaration> ownParameters = new HashMap<>();
        Optional<ParameterizedType> parameterizedTypeOptional = typeResolution.getParameterizedType();

        if (typeResolution.getGenericName().isPresent()) {
            String name = typeResolution.getGenericName().get();
            ownParameters.put(
                    name,
                    effectiveParameters.get(name)
            );
            return ownParameters;
        }

        if (!parameterizedTypeOptional.isPresent() && !typeResolution.getGenericName().isPresent())
            return new HashMap<>();

        ParameterizedType parameterizedType = parameterizedTypeOptional.get();
        TypeVariable[] templateTypeParameters = ((Class) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        int parameterLength = templateTypeParameters.length;
        if (parameterLength != actualTypes.length) {
            throw new IllegalStateException("");
        }

        for (int i = 0; i < parameterLength; i++) {
            String templateParameterName = templateTypeParameters[i].getTypeName();
            ownParameters.put(
                    templateParameterName,
                    assertLegalDeclaration(resolveDeclaration(
                            actualTypes[i],
                            actualTypes[i].getTypeName(),
                            effectiveParameters
                    ))
            );
        }
        return ownParameters;
    }

    private Declaration assertLegalDeclaration(Declaration declaration) {
        if (declaration == null) {
            throw new IllegalStateException();
        }
        return declaration;
    }

    private Declaration resolveDeclaration(Type actualType, String effectiveParameterName, Map<String, Declaration> effectiveParameters) {
        TypeResolution actualTypeResolution = typeResolution(actualType);
        if (actualTypeResolution.getSpecificClass().isPresent()) {
            return new Declaration(actualTypeResolution, new HashMap<>());
        } else {
            return effectiveParameters.get(effectiveParameterName);
        }
    }

    private TypeResolution typeResolution(Type type) {
        String genericName = null;
        Class specificClass = null;
        ParameterizedType parameterizedType = null;

        boolean parameterized = isParameterized(type);
        if (parameterized) {
            parameterizedType = (ParameterizedType) type;
            specificClass = (Class) parameterizedType.getRawType();
        } else if (Class.class.isAssignableFrom(type.getClass())) {
            specificClass = (Class) type;
        } else {
            genericName = type.getTypeName();
        }

        return new TypeResolution(
                (genericName == null) ? Optional.<String>empty() : of(genericName),
                (specificClass == null) ? Optional.<Class>empty() : of(specificClass),
                (parameterizedType == null) ? Optional.<ParameterizedType>empty() : of(parameterizedType)
        );
    }

    private boolean isParameterized(Type type) {
        return ParameterizedType.class.isAssignableFrom(type.getClass());
    }
}
