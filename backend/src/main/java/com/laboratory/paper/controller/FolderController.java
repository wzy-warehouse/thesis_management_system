package com.laboratory.paper.controller;

import com.laboratory.paper.domain.folder.CreateFolderResponse;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.service.FolderService;
import jakarta.annotation.Resource;
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
    public ApiResponse<CreateFolderResponse> create(@RequestParam String name, @RequestParam Long parentId) {
        return ApiResponse.ok(new CreateFolderResponse());
    }

    @PostMapping("/add-paper")
    public ApiResponse<Void> addPaper(
            @RequestParam Long folderId,
            @RequestParam List<Long> paperIds) {
        return ApiResponse.ok("添加成功", null);
    }


}
