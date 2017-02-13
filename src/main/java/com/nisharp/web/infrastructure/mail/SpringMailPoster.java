package com.nisharp.web.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 基于Spring Mail的MailPoster实现
 *
 * @author YQ.Huang
 */
public class SpringMailPoster implements MailPoster {
    private static Logger logger = LoggerFactory.getLogger(SpringMailPoster.class);

    private final JavaMailSender mailSender;
    private final String mail;
    private final boolean enabled;

    public SpringMailPoster(JavaMailSender mailSender, String mail, boolean enabled) {
        this.mailSender = mailSender;
        this.mail = mail;
        this.enabled = enabled;
    }

    @Override
    public void sendSignUpMail(String email, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail);
        message.setTo(email);
        message.setSubject("逆锋注册验证码邮件");
        message.setText(content);
        if (enabled) {
            mailSender.send(message);
            logger.debug("用户注册邮件发送成功");
        } else {
            logger.info("用户注册通知:\n\nto={}\n内容={}\n\n", email, content);
        }
    }

    @Override
    public void sendForgetPasswordMail(String email, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail);
        message.setTo(email);
        message.setSubject("逆锋重置密码验证码邮件");
        message.setText(content);
        if (enabled) {
            mailSender.send(message);
            logger.debug("渠道代申请邮件发送成功");
        } else {
            logger.info("渠道代申请提交通知:\n\nto={}\n内容={}\n\n", email, content);
        }
    }
}
