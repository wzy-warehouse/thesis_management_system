package com.laboratory.paper.mapper;

import com.laboratory.paper.domain.folder.QueryFolderResponse;
import com.laboratory.paper.entity.Folder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FolderMapper {

    List<QueryFolderResponse> getFoldersByParentId(Long parentId);

    Long createNewFolder(Folder folder);
}
