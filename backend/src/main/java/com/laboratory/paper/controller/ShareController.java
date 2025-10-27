package com.laboratory.paper.controller;

import com.laboratory.paper.domain.Share;
import com.laboratory.paper.entity.ApiResponse;
import com.laboratory.paper.vo.ShareVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/share")
public class ShareController extends BaseController{

    @PostMapping("/create")
    public ApiResponse<Share> create(ShareVo share) {
        return ApiResponse.ok(new Share());
    }
}
