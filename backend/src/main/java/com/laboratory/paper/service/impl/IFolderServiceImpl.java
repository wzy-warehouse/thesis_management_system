package com.laboratory.paper.service.impl;

import com.laboratory.paper.domain.folder.CreateFolderResponse;
import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.entity.Folder;
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

    @Override
    public CreateFolderResponse createFolder(String name, Long parentId, Long createUserId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setParentId(parentId);
        folder.setCreateUser(createUserId);

        // 创建文件夹
        folderMapper.createNewFolder(folder);

        return new CreateFolderResponse(folder.getId());
    }

    @Override
    public void renameFolder(String name, Long id) {
        folderMapper.renameFolder(name, id);
    }
}
