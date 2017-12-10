package com.yangle.service;

import com.yangle.domain.Article;
import com.yangle.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IArticleService {
    void save(Article article);
     List<Article> getArticles(User user);
     Article getArticle(int id);
    List<String> getYears(User user);
    void delete(String id);
}
