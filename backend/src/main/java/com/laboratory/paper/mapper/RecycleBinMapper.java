package com.laboratory.paper.mapper;

import com.laboratory.paper.entity.RecycleBin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecycleBinMapper {
    List<RecycleBin> queryRecycleBinByParentId(Long parentId);

    List<RecycleBin> queryRecycleBinByIdFolderId(
            @Param("paperId") Long paperId,
            @Param("parentId") Long parentId);

    void deleteByFolderId(Long folderId);

    /**
     * 移动到回收站
     * @param paperId 论文id
     * @param parentId 父目录id
     * @param userId 用户id
     */
    void removeToRecycleBin(
            @Param("paperId") Long paperId,
            @Param("parentId") Long parentId,
            @Param("userId") Long userId);

    /**
     * 按照文件id和目录id删除回收站中文件
     * @param paperId 文件id
     * @param folderId 目录id
     */
    void deleteRecycleBinByPaperIdFolderId(
            @Param("paperId") Long paperId,
            @Param("folderId") Long folderId);

    /**
     * 判断回收站中是否存在论文id和文件夹id对应的数据
     * @param paperIds 论文id集合
     * @param parentId 文件夹id
     * @return 查询数据
     */
    List<RecycleBin> queryAllRecycleBinByIdFolderId(
            @Param("paperIds") List<Long> paperIds,
            @Param("parentId") Long parentId);

    /**
     * 批量移动到回收站
     * @param paperIds 论文id
     * @param parentId 父目录id
     * @param userId 用户id
     */
    void batchRemoveToRecycleBin(
            @Param("paperIds") List<Long> paperIds,
            @Param("parentId") Long parentId,
            @Param("userId") Long userId);
}
