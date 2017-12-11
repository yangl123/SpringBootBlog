package com.yangle.service.impl;

import com.yangle.domain.Article;
import com.yangle.domain.User;
import com.yangle.mapper.ArticleMapper;
import com.yangle.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public int save(Article article) {

        return articleMapper.create(article);
    }
    @Override
    public int update(Article article) {

            return  articleMapper.update(article);

    }

    @Override
    public List<Article> getArticles(User user) {

        if(user==null){
            return articleMapper.getArticles();
        }else{
            return articleMapper.getArticlesByAdmin();
        }


    }

    @Override
    public Article getArticle(int id) {
        return articleMapper.getArticle(id);
    }

    @Override
    public List<String> getYears(User user) {
        if(user==null){
            return articleMapper.getYears();
        }else {
            return articleMapper.getYearsByAdmin();
        }

    }
    public void delete(String id){
        articleMapper.delete(id);
    }
}
