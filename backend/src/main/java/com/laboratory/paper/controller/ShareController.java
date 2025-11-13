package com.laboratory.paper.controller;

import com.laboratory.paper.domain.share.CreateShareResponse;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.vo.share.CreateShareVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/share")
public class ShareController extends BaseController{

    @PostMapping("/create")
    public ApiResponse<CreateShareResponse> create(@RequestBody CreateShareVo share) {
        return ApiResponse.ok(new CreateShareResponse());
    }
}
