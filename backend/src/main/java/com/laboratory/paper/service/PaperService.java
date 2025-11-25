package com.laboratory.paper.service;

import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.domain.paper.PaperResponse;
import com.laboratory.paper.domain.paper.PaperUploadRequest;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PaperService {
    List<PaperListItem> searchPaperList(Long folderId, String keyword);

    PaperResponse queryBaseInfo(QueryPaperBaseInfoVo queryPaperBaseInfo);

    void deletePaper(Long id, Long parentId, Long userId);

    void upload(PaperUploadRequest paperUploadRequest, MultipartFile file, Long userId);

    Mono<String> chat(Long paperId);
}
