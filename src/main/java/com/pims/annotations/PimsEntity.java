package com.pims.annotations;

import com.pims.core.PimsMapEntities;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
public @interface PimsEntity {
    Class<PimsMapEntities> managedBy();
}
