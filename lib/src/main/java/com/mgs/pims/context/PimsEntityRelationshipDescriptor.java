package com.mgs.pims.context;

import com.mgs.pims.types.base.PimsBaseEntity;
import com.mgs.reflections.Declaration;
import com.mgs.reflections.ParsedType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

public class PimsEntityRelationshipDescriptor {
    private final Class<? extends PimsBaseEntity> baseClass;
    private final String parameterPlaceholder;

    public PimsEntityRelationshipDescriptor(Class<? extends PimsBaseEntity> baseClass, String parameterPlaceholder) {
        this.baseClass = baseClass;
        this.parameterPlaceholder = parameterPlaceholder;
    }

    Optional<PimsEntityStaticDescriptor> extract(
            PimsEntityStaticDescriptor baseType,
            Collection<PimsEntityStaticDescriptor> candidateRelationships
    ) {
        List<PimsEntityStaticDescriptor> pimsEntityStaticDescriptorStream = candidateRelationships.stream().
                filter(this::isOfType).
                filter(pimsEntityStaticDescriptor -> applies(baseType, pimsEntityStaticDescriptor))
                .collect(toList());
        if (pimsEntityStaticDescriptorStream.size() > 1) throw new IllegalStateException();
        return pimsEntityStaticDescriptorStream.size() == 0 ? empty() : of(pimsEntityStaticDescriptorStream.get(0));
    }

    boolean isOfType(PimsEntityStaticDescriptor pimsEntityStaticDescriptor) {
        return pimsEntityStaticDescriptor.getType().getSuperDeclarations().get(baseClass) != null;
    }

    private boolean applies(PimsEntityStaticDescriptor baseType, PimsEntityStaticDescriptor pimsEntityStaticDescriptor) {
        ParsedType superType = pimsEntityStaticDescriptor.getType().getSuperDeclarations().get(baseClass);
        Declaration relatesTo = superType.getOwnDeclaration().getParameters().get(parameterPlaceholder);
        return baseType.getType().getActualType().get().equals(relatesTo.getActualType().get());
    }
}
