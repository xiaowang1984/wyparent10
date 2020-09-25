package test;

import com.neuedu.App;
import com.neuedu.mail.MailController;
import com.neuedu.pojo.MailVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MailTest {
    @Resource
    MailController controller;
    @Test
    public void handler() throws UnsupportedEncodingException, MessagingException {

    }
}
