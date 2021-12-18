package org.spring.seek.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that is to be applied to the field of the entity you wish to perform the SEEK
 * pagination by.
 *
 * // FIXME: Provide more instructions on usage in conjunction with Repository
 *
 * @author Elliot Ball
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SeekPaginationField {

}
