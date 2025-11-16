package com.laboratory.paper.mapper;

import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.entity.Paper;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperMapper {

    /**
     * 按文件夹搜索文件
     * @param folderId 文件夹id
     * @param keyword 关键字
     * @return 列表
     */
    List<PaperListItem> searchPaperByFolder(@Param("folderId") Long folderId, @Param("keyword") String keyword);

    /**
     * 搜索所有文件
     * @param keyword 关键字
     * @return 列表
     */
    List<PaperListItem> searchAllPapers(String keyword);

    /**
     * 查询所有论文基本信息
     * @param queryPaperBaseInfo 前端传递的信息
     * @return 论文信息列表
     */
    List<Paper> queryAllPapersBaseInfo(QueryPaperBaseInfoVo queryPaperBaseInfo);

    /**
     * 查询所有论文基本信息对应总页数
     * @param queryPaperBaseInfo 前端传递的信息
     * @return 总页数
     */
    Integer queryAllPapersBaseInfoPages(QueryPaperBaseInfoVo queryPaperBaseInfo);

    /**
     * 根据id查询论文基本信息
     * @param queryPaperBaseInfo 前端传递的信息
     * @return 论文列表
     */
    List<Paper> queryPapersById(QueryPaperBaseInfoVo queryPaperBaseInfo);
}
