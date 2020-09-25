package com.neuedu.service;

import com.neuedu.pojo.MailVo;
import com.neuedu.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mail")
public interface MailService {
    @PostMapping("/mail/send")
    ResultData send(MailVo mailVo);
}
