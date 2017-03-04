package com.games.job.server.sender;

import com.games.job.common.model.TaskModel;
import com.games.job.server.async.AsyncServiceImpl;
import com.games.job.server.configure.EmailConfig;
import com.games.job.server.entity.TaskEmail;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author:liujh
 * @create_time:2017/3/4 15:16
 * @project:task-scheduler
 * @full_name:com.games.job.server.sender.EmailSender
 * @ide:IntelliJ IDEA
 */
@Component
public class EmailSenderImpl implements EmailSender {

    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AsyncServiceImpl asyncService;

    @Override
    public void sendTaskFail(List<TaskEmail> emails, String title, String content) {
        if(emails!=null&&!emails.isEmpty()){
            asyncService.executeTask(()->{
                for(TaskEmail email:emails){
                    sendSimpleMail(email.getEmailAddress(),title,content+"");
                }
            });
        }
    }

    @Override
    public void sendSimpleMail(String sendTo, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailFrom());
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String sendTo, String title, String content, List<Pair<String, File>> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(title);
            helper.setText(content);

            for (Pair<String, File> pair : attachments) {
                helper.addAttachment(pair.getLeft(), new FileSystemResource(pair.getRight()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<Pair<String, File>> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(title);

            String text = "";//VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template.vm", "UTF-8", content);
            helper.setText(text, true);

            for (Pair<String, File> pair : attachments) {
                helper.addAttachment(pair.getLeft(), new FileSystemResource(pair.getRight()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }
}
