package com.laboratory.paper.service;

import com.laboratory.paper.domain.user.LoginResponse;
import com.laboratory.paper.entity.User;
import com.laboratory.paper.vo.user.UserVo;

public interface UserService {

    User login(UserVo user);
}
