package com.brm.GoatEngine.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class used to describe POD only types, mostly used for serialization
 */
public class PODType{
    /**
     * Used to customize the property name used in the output serialization file
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SerializeName{
        String value();
    }
}

