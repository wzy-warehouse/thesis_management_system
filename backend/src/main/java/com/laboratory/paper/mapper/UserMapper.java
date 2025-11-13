package com.laboratory.paper.mapper;

import com.laboratory.paper.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    User findById(Long id);
}
