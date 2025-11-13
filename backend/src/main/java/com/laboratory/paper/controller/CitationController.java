package com.laboratory.paper.controller;

import com.laboratory.paper.domain.citation.GenerateCitationResponse;
import com.laboratory.paper.domain.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citation")
public class CitationController extends BaseController{

    @GetMapping("/generate/{paperId}")
    public ApiResponse<GenerateCitationResponse> generator(
            @PathVariable String paperId,
            @RequestParam(value = "formatId", required = false) Long formatId
    ) {
        return ApiResponse.ok(new GenerateCitationResponse());
    }
}
