package com.laboratory.paper.service.impl;

import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.domain.paper.PaperResponse;
import com.laboratory.paper.domain.paper.PaperResponseData;
import com.laboratory.paper.entity.Paper;
import com.laboratory.paper.mapper.PaperMapper;
import com.laboratory.paper.service.PaperService;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPaperServiceImpl implements PaperService {
    @Resource
    private PaperMapper paperMapper;

    @Override
    public List<PaperListItem> searchPaperList(Long folderId, String keyword) {
        if(folderId == null || folderId == 0) {
           return paperMapper.searchAllPapers(keyword);
        }else {
            return paperMapper.searchPaperByFolder(folderId, keyword);
        }

    }

    @Override
    public PaperResponse queryBaseInfo(QueryPaperBaseInfoVo queryPaperBaseInfo) {
        List<Paper> papers;
        Integer totalPage = 1;
        if(queryPaperBaseInfo.getPaperId() == null || queryPaperBaseInfo.getPaperId() == 0) {
            papers  = paperMapper.queryAllPapersBaseInfo(queryPaperBaseInfo);
            totalPage = paperMapper.queryAllPapersBaseInfoPages(queryPaperBaseInfo);
        }else {
            papers = paperMapper.queryPapersById(queryPaperBaseInfo);
        }

        PaperResponse paperResponse = new PaperResponse();

        // 遍历数据添加
        for(Paper paper : papers) {
            paperResponse.getData().add(new PaperResponseData(
                    paper.getId(),
                    paper.getTitle(),
                    paper.getAuthor(),
                    paper.getPublishTime(),
                    paper.getJournal(),
                    paper.getJournalType(),
                    paper.getResearchDirection(),
                    paper.getInnovation(),
                    paper.getDeficiency(),
                    paper.getKeywords(),
                    paper.getCreateTime()
            ));
        }

        // 设置总页数
        paperResponse.setTotalPage(totalPage);

        return paperResponse;
    }
}
