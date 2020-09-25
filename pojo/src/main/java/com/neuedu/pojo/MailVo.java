package com.neuedu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailVo {
    private String to;
    private String from;
    private String personal;
    private String subject;
    private String content;
}
