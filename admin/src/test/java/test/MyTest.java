package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.App;
import com.neuedu.pojo.MailVo;
import com.neuedu.util.JwtUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MyTest {
    public static void main(String[] args) {
        System.out.println(RandomStringUtils.random(8,true,true));
    }
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    ObjectMapper objectMapper;
    @Test
    public void handler() throws JsonProcessingException, UnsupportedEncodingException {
        MailVo mailVo = new MailVo("wang-yu-wang@neusoft.com"
                , "14481278@qq.com"
                , "系统消息"
                , "创建登录用户"
                , "您好,您的登录用户已经创建,密码为:" + "11111111");
        rabbitTemplate.convertAndSend("mailqueue",new String(objectMapper.writeValueAsString(mailVo)));
    }
}
