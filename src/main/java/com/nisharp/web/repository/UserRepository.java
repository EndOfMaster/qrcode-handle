package com.nisharp.web.repository;

import com.nisharp.web.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZM.Wang
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
