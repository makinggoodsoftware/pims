package com.mgs.maps;

import com.mgs.reflections.FieldAccessor;

@FunctionalInterface
public interface OnMapField {
	void apply(FieldAccessor fieldAccessor, Object mapValue);
}
