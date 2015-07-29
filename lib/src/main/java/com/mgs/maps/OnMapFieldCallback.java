package com.mgs.maps;

import com.mgs.reflections.FieldAccessor;

@FunctionalInterface
public interface OnMapFieldCallback {
	Object apply(FieldAccessor fieldAccessor, Object mapValue);
}
