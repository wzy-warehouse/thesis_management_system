package com.laboratory.paper.service.impl;

import com.laboratory.paper.domain.user.LoginResponse;
import com.laboratory.paper.entity.User;
import com.laboratory.paper.mapper.UserMapper;
import com.laboratory.paper.service.UserService;
import com.laboratory.paper.service.ex.IncorrectPasswordException;
import com.laboratory.paper.service.ex.UserNotExistException;
import com.laboratory.paper.utils.user.PasswordUtils;
import com.laboratory.paper.vo.user.UserVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(UserVo user) {
        // 根据用户名获取用户信息
        User userInfo = userMapper.findByUsername(user.getUsername());

        // 判断用户是否存在
        if (userInfo == null || userInfo.getUsername() == null) {
            throw new UserNotExistException();
        }

        boolean verify = PasswordUtils.verifyPassword(user.getPassword(), userInfo.getSalt(), userInfo.getPassword());

        if (!verify) {
            throw new IncorrectPasswordException();
        }

        return userInfo;
    }
}
