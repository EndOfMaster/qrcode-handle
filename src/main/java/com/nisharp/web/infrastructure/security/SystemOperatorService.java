package com.nisharp.web.infrastructure.security;

import cn.payingcloud.commons.rest.exception.NotFoundException;
import com.nisharp.web.domain.User;
import com.nisharp.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class SystemOperatorService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SystemOperatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findOne(username);
            if (user == null) {
                throw new NotFoundException("用户不存在");
            }
            return new SystemUser(user);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage());
        }
    }

}
