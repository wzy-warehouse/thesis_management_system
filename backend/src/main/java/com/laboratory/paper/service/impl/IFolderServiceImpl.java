package com.laboratory.paper.service.impl;

import com.laboratory.paper.domain.folder.CreateFolderResponse;
import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.entity.Folder;
import com.laboratory.paper.entity.RecycleBin;
import com.laboratory.paper.mapper.FolderMapper;
import com.laboratory.paper.mapper.PaperMapper;
import com.laboratory.paper.mapper.RecycleBinMapper;
import com.laboratory.paper.service.FolderService;
import com.laboratory.paper.service.ex.FilesInRecycleBinException;
import com.laboratory.paper.service.ex.IncludingFilesException;
import com.laboratory.paper.service.ex.IncludingSubdirectoriesException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IFolderServiceImpl implements FolderService {

    @Resource
    private FolderMapper folderMapper;

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private RecycleBinMapper recycleBinMapper;

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

    @Override
    public void deleteFolder(Long id) {
        // 判断子目录存不存在
        List<QueryFolderResponse> folderList = folderMapper.getFoldersByParentId(id);

        if(folderList != null && !folderList.isEmpty()) {
            throw new IncludingSubdirectoriesException();
        }

        // 判断该目录下是否存在文件
        List<PaperListItem> paperList = paperMapper.searchPaperByFolder(id, null);
        if(paperList != null && !paperList.isEmpty()) {
            throw new IncludingFilesException();
        }


        // 判断回收站中是否存在关联文件
        List<RecycleBin> recycleBinsList = recycleBinMapper.queryRecycleBinByParentId(id);
        if(recycleBinsList != null && !recycleBinsList.isEmpty()) {
            throw new FilesInRecycleBinException();
        }

        // 删除文件夹
        folderMapper.deleteFolder(id);
    }
}
