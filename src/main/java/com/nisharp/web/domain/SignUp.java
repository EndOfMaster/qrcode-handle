package com.nisharp.web.domain;

import java.util.Date;

/**
 * @author ZM.Wang
 */
public class SignUp {
    private final String id;
    private Date cteateAt;
    private String code;

    public SignUp(String id, String code) {
        this.id = id;
        this.code = code;
        this.cteateAt = new Date();
    }

    public void reset(String code) {
        this.code = code;
        this.cteateAt = new Date();
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
