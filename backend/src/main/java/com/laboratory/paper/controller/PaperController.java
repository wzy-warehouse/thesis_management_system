package com.laboratory.paper.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.domain.paper.PaperResponse;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.domain.paper.PaperResponseData;
import com.laboratory.paper.service.PaperService;
import com.laboratory.paper.vo.paper.PaperVo;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperController extends BaseController {

    @Resource
    private PaperService paperService;

    @PostMapping("/search-list")
    public ApiResponse<List<PaperListItem>> searchList(
            @RequestParam(value = "folderId") @Min(0) Long folderId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResponse.ok(paperService.searchPaperList(folderId, keyword));
    }

    @PostMapping("/query-base-info")
    public ApiResponse<PaperResponse> queryBaseInfo(@RequestBody @Valid QueryPaperBaseInfoVo queryPaperBaseInfo) {
        return ApiResponse.ok(paperService.queryBaseInfo(queryPaperBaseInfo));
    }

    @PostMapping("/upload")
    public ApiResponse<List<Long>> upload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folderId", required = false) Long folderId) {
        return ApiResponse.ok(List.of(1L, 2L, 3L));
    }

    @GetMapping("/{id}")
    public ApiResponse<PaperResponseData> queryById(@PathVariable @NotEmpty(message = "id不能为空") String id) {
        return ApiResponse.ok(new PaperResponseData());
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@RequestBody PaperVo paper) {
        return ApiResponse.ok("更新成功", null);
    }

    @DeleteMapping("/delete/{id}/{parentId}")
    public ApiResponse<Void> deleteById(@PathVariable String id, @PathVariable String parentId) {
        paperService.deletePaper(Long.parseLong(id), Long.parseLong(parentId), StpUtil.getLoginIdAsLong());
        return ApiResponse.ok("已移至回收站", null);
    }
}
