package com.laboratory.paper.service.impl;

import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.mapper.FolderMapper;
import com.laboratory.paper.service.FolderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IFolderServiceImpl implements FolderService {

    @Resource
    private FolderMapper folderMapper;

    @Override
    public List<QueryFolderResponse> queryFolder(Long parentId) {
        return folderMapper.getFoldersByParentId(parentId);
    }
}
