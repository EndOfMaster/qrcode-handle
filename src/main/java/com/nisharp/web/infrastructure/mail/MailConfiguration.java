package com.nisharp.web.infrastructure.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author YQ.Huang
 */
@Configuration
public class MailConfiguration {

    @Bean
    MailProperties mailProperties() {
        return new MailProperties();
    }

    @Bean
    MailPoster mailPoster(JavaMailSender javaMailSender) {
        return new SpringMailPoster(javaMailSender, mailProperties().getUsername(), mailProperties().isEnabled());
    }
}
