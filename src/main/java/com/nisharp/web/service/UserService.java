package com.nisharp.web.service;

import cn.payingcloud.commons.rest.exception.BadRequestException;
import cn.payingcloud.commons.rest.exception.NotFoundException;
import com.nisharp.web.domain.SignUp;
import com.nisharp.web.domain.User;
import com.nisharp.web.infrastructure.util.DateUtils;
import com.nisharp.web.infrastructure.util.RandomUtils;
import com.nisharp.web.repository.SignUpRepository;
import com.nisharp.web.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ZM.Wang
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SignUpRepository signUpRepository;

    @Autowired
    public UserService(UserRepository userRepository, SignUpRepository signUpRepository) {
        this.userRepository = userRepository;
        this.signUpRepository = signUpRepository;
    }

    public SignUp get(String signUpId, boolean allowNull) {
        SignUp signUp = signUpRepository.findOne(signUpId);
        if (signUp == null && !allowNull) {
            throw new NotFoundException("signUpId不存在");
        }
        return signUp;
    }

    public String getCode(String email) {
        String code = RandomUtils.getRandomNum(6);
        SignUp signUp = new SignUp(email, code);
        signUpRepository.save(signUp);
        return code;
    }

    public void signUp(String email, String password, String code) {
        SignUp signUp = get(email, true);
        int hour = DateUtils.differenceByHour(signUp.getCteateAt(), new Date());
        if (hour > 1) {
            throw new BadRequestException("验证码超时");
        }
        if (!StringUtils.equals(code, signUp.getCode())) {
            throw new RuntimeException("验证码不正确");
        }
        User user = new User(email, password);
        userRepository.save(user);
    }

}
