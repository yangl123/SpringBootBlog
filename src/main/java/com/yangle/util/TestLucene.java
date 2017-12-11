package com.yangle.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;

/**
 * Created by yangle on 2017/12/8.
 */

        public  class TestLucene {

        private Directory directory;

        private IndexReader indexReader;

        private IndexSearcher indexSearcher;
        private static final String root ="d:/index_blog";

        public void setUp() throws IOException {
                //索引存放的位置，设置在当前目录中
                directory = FSDirectory.open(Paths.get(root,"indexDir/"));

                //创建索引的读取器
                indexReader = DirectoryReader.open(directory);

                //创建一个索引的查找器，来检索索引库
                indexSearcher = new IndexSearcher(indexReader);
        }


        public void tearDown() throws Exception {
                indexReader.close();
        }

        //执行查询，并打印查询到的记录数
        public void executeQuery(Query query) throws IOException {
                TopDocs topDocs = indexSearcher.search(query, 100);
                //打印查询到的记录数
                System.out.println("总共查询到" + topDocs.totalHits + "个文档");
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        //取得对应的文档对象
                        Document document = indexSearcher.doc(scoreDoc.doc);
                        System.out.println("id：" + document.get("id"));
                        System.out.println("title：" + document.get("title"));
                        System.out.println("content：" + document.get("content"));
                }
        }

        /**
         * 分词打印
         *
         * @param analyzer
         * @param text
         * @throws IOException
         */
        public void printAnalyzerDoc(Analyzer analyzer, String text) throws IOException {

                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
                CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                try {
                        tokenStream.reset();
                        while (tokenStream.incrementToken()) {
                                System.out.println(charTermAttribute.toString());
                        }
                        tokenStream.end();
                } finally {
                        tokenStream.close();
                        analyzer.close();
                }
        }

        public void indexWriterTest() throws IOException {
                long start = System.currentTimeMillis();

                //索引存放的位置，设置在当前目录中
                Directory directory = FSDirectory.open(Paths.get("indexDir/"));

                //在 6.6 以上版本中 version 不再是必要的，并且，存在无参构造方法，可以直接使用默认的 StandardAnalyzer 分词器。
                Version version = Version.LUCENE_7_1_0;

                //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
                //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
                //Analyzer analyzer = new ComplexAnalyzer();//中文分词
                //Analyzer analyzer = new IKAnalyzer();//中文分词

                Analyzer analyzer = new IKAnalyzer();//中文分词

                //创建索引写入配置
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

                //创建索引写入对象
                IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

                //创建Document对象，存储索引

                Document doc = new Document();

                String  id = "1";

                //将字段加入到doc中
                doc.add(new StringField("id", id,Field.Store.YES));
                doc.add(new StringField("title", "sss", Field.Store.YES));
                doc.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎", Field.Store.YES));


                Document doc2 = new Document();

                String id2 = "2";

                //将字段加入到doc中
                doc2.add(new StringField("id", id2,Field.Store.YES));
                doc2.add(new StringField("title", "Spark", Field.Store.YES));
                doc2.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎", Field.Store.YES));

                //将doc对象保存到索引库中
                indexWriter.addDocument(doc);
                indexWriter.addDocument(doc2);

                indexWriter.commit();
                //关闭流
                indexWriter.close();

                long end = System.currentTimeMillis();
                System.out.println("索引花费了" + (end - start) + " 毫秒");
        }
        public void updateDocumentTest() throws IOException {
                //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
                //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
                //Analyzer analyzer = new ComplexAnalyzer();//中文分词
                //Analyzer analyzer = new IKAnalyzer();//中文分词

                Analyzer analyzer = new IKAnalyzer();//中文分词

                //创建索引写入配置
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

                //创建索引写入对象
                IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

                Document doc = new Document();

                String id = "4";

                doc.add(new StringField("id", id,Field.Store.YES));
                doc.add(new StringField("title", "傻逼", Field.Store.YES));
                doc.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎", Field.Store.YES));

                long count = indexWriter.updateDocument(new Term("title", "sss"), doc);
                System.out.println("更新文档:" + count);
                indexWriter.commit();
                indexWriter.close();
        }

        public void termQueryTest() throws IOException {

                String searchField = "title";
                //这是一个条件查询的api，用于添加条件
                TermQuery query = new TermQuery(new Term(searchField, "傻逼"));

                //执行查询，并打印查询到的记录数
                executeQuery(query);
        }

        public void HighlighterTest() throws IOException, ParseException, InvalidTokenOffsetsException {
                //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
                //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
                //Analyzer analyzer = new ComplexAnalyzer();//中文分词
                //Analyzer analyzer = new IKAnalyzer();//中文分词

                Analyzer analyzer = new IKAnalyzer();//中文分词

                String[] filedStr = new String[]{"tages", "content"};
                String text = "php";

                //指定搜索字段和分析器
                QueryParser parser =  new MultiFieldQueryParser(filedStr, analyzer);

                //用户输入内容
                Query query = parser.parse(text);

                TopDocs topDocs = indexSearcher.search(query, 100);

                // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
                SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
                Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {

                        //取得对应的文档对象
                        Document document = indexSearcher.doc(scoreDoc.doc);

                        // 内容增加高亮显示
                        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(document.get("content")));
                        String content = highlighter.getBestFragment(tokenStream, document.get("content"));

                        System.out.println(content);
                }

        }
        public static void main(String[] args) {
                TestLucene lucene = new TestLucene();

                try {
                        lucene.setUp();
                        lucene.HighlighterTest();
                        lucene.tearDown();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        }


