package com.nisharp.web.infrastructure.mail;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author YQ.Huang
 */
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private boolean enabled = true;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public boolean isEnabled() {
        return enabled;
    }

    public MailProperties setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MailProperties setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailProperties setPassword(String password) {
        this.password = password;
        return this;
    }
}
