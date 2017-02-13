package com.nisharp.web.infrastructure.mail;

public interface MailPoster {

    void sendSignUpMail(String email, String content);

    void sendForgetPasswordMail(String email, String content);
}
