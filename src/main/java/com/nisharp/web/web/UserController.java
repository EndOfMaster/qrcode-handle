package com.nisharp.web.web;

import com.nisharp.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZM.Wang
 */
@Api("用户")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("注册，输入邮箱")
    @PostMapping("/email")
    public void register(String email) {
        userService.getCode(email);
    }
}
