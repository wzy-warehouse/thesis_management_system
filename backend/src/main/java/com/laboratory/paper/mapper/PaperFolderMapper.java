package com.laboratory.paper.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaperFolderMapper {
    /**
     * 按照论文id和目录id删除论文
     * @param paperId 论文id
     * @param folderId 目录id
     */
    void deleteByPaperIdFolderId(
            @Param("paperId") Long paperId,
            @Param("folderId") Long folderId);
}
