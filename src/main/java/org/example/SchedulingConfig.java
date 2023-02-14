//package org.example;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
///**
// * 定时任务装配
// */
//
//@Slf4j   //日志
//@Configuration   //用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被
//public class SchedulingConfig {
//    @Autowired
//    private ITestService iTestService;
//    @Autowired
//    private RedisTemplate<String ,String> redisTemplate;
//
//    //@Scheduled(cron = "0 0 0 * * * ?")  每天凌晨执行一次（0:0:0）
//    //@Scheduled(cron = "0 */10 * * * ?") 每10秒执行一次
////    @Scheduled(cron = "*/1 * * * * ?")  //每5秒执行一次,设置定时任务
////    public void getToken(){
////        try {
////            log.info("\n" +"定时任务开始了........");
////            redisTemplate.opsForValue().set("tempK",System.currentTimeMillis() +"");
////            redisTemplate.opsForHash().put("hashk","tempK",System.currentTimeMillis() +"");
////        } catch (Exception e) {
////            log.error("定时任务出错",e);
////        }
////    }
//
//
//
//}
