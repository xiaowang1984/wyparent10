package com.neuedu.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.pojo.MailVo;
import com.neuedu.util.ResultData;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
public class MailController {
    @Resource
    JavaMailSender javaMailSender;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    RabbitTemplate rabbitTemplate;
    @RabbitListener(queues = "mailqueue")
    public void send(Message message)  {
        try {
            String body = new String(message.getBody(),"UTF-8");
            MailVo mailVo = null;
            mailVo = objectMapper.readValue(body, MailVo.class);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mailVo.getFrom(),mailVo.getPersonal());
            helper.setTo(mailVo.getTo());
            helper.setSubject(mailVo.getSubject());
            helper.setText(mailVo.getContent(),true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(message);
        }
    }
}
