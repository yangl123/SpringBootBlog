package com.yangle.service;

import com.yangle.domain.Category;
import com.yangle.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ICategoryService {
    List<Category> getCategories(User user);
    void insert(Category category);
}
