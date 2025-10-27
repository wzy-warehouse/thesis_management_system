package com.laboratory.paper.controller;

import com.laboratory.paper.domain.Paper;
import com.laboratory.paper.entity.ApiResponse;
import com.laboratory.paper.vo.PaperVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperController extends BaseController {

    @PostMapping("/upload")
    public ApiResponse<List<Long>> upload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folderId", required = false) Long folderId) {
        return ApiResponse.ok(List.of(1L, 2L, 3L));
    }

    @GetMapping("/{id}")
    public ApiResponse<Paper> queryById(@PathVariable String id) {
        return ApiResponse.ok(new Paper());
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(PaperVo paper) {
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        return ApiResponse.ok();
    }
}
