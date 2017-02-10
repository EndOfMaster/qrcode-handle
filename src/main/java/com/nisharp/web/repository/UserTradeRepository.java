package com.nisharp.web.repository;

import com.nisharp.web.domain.UserTrade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ZM.Wang
 */
@Repository
public interface UserTradeRepository extends MongoRepository<UserTrade, String> {
}
