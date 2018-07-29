package com.ebay.xcelite.annotations;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be use for
 * converts from XLS files to list java objects
 *
 * <p>This annotation only use for type who extends {@link java.util.Collection Collection&lt;Object&gt;}
 *
 * @author Aleksey Vasiliev
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XlsParam {
}
