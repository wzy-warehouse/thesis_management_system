package com.laboratory.paper.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.laboratory.paper.domain.folder.CreateFolderResponse;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.service.FolderService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderController extends BaseController {

    @Resource
    private FolderService folderService;

    @PostMapping("/query-folder")
    public ApiResponse<List<QueryFolderResponse>> queryFolder(@RequestParam(value = "parentId", required = true) Long parentId) {
        return ApiResponse.ok(folderService.queryFolder(parentId));
    }

    @PostMapping("/create")
    @Validated
    public ApiResponse<CreateFolderResponse> create(
            @RequestParam(value = "name")
            @NotEmpty(message = "目录名称不能为空")
            @Size(max = 100, message = "目录名称不能超过100个字符")
            String name,
            @RequestParam(value = "parentId")
            @NotNull(message = "父级目录不能为空")
            Long parentId) {
        return ApiResponse.ok(folderService.createFolder(name, parentId, StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/add-paper")
    public ApiResponse<Void> addPaper(
            @RequestParam Long folderId,
            @RequestParam List<Long> paperIds) {
        return ApiResponse.ok("添加成功", null);
    }


}
