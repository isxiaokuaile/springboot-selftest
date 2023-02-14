package org.example.common.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 接口监控
 */
@Aspect
@Component
public class logAspect {

    /**
     * 包路径的固定写法（范匹配）
     * "execution(public * org.example.controller.*..*(..))"
     * *是指这个controller包下的所有类
     * *..是指这个controller包下类中所有的方法
     * *(..)是指这个controller包下类中所有的入参
     */
    @Pointcut("execution(public * org.example.controller.*..*(..))")
    public void methodPointCut() {

    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("methodPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

//        redisTemplate.opsForValue().get()
        Signature signature = pjp.getSignature();

        Long startTime = null; // 开始时间
        Long endTime = null;    // 结束时间
        //被代理的类的类名
        String className = pjp.getTarget().getClass().getName();

        //方法名
        String methodName = signature.getName();

        //获取日志
        Logger logger = LoggerFactory.getLogger(className);

        //参数数组
        Object[] requestParams = pjp.getArgs();


        StringBuffer sb = new StringBuffer();
        for (Object requestParam : requestParams) {
            if (requestParam != null) {
                sb.append(JSON.toJSONString(requestParam));
                sb.append(",");
            }
        }

        String requestParamsString = sb.toString();
        // 去除最后一个逗号
        if (requestParamsString.length() > 0) {
            requestParamsString = requestParamsString.substring(0, requestParamsString.length() - 1);
        }

        //接口调用开始响应起始时间
        startTime = System.currentTimeMillis();

        //正常执行方法
        /**
         * 核心代码，切点具体执行的方法是在这一行执行的、
         * 这里是用的反射
         */
        Object response = pjp.proceed();

        //接口调用结束时间
        endTime = System.currentTimeMillis();

        //接口耗时
        logger.info(String.format("接口[%s] 耗时：[%s] ms", methodName, (endTime - startTime)));

        return response;
    }

}
