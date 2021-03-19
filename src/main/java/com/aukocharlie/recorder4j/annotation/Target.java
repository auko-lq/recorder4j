package com.aukocharlie.recorder4j.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/**
 * @author auko
 * @date 2021/3/15 21:02
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({METHOD, CONSTRUCTOR, FIELD, PARAMETER, LOCAL_VARIABLE})
public @interface Target {


}
