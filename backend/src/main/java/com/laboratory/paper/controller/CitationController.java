package com.laboratory.paper.controller;

import com.laboratory.paper.entity.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class CitationController extends BaseController{

    @GetMapping("/generator/{paperId}")
    public ApiResponse<String> generator(
            @PathVariable String paperId,
            @RequestParam(value = "format", required = false) String format
    ) {
        return ApiResponse.ok("");
    }
}
