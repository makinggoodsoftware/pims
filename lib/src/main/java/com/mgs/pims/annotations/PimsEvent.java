package com.mgs.pims.annotations;

import com.mgs.pims.event.PimsEventType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PimsEvent {
	PimsEventType type();
}
