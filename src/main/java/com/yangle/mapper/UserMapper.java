package com.yangle.mapper;

import com.yangle.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from tb_user where userId=#{userId}")
    User getUserByuserId(@Param("userId") String userId);
//    @Select("select * from user where name=#{name}")
//    User getUserByName(@Param("name") String name);
//
//    @Select("select * from user")
//    List<Map<String,Object>> getUsers();
}
