package com.nisharp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author YQ.Huang
 */
public class UpdateAccessKeyRequest {

    @ApiModelProperty("accessSecret")
    @Length(min = 1, max = 32)
    private String secret;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private boolean enabled;

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
