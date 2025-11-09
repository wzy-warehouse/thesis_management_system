package com.laboratory.paper.controller;

import com.laboratory.paper.vo.user.ChangePasswordVo;
import com.laboratory.paper.domain.user.CreateResponse;
import com.laboratory.paper.domain.user.LoginResponse;
import com.laboratory.paper.entity.ApiResponse;
import com.laboratory.paper.vo.user.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController extends BaseController{

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid UserVo user) {
        return ApiResponse.ok(new LoginResponse("dhsbghsdgsyduius", 1L, "admin"));
    }

    @PostMapping("/change-pwd")
    public ApiResponse<Void> changePwd(@RequestBody ChangePasswordVo changePassword) {
        return ApiResponse.ok();
    }

    @PostMapping("/create")
    public ApiResponse<CreateResponse> create(@RequestBody UserVo user) {
        return ApiResponse.ok(new CreateResponse(2L));
    }
}
