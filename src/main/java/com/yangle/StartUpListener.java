package com.yangle;

import com.yangle.domain.Article;
import com.yangle.domain.Category;
import com.yangle.mapper.ArticleMapper;
import com.yangle.mapper.CategoryMapper;
import com.yangle.util.LuceneSearchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/10/11.
 */
public class StartUpListener implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartUpListener.class);
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("======================系统启动之后，可以在这里执行一些事情。。。。。。===================");
        List<Article> articles=articleMapper.getArticlesByAdmin();
        List<Category> categories=categoryMapper.getCategories();
        Map<String,String> dircts=new HashMap<>();
        for(Category category:categories){
            dircts.put("key"+category.getId(),category.getCategoryName());
        }
        LuceneSearchUtils.createIndex(articles,dircts);
    }
}
