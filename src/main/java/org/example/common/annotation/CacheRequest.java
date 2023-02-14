package org.example.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheRequest  {
    /**
     * 缓存时间，默认60秒，单位：秒
     *
     * 定义两个属性，
     *
     * ①expire，设置缓存内容的过期时间，过期后再次访问，则从数据库查询再次进行缓存，
     *
     * ②byUser，是否根据用户维度区分缓存，有些场景不同用户访问的是相同数据，所以这个是否设置为false，则只缓存一份，更节省缓存空间
     *
     * ③key，不根据参数生成缓存，自定义配置，便于后续有更新操作无法处理，具体可以看下面aop的clearCache方法
     */
    int expire() default 60;

    /**
     * 自定义key，后续便于其它接口清理缓存。若无更新操作，可忽略此配置
     */
    String key() default "";
}
