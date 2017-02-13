package com.nisharp.web.repository;

import com.nisharp.web.domain.AccessKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZM.Wang
 */
@Repository
public interface ResetPasswdRepository extends MongoRepository<AccessKey, String> {
}
