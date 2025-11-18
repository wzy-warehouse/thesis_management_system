package com.laboratory.paper.service.impl;

import com.laboratory.paper.mapper.RecycleBinMapper;
import com.laboratory.paper.service.RecycleBinService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IRecycleBinServiceImpl implements RecycleBinService {

    @Resource
    private RecycleBinMapper recycleBinMapper;

    @Override
    public void deleteByFolderId(Long folderId) {
        recycleBinMapper.deleteByFolderId(folderId);
    }

}
