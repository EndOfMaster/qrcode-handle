package com.nisharp.web.repository;

import com.nisharp.web.domain.SignUp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZM.Wang
 */
@Repository
public interface SignUpRepository extends MongoRepository<SignUp, String> {
}
