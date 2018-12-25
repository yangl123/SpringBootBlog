package com.yangle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by yangle on 2017/9/27.
 */
//@Component
public class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;

    public void sendEmail(String to,String subject,String content){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发送邮件失败");
        }

    }

    public void sendHtmlEmail(String to,String subject,String content){

        MimeMessage message = mailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            logger.info("html邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
        }

    }

    public void sendAttachEmail(String to,String subject,String content,String path){
    MimeMessage mimeMessage=mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            FileSystemResource fileSystemResource=new FileSystemResource(new File(path));
            String fname=path.substring(path.lastIndexOf(File.separator));
            helper.addAttachment(fname,fileSystemResource);
            mailSender.send(mimeMessage);
            logger.info("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
//            logger.error("邮件发送失败");
        }

    }
}
