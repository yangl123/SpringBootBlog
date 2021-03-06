package com.yangle.mapper;

import com.yangle.domain.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleMapper {
    @Insert("insert into tb_article(id,title,categoryId,content,tages,thumbUrl,subTime,viewCount) values(#{id},#{title},#{categoryId},#{content},#{tages},#{thumbUrl},#{subTime},0)")
    public int create(Article article);

    @Select("select * from tb_article where categoryId not in(select id from tb_category where categoryName='隐私') order by subTime desc")
    public List<Article> getArticles();

    @Select("select * from tb_article order by subTime desc")
    public List<Article> getArticlesByAdmin();
    @Select("select * from tb_article where id=#{id} ")

    public Article getArticle(String id);
    @Insert("update tb_article set title=#{title},categoryId=#{categoryId},content=#{content},tages=#{tages},thumbUrl=#{thumbUrl},subTime=#{subTime},viewCount=#{viewCount} where id=#{id}")
    public int update(Article article);

    @Select("select left(subTime,4) from tb_article where  categoryId not in(select id from tb_category where categoryName='隐私') group by left(subTime,4) order by subTime desc")
    public List<String> getYears();

    @Select("select left(subTime,4) from tb_article   group by left(subTime,4) order by subTime desc")
    public List<String> getYearsByAdmin();

    @Delete("delete from tb_article where id=#{id}")
    public void delete(String id);

    @Delete("delete from tb_article where categoryId=#{id}")
    public void deleteByCatId(String id);
}
