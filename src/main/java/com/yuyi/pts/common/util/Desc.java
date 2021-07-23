package com.yuyi.pts.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/22
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Desc {
    /**
     * 类的描述信息
     *
     * @return
     */
    String value() default "";

}
