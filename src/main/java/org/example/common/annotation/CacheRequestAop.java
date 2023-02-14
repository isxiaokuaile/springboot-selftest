package org.example.common.annotation;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect  //切面编程
@Configuration
public class CacheRequestAop  {
    //可以在IDE控制台打印日志，便于开发，一般加在最上面：
    private static final Logger log = LoggerFactory.getLogger(CacheRequest.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 此处注解路径，按实际项目开发进行配置
    @Pointcut("@annotation(org.example.common.annotation.CacheRequest)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {

        // 获取注解对象,反射
        Item<CacheRequest> item = getDeclaredAnnotation(pjp,CacheRequest.class);
        CacheRequest annotation = item.getAnnotation();
        Class<?> returnType = item.getReturnType();

        long expire = annotation.expire();
        final String redisKey = annotation.key();

        String value = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(value)){
            if(Collection.class.isAssignableFrom(returnType) && Objects.nonNull(item.getClassType())){
                return JSONObject.parseArray(value,item.getClassType());
            }else{
                return JSONObject.parseObject(value,returnType);
            }
        }

        Object proceed = pjp.proceed(pjp.getArgs());
        if(Objects.nonNull(proceed)){
            redisTemplate.opsForValue().set(redisKey,JSONObject.toJSONString(proceed),expire, TimeUnit.MINUTES);
        }
        return proceed;
    }

    /**
     * 获取当前注解对象
     *
     * @param <T>
     * @param joinPoint
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     */
    public static <T extends Annotation> Item<T> getDeclaredAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) throws NoSuchMethodException {
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
        result.setReturnType(objMethod.getReturnType());

        if(Collection.class.isAssignableFrom(result.getReturnType())){
            Type genericReturnType = objMethod.getGenericReturnType();
            Type actualTypeArgument = null;
            if (genericReturnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                actualTypeArgument = actualTypeArguments[0];
            }
            result.setClassType((Class<?>) actualTypeArgument);
        }
        return result;
    }


    @Data
    static class Item<T>{
        private T annotation;
        private Class<?> returnType;
        private Class<?> classType;
    }


}
