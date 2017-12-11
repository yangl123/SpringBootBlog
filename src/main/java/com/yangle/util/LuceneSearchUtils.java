package com.yangle.util;

import com.yangle.domain.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/12/11.
 */
public class LuceneSearchUtils {
    private static Directory directory;

    private static IndexReader indexReader;

    private static IndexSearcher indexSearcher;
private static final String root ="d:/index_blog";
    public static void createIndex(List<Article> articles, Map<String, String> dircts) {
        File file = Paths.get(root, "indexDir/").toFile();
        if (!file.exists()) {
            try {
                new File(root).mkdir();
                long start = System.currentTimeMillis();
                //索引存放的位置，设置在当前目录中
                Directory directory = FSDirectory.open(Paths.get("indexDir/"));
                //在 6.6 以上版本中 version 不再是必要的，并且，存在无参构造方法，可以直接使用默认的 StandardAnalyzer 分词器。
                Version version = Version.LUCENE_7_1_0;
                Analyzer analyzer = new IKAnalyzer();//中文分词
                //创建索引写入配置
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
                //创建索引写入对象
                IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
                //创建Document对象，存储索引
                for (Article article : articles) {

                    indexWriter.addDocument(generateDoc(article,dircts));
                }
                indexWriter.commit();
                indexWriter.close();
                long end = System.currentTimeMillis();
                System.out.println("索引花费了" + (end - start) + " 毫秒");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws IOException {
        //索引存放的位置，设置在当前目录中
        directory = FSDirectory.open(Paths.get("indexDir/"));

        //创建索引的读取器
        indexReader = DirectoryReader.open(directory);

        //创建一个索引的查找器，来检索索引库
        indexSearcher = new IndexSearcher(indexReader);
    }

    public void destory() throws Exception {
        indexReader.close();
    }

    //执行查询，并打印查询到的记录数
    public static List<Article>  executeQuery(Query query) throws IOException {
        List<Article> articles=new ArrayList<>();
        TopDocs topDocs = indexSearcher.search(query, 100);
        //打印查询到的记录数
        System.out.println("总共查询到" + topDocs.totalHits + "个文档");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //取得对应的文档对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            Article article=new Article();
            article.setId(Integer.valueOf(document.get("id")));
            article.setTitle(document.get("title"));
            article.setContent(document.get("content"));
            article.setTages(document.get("tages"));
            articles.add(article);
        }
        return articles;
    }

    public static  void  updateIndex(Article article,Map<String, String> dircts) throws IOException {
        Analyzer analyzer = new IKAnalyzer();//中文分词
        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //创建索引写入对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        long count = indexWriter.updateDocument(new Term("title", "sss"), generateDoc(article,dircts));
        System.out.println("更新文档:" + count);
        indexWriter.commit();
        indexWriter.close();
    }

    public static List<Article> HighlighterTest(String key) throws IOException, ParseException, InvalidTokenOffsetsException {
        Analyzer analyzer = new IKAnalyzer();//中文分词
        String[] filedStr = new String[]{"title", "content","category","tages"};
        //指定搜索字段和分析器
        QueryParser parser =  new MultiFieldQueryParser(filedStr, analyzer);
        //用户输入内容
        Query query = parser.parse(key);

        TopDocs topDocs = indexSearcher.search(query, 100);
        // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
        List<Article> articles=new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //取得对应的文档对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            // 内容增加高亮显示
            TokenStream tokenStream1 = analyzer.tokenStream("content", new StringReader(document.get("content")));
            TokenStream tokenStream2 = analyzer.tokenStream("content", new StringReader(document.get("title")));
            TokenStream tokenStream3 = analyzer.tokenStream("content", new StringReader(document.get("tages")));
            String content = highlighter.getBestFragment(tokenStream1, document.get("content"));
            String title = highlighter.getBestFragment(tokenStream2, document.get("content"));
            String tages = highlighter.getBestFragment(tokenStream3, document.get("content"));
            Integer id= Integer.valueOf(document.get("id"));
            Article article=new Article();
            article.setId(id);
            article.setTitle(title);
            article.setContent(content);
            article.setTages(tages);
            articles.add(article);
        }
        return articles;

    }
    public static Document generateDoc(Article article, Map<String, String> dircts){
        Document doc = new Document();
        //将字段加入到doc中
        doc.add(new StringField("id", article.getId() + "", Field.Store.YES));
        doc.add(new StringField("title", article.getTitle(), Field.Store.YES));
        doc.add(new StringField("tages", article.getTages(), Field.Store.YES));
        doc.add(new StringField("category", dircts.get(article.getCategoryId()), Field.Store.YES));
        doc.add(new TextField("content", article.getContent().replaceAll("<.+?>", ""), Field.Store.YES));
        return doc;
    }

}
