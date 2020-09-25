package com.neuedu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.pojo.MailVo;
import com.neuedu.pojo.UmsAdmin;
import com.neuedu.util.JwtUtil;
import com.neuedu.util.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class MyAop {
    /*@Before("execution(* com.neuedu.controller.*.*(..))")
    public void before() {
        System.out.println("方法前");
    }*/
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    ObjectMapper objectMapper;
    @AfterReturning(value = "execution(* com.neuedu.controller.*.*(..))",returning = "result")
    public ResultData after(ResultData result) {
        // 拿出原先的token
        // 想拿原先的token 必须有 HttpServletRequest对象
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)) {
            return result;
        }
        try {
            // 解析token 拿到 用户id 和密码
            JwtUtil jwtUtil = JwtUtil.deToken(token);
            // 生成新token
            String newtoken = JwtUtil.createTokenBySession(jwtUtil.getUserid(),jwtUtil.getPwd());
            result.setToken(newtoken);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    @AfterReturning(value = "execution(* com.neuedu.service.impl.UmsAdminServiceImpl.add(..))",returning = "result")
    public void afteradd(JoinPoint joinPoint,boolean result){
        try {
            Object[] args = joinPoint.getArgs();
            System.out.println(args.length);
            UmsAdmin umsAdmin = (UmsAdmin) args[0];
            if(result) {
                MailVo mailVo = new MailVo(umsAdmin.getMail()
                        , "14481278@qq.com"
                        , "系统消息"
                        , "创建登录用户"
                        , "您好,您的登录用户已经创建,密码为:" + umsAdmin.getPassword());
                rabbitTemplate.convertAndSend("mailqueue",objectMapper.writeValueAsString(mailVo));
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   /* @Around(value = "execution(* com.neuedu.controller.*.*(..))")
    public ResultData around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("方法前");
        ResultData resultData = (ResultData) joinPoint.proceed();
        resultData.setToken("fdjkslajfkldsjafkldsjla");
        System.out.println("方法后");
        return resultData;
    }*/
}
