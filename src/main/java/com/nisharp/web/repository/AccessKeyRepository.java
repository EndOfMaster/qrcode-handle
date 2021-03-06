package com.nisharp.web.repository;

import com.nisharp.web.domain.AccessKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZM.Wang
 */
@Repository
public interface AccessKeyRepository extends MongoRepository<AccessKey, String> {

    List<AccessKey> findByUserId(String userId);

    int countByUserId(String userId);
}
