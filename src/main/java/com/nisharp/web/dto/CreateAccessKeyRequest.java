package com.nisharp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author YQ.Huang
 */
public class CreateAccessKeyRequest {

    @ApiModelProperty("应用id")
    @NotBlank
    private String userId;

    @ApiModelProperty("accessSecret")
    @Length(min = 1, max = 32)
    private String secret;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private boolean enabled;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
