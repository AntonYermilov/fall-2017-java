package me.eranik.xunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that annotated method is test.
 *
 * Test can be ignored if {@code ignore} argument present and is not empty.
 * This argument should contain the reasons why annotated method is ignored.
 *
 * If we expect that some exception would be thrown we can pass the class that
 * describes this exception to the annotation as {@code expected} argument.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    class NONE {}
    String EMPTY = "";

    Class expected() default NONE.class;
    String ignore() default EMPTY;

}
