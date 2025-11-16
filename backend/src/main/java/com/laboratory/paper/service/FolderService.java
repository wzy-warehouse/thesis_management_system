package com.laboratory.paper.service;

import com.laboratory.paper.domain.folder.QueryFolderResponse;

import java.util.List;

public interface FolderService {
    List<QueryFolderResponse> queryFolder(Long parentId);
}
