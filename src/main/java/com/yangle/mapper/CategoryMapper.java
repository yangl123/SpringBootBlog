package com.yangle.mapper;

import com.yangle.domain.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CategoryMapper {
    @Select("select * from tb_category  where  categoryName !='隐私'")
    List<Category> getCategories();

    @Select("select * from tb_category ")
    List<Category> getCategoriesAdmin();

    @Insert("insert into tb_category values(#{id},#{categoryName})")
    void insert(Category category);

    @Delete("delete from  tb_category where id=#{id}")
    void delete(String id);

    @Update("update tb_category set categoryName=#{categoryName} where id=#{id}")
    void update(Category category);

}
