package com.mgs.pims.core;

import com.reflections.ParsedType;

@FunctionalInterface
public interface OnMapEntityProcessor {
	Object process(ParsedType mapEntityParsedType, Object value);
}
