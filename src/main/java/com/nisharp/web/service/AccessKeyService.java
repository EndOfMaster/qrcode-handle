package com.nisharp.web.service;

import cn.payingcloud.commons.rest.exception.BadRequestException;
import cn.payingcloud.commons.rest.exception.NotFoundException;
import com.nisharp.web.domain.AccessKey;
import com.nisharp.web.dto.CreateAccessKeyRequest;
import com.nisharp.web.dto.UpdateAccessKeyRequest;
import com.nisharp.web.infrastructure.util.RandomUtils;
import com.nisharp.web.repository.AccessKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YQ.Huang
 */
@Service
public class AccessKeyService {

    private final AccessKeyRepository accessKeyRepository;

    @Autowired
    public AccessKeyService(AccessKeyRepository accessKeyRepository) {
        this.accessKeyRepository = accessKeyRepository;
    }

    public AccessKey create(CreateAccessKeyRequest request) {
        if (accessKeyRepository.countByAppId(request.getAppId()) >= 10) {
            throw new BadRequestException("您可以免费创建5个AccessKey。如需更多，请直接联系我们。");
        }
        AccessKey accessKey = new AccessKey(request.getAppId(), request.getSecret(), request.getRemark(), request.isEnabled());
        return accessKeyRepository.insert(accessKey);
    }

    public void update(String accessKeyId, UpdateAccessKeyRequest request) {
        AccessKey accessKey = get(accessKeyId);
        accessKey.setSecret(request.getSecret());
        accessKey.setRemark(request.getRemark());
        accessKey.setEnabled(request.isEnabled());
        accessKeyRepository.save(accessKey);
    }

    public void delete(String accessKeyId) {
        accessKeyRepository.delete(accessKeyId);
    }

    public AccessKey get(String accessKeyId) {
        AccessKey accessKey = accessKeyRepository.findOne(accessKeyId);
        if (accessKey == null) {
            throw new NotFoundException("AccessKey不存在");
        }
        return accessKey;
    }

    public List<AccessKey> list(String appId) {
        return accessKeyRepository.findByAppId(appId);
    }

    public String getRandomStr() {
        return RandomUtils.getRandomStr(32);
    }

}
