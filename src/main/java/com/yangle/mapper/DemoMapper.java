package com.yangle.mapper;

import com.yangle.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/9/24.
 */
@Repository
    public interface DemoMapper {
        @Select("select * from user where id=#{id}")
        User getUserById(@Param("id") int id);
    @Select("select * from user where name=#{name}")
    User getUserByName(@Param("name") String name);

    @Select("select * from user")
    List<Map<String,Object>> getUsers();


}
