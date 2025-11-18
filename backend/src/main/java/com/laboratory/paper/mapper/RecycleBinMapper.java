package com.laboratory.paper.mapper;

import com.laboratory.paper.entity.RecycleBin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecycleBinMapper {
    List<RecycleBin> queryRecycleBinByParentId(Long parentId);

    void deleteByFolderId(Long folderId);
}
