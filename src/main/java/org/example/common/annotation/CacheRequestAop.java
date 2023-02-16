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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect  //切面编程
@Component
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
    //ProceedingJoinPoint  这个handler方法是around方法在调用，如果不写没发获取到上下文，aop会自动写入参
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {

        // 获取注解对象,反射
        //调用这个方法方法能拿到item，通过item对象可以得到注解和返回体类型，能拿到注解的key
        Item<CacheRequest> item = getDeclaredAnnotation(pjp,CacheRequest.class);
        CacheRequest annotation = item.getAnnotation();
        Class<?> returnType = item.getReturnType();

        long expire = annotation.expire();
        //这里的final是要求这个redisKey不能更改了
        final String redisKey = annotation.key();

        String value = redisTemplate.opsForValue().get(redisKey);

        if (StringUtils.isNotBlank(value)){
            //被标记的方法返回值可能是多条/单条，如果是单条就能直接获取到返回体类型，如果是多条返回值就会是一个集合，但是集合的泛型并不知道得获取
            //Collection.class.isAssignableFrom(returnType)   这个条件是判断返回体是不是一个collection的子集，map不是子集不能用
            // Objects.nonNull(item.getClassType()  这个条件是判断集合的泛型是不是空
            //如果都为真，返回json序列化的集合
            //这里是两个分支，if是集合的判断，else是除了集合也就是一个对象，集合走if，对象走else
            if(Collection.class.isAssignableFrom(returnType) && Objects.nonNull(item.getClassType())){
                return JSONObject.parseArray(value,item.getClassType());
            }else{
                return JSONObject.parseObject(value,returnType);
            }
        }

        //执行具体的方法（pjp.getArgs()是把返回值传进来）
        Object proceed = pjp.proceed(pjp.getArgs());
        if(Objects.nonNull(proceed)){
            redisTemplate.opsForValue().set(redisKey,JSONObject.toJSONString(proceed),expire, TimeUnit.MINUTES);
        }
        return proceed;
    }

    /**
     * 获取入参和出参的类型，以及注解详情
     *
     * @param <T>
     * @param joinPoint
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * 根据方法对象得到这个方法的所有属性（这就是反射）
     */
    public static <T extends Annotation> Item<T> getDeclaredAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的入参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 以上所有的参数都是为了下面这一行
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);

        // 拿到方法定义的注解信息,这里需要的注解是自定义注解，所以入参是这个
        T annotation = objMethod.getDeclaredAnnotation(clazz);
        Item<T> result = new Item<>();
        result.setAnnotation(annotation);
        result.setReturnType(objMethod.getReturnType());

        //查返回体的泛型类型
        if(Collection.class.isAssignableFrom(result.getReturnType())){
            Type genericReturnType = objMethod.getGenericReturnType();
            //实际的类型（返回值的泛型）
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
