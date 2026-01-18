package com.javaguides.bms.customannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Used on classes
@Retention(RetentionPolicy.RUNTIME) // Available at runtime via reflection
public @interface TableAlias {
    String value(); // Alias value, e.g., "s"
}