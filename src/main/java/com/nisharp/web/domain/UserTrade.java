package com.nisharp.web.domain;

import java.util.Date;

/**
 * @author ZM.Wang
 */
public class UserTrade {

    private final String id;
    private final String userId;
    private final int money;
    private Date startedAt;
    private Date completedAt;
    private Status status;

    public enum Status {
        CREATED,
        PENDING,
        SUCCEEDED,
        FAILED;
    }

    public UserTrade(String userId, int money) {
        this.id = null;
        this.userId = userId;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getMoney() {
        return money;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public UserTrade setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public UserTrade setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public UserTrade setStatus(Status status) {
        this.status = status;
        return this;
    }
}


