package com.laboratory.paper.mapper;

import com.laboratory.paper.domain.folder.QueryFolderResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FolderMapper {

    List<QueryFolderResponse> getFoldersByParentId(Long parentId);
}
