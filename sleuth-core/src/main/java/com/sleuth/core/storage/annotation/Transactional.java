package com.sleuth.core.storage.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** 事务
 * <p>在方法上加上Transaction表示开启事务</p>
 * @author Jonse
 * @date 2021年2月4日
 */
@Documented
@Retention (RUNTIME)
@Target(METHOD)
public @interface Transactional {

}
