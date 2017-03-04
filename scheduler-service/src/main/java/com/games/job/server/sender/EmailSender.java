package com.games.job.server.sender;


import com.games.job.server.entity.TaskEmail;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author:liujh
 * @create_time:2017/3/4 15:15
 * @project:task-scheduler
 * @full_name:com.games.job.server.sender.Sender
 * @ide:IntelliJ IDEA
 */
public interface EmailSender {

    void sendTaskFail(List<TaskEmail> emails, String title, String content);

    /**
     * 发送简单邮件
     * @param sendTo 收件人地址
     * @param title  邮件标题
     * @param content 邮件内容
     */
    void sendSimpleMail(String sendTo, String title, String content);

    /**
     * 发送带附件简单邮件
     * @param sendTo 收件人地址
     * @param title  邮件标题
     * @param content 邮件内容
     * @param attachments<文件名，附件> 附件列表
     */
    void sendAttachmentsMail(String sendTo, String title, String content, List<Pair<String, File>> attachments);

    /**
     * 发送模板邮件
     * @param sendTo 收件人地址
     * @param title  邮件标题
     * @param content<key, 内容> 邮件内容
     * @param attachments<文件名，附件> 附件列表
     */
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<Pair<String, File>> attachments);
}
