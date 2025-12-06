package com.laboratory.paper.mapper;

import com.laboratory.paper.entity.PaperFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询对应关系是否已经存在
     * @param paperFolder 参数
     * @return 数量
     */
    Integer queryPaperFolder(PaperFolder paperFolder);

    /**
     * 写入关联
     * @param paperFolder 参数
     */
    void insertPaperFolder(PaperFolder paperFolder);

    /**
     * 批量按照id和目录id删除论文
     * @param paperIds id
     * @param folderId 目录id
     */
    void batchDeleteByPaperIdFolderId(
            @Param("paperIds") List<Long> paperIds,
            @Param("folderId") Long folderId);
}
