package com.nisharp.web.domain;

import java.util.Date;

/**
 * @author ZM.Wang
 */
public class User {
    private final String id;
    private final Date createdAt;
    private final String password;
    private final int money = 0;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getPassword() {
        return password;
    }

    public int getMoney() {
        return money;
    }
}
