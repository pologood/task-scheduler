package com.games.job.server.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author:liujh
 * @create_time:2017/3/4 15:44
 * @project:task-scheduler
 * @full_name:com.games.job.server.configure.EmailConfig
 * @ide:IntelliJ IDEA
 */
@Component
public class EmailConfig {
    @Value("${spring.mail.username}")
    private String emailFrom;

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
}
