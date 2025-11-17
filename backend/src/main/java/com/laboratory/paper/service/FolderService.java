package com.laboratory.paper.service;

import com.laboratory.paper.domain.folder.CreateFolderResponse;
import com.laboratory.paper.domain.folder.QueryFolderResponse;

import java.util.List;

public interface FolderService {
    List<QueryFolderResponse> queryFolder(Long parentId);

    CreateFolderResponse createFolder(String name, Long parentId, Long createUserId);
}
