package com.laboratory.paper.service;

import com.laboratory.paper.domain.user.GenerateCaptchaResponse;
import com.laboratory.paper.entity.User;
import com.laboratory.paper.vo.user.UserVo;

public interface UserService {

    GenerateCaptchaResponse generateCaptcha(int width);

    User login(UserVo user);
}
