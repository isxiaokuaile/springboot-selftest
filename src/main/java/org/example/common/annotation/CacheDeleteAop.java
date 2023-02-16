package org.example.common.annotation;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class CacheDeleteAop {
    private static final Logger log = LoggerFactory.getLogger(CacheRequest.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(org.example.common.annotation.CacheDelete)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {
        Item<CacheDelete> item = getDelDeclaredAnnotation(pjp, CacheDelete.class);
        CacheDelete annotation = item.getAnnotation();


        final String redisKey = annotation.key();

        //有key就要直接删除这个缓存
        redisTemplate.delete(redisKey);
        Object proceed = pjp.proceed(pjp.getArgs());

        return proceed;
    }


    /**
     * 获取当前注解对象
     *
     * @param <T>
     * @param joinPoint
     * @param clazz
     * @return
     * @throws NoSuchMethodException getDeclaredAnnotation  获取声明注解
     */

    public static <T extends Annotation> Item<T> getDelDeclaredAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);

        // 拿到方法定义的注解信息
        T annotation = objMethod.getDeclaredAnnotation(clazz);
        Item<T> result = new Item<>();
        result.setAnnotation(annotation);

        return result;
    }


    @Data
    static class Item<T> {
        private T annotation;
        private Class<?> returnType;
    }

}

