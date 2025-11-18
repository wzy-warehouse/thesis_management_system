package com.laboratory.paper.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.service.RecycleBinService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recycle-bin")
public class RecycleBinController extends Serializers.Base {

    @Resource
    private RecycleBinService recycleBinService;

    @DeleteMapping("/delete-by-folder-id/{folderId}")
    public ApiResponse<Void> deleteByFolderId(@PathVariable String folderId) {
        recycleBinService.deleteByFolderId(Long.parseLong(folderId));
        return ApiResponse.ok();
    }
}
