package com.laboratory.paper.controller;

import com.laboratory.paper.entity.ApiResponse;
import com.laboratory.paper.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController extends BaseController{

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid UserVo user) {
        return ApiResponse.ok("token:3223323232");
    }

    @PostMapping("/change-pwd")
    public ApiResponse<Void> changePwd(String oldPwd, String newPwd) {
        return ApiResponse.ok();
    }

    @PostMapping("/create")
    public ApiResponse<?> create(UserVo user) {
        return ApiResponse.ok("id:2");
    }
}
