package com.nisharp.web.repository;

import com.nisharp.web.domain.AccessKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ZM.Wang
 */
public interface AccessKeyRepository extends MongoRepository<AccessKey, String> {

    List<AccessKey> findByAppId(String appId);

    int countByAppId(String appId);
}
