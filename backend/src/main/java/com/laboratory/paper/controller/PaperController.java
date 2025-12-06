package com.laboratory.paper.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.laboratory.paper.domain.paper.*;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.service.PaperService;
import com.laboratory.paper.vo.paper.PaperVo;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> upload(PaperUploadRequest paperUploadRequest,
                                    @RequestPart("file") MultipartFile file) {
        paperService.upload(paperUploadRequest, file, StpUtil.getLoginIdAsLong());
        return ApiResponse.ok("上传成功", null);
    }

    @PostMapping("/chat")
    public Mono<ApiResponse<String>> chat(@RequestParam(value = "paperId") Long paperId) {
        return paperService.chat(paperId)
                // 成功：返回200 + success + 大模型响应内容
                .flatMap(reply -> Mono.just(ApiResponse.ok(reply)))
                // 异常/失败：返回500 + 错误信息 + 空数据
                .onErrorReturn(ApiResponse.error(500, "大模型调用失败", null));
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

    @DeleteMapping("/batch-delete")
    public ApiResponse<Void> batchDelete(@RequestBody PaperBatchDeleteRequest paperBatchDeleteRequest) {
        paperService.batchDeletePaper(paperBatchDeleteRequest.getPaperIds(), paperBatchDeleteRequest.getFolderId(), StpUtil.getLoginIdAsLong());
        return ApiResponse.ok("已移至回收站", null);
    }
}
