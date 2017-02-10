package com.nisharp.web.domain;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

/**
 * Created by ZM.Wang
 */
public class AccessKey {

    private final String id;
    private final String appId;
    private final Date createdAt;
    private boolean enabled;
    private String secret;
    private String remark;

    public AccessKey(String id, String appId, String secret, String remark, boolean enabled) {
        this.id = id;
        this.appId = appId;
        this.secret = secret;
        this.remark = remark;
        this.createdAt = new Date();
        this.enabled = enabled;
    }

    @PersistenceConstructor
    public AccessKey(String appId, String secret, String remark, boolean enabled) {
        this(null, appId, secret, remark, enabled);
    }

    public AccessKey(String appId) {
        this.id = null;
        this.appId = appId;
        this.createdAt = new Date();
        this.enabled = true;
    }

    public String getId() {
        return id;
    }

    public String getAppId() {
        return appId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getSecret() {
        return secret;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
