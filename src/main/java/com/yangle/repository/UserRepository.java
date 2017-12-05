package com.yangle.repository;

import com.yangle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yangle on 2017/9/24.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select count(1) from User u")
    int getUserCount();
    @Query(value = "select * from tb_user",nativeQuery = true)
    List<Object[]> getUsers();
}
