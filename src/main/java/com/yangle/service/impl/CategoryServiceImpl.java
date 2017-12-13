package com.yangle.service.impl;

import com.yangle.domain.Category;
import com.yangle.domain.User;
import com.yangle.mapper.CategoryMapper;
import com.yangle.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements ICategoryService {
@Autowired
private CategoryMapper categoryMapper;
    @Override
    public List<Category> getCategories(User user) {


        if(user==null){
            return categoryMapper.getCategories();
        }else{
            return categoryMapper.getCategoriesAdmin();
        }


    }

    @Override
    public void insert(Category category) {
categoryMapper.insert(category);
    }
}
