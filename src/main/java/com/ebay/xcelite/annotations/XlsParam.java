package com.ebay.xcelite.annotations;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be used for
 * conversion from Excel files to List type java objects
 *
 * <p>This annotation is intended only for use on types that extend
 * {@link java.util.Collection Collection&lt;Object&gt;}
 *
 * @author Aleksey Vasiliev
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XlsParam {
}
