package com.yangle.mapper;

import com.yangle.domain.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CategoryMapper {
    @Select("select * from tb_category  where  categoryName !='隐私'")
    List<Category> getCategories();

    @Select("select * from tb_category ")
    List<Category> getCategoriesAdmin();

    @Insert("insert into tb_category values(null,#{categoryName})")
    void insert(Category category);
}
