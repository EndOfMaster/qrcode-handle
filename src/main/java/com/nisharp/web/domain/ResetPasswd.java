package com.nisharp.web.domain;

import java.util.Date;

/**
 * @author ZM.Wang
 */
public class ResetPasswd {
    private final String id;
    private Date cteateAt;
    private String code;


    public ResetPasswd(String id, String code) {
        this.id = id;
        this.cteateAt = new Date();
        this.code = code;
    }

    public ResetPasswd reset(String code) {
        this.code = code;
        this.cteateAt = new Date();
        return this;
    }

    public String getId() {
        return id;
    }

    public Date getCteateAt() {
        return cteateAt;
    }

    public String getCode() {
        return code;
    }
}
