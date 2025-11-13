package com.laboratory.paper.controller;

import com.laboratory.paper.domain.paper.PaperResponse;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.vo.paper.PaperVo;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<PaperResponse> queryById(@PathVariable @NotEmpty(message = "id不能为空") String id) {
        return ApiResponse.ok(new PaperResponse());
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@RequestBody PaperVo paper) {
        return ApiResponse.ok("更新成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteById(@PathVariable String id) {
        return ApiResponse.ok("已移至回收站", null);
    }
}
