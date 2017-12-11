package com.yangle.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.yangle.domain.Article;
import com.yangle.domain.ArticleForLucene;
import com.yangle.domain.Category;
import com.yangle.domain.User;
import com.yangle.service.IArticleService;
import com.yangle.service.ICategoryService;
import com.yangle.service.IUserService;
import com.yangle.util.LuceneSearchUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by yangle on 2017/11/30.
 */
@Controller
public class IndexControllor {
    private static final java.lang.String ROOT = "D:\\blog-files\\";
    private final ResourceLoader resourceLoader;

    @Autowired
    public IndexControllor(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
@Autowired
private ICategoryService categoryServiceImpl;
    @Autowired
    private IArticleService articleServiceImpl;
    Map<String,List<Article>> tages=null;

    @RequestMapping("to_search_list")
    public String search(String key,HttpServletRequest request){
        List<ArticleForLucene> articles=new ArrayList<>();
        try {
             articles=LuceneSearchUtils.HighlighterTest(key);

        } catch (Exception e) {

        }
        request.setAttribute("articles",articles);
        return "searchresult";
    }

    @ResponseBody
    @RequestMapping("deleteArticle")
    public Integer deleteArticle(String id){
        try{
                articleServiceImpl.delete(id);
            LuceneSearchUtils.deleteIndex(id);
        return 0;//删除成功
        }
catch (Exception e){
    e.printStackTrace();
return 1;//删除失败
}
    }
@RequestMapping("/viewArticle")
public String viewArticle(String id,HttpServletRequest request){
    Article article=articleServiceImpl.getArticle(Integer.parseInt(id));
    request.setAttribute("tages",Arrays.asList(article.getTages().split(",")));
    article.setViewCount(article.getViewCount()+1);
    articleServiceImpl.update(article);//更新阅读次数
    request.setAttribute("article",article);
    Map<String,String> dircts=new HashMap<>();
    for(Category category:categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user"))){
        dircts.put("key"+category.getId(),category.getCategoryName());
    }
    request.setAttribute("dircts",dircts);
    return "view";
}
@RequestMapping("/submitArticle")
public String submitArticle(Article article,HttpServletRequest request){
    article.setSubTime(new Date());
    if(article.getId()==null||"".equals(article.getId())){
article.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        articleServiceImpl.save(article);
    }else{
        articleServiceImpl.update(article);
    }
    List<Category> categories=categoryServiceImpl.getCategories(new User());
    Map<String,String> dircts=new HashMap<>();
    for(Category category:categories){
        dircts.put("key"+category.getId(),category.getCategoryName());
    }
    try {
        LuceneSearchUtils.updateIndex(article,dircts);
    } catch (Exception e) {

    }
    return index(request);
}
@Autowired
private IUserService userServiceImpl;
    @RequestMapping("/login")
    public String login(String userid,String password,HttpServletRequest request){

        User user=userServiceImpl.getUser(userid);
        if(user==null){
            request.setAttribute("message","用户不存在");
            return "login";
        }else if(!user.getPassword().equals(new BASE64Encoder().encode(password.getBytes()))){
            request.setAttribute("message","密码不正确");
            return "login";
        }
        request.getSession().setAttribute("user",user);
        return index(request);
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().setAttribute("user",null);
        request.setAttribute("message","你已成功退出");
        return "login";
    }




    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        Map<String,String> dircts=new HashMap<>();
        for(Category category:categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user"))){
            dircts.put("key"+category.getId(),category.getCategoryName());
        }
        request.setAttribute("dircts",dircts);
        request.setAttribute("articles",articleServiceImpl.getArticles((User) request.getSession().getAttribute("user")));
        request.setAttribute("categories",categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user")));
        return "index";
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/to_write")
    public String to_write(HttpServletRequest request) {

        String id= request.getParameter("id");
        if(id!=null&&!id.equals("")){
            request.setAttribute("article",articleServiceImpl.getArticle(Integer.parseInt(id)));
        }else {
            request.setAttribute("article",new Article());
        }
request.setAttribute("categories",categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user")));
        return "writearticle";
    }

    @RequestMapping("/to_manage_article")
    public String to_manage_article(HttpServletRequest request) {


        request.setAttribute("categories",categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user")));
        request.setAttribute("articles",articleServiceImpl.getArticles((User) request.getSession().getAttribute("user")));
        return "glwz";
    }

    @RequestMapping("/to_timezhou")
    public String to_timezhou(HttpServletRequest request) {
        request.setAttribute("years",articleServiceImpl.getYears((User) request.getSession().getAttribute("user")));
request.setAttribute("articles",articleServiceImpl.getArticles((User) request.getSession().getAttribute("user")));

        return "timezhou";
    }

    @RequestMapping("/to_tages")
    public String to_tages(HttpServletRequest request){
    getTagContents(articleServiceImpl.getArticles((User) request.getSession().getAttribute("user")));
        request.setAttribute("tages",tages);
        Map<String,String> dircts=new HashMap<>();
        for(Category category:categoryServiceImpl.getCategories((User) request.getSession().getAttribute("user"))){
            dircts.put("key"+category.getId(),category.getCategoryName());
        }
        request.setAttribute("dircts",dircts);
    return "tages";
    }


    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map uploadImage(HttpServletRequest request, String img, String fileExtendName) {
        Map map = new HashMap();
        map.put("msg", "上传成功");
        System.out.println(img);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            img = img.substring(img.indexOf(",") + 1, img.length());
            byte[] b = decoder.decodeBuffer(img);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            String fileName=UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtendName;
            Files.copy(new ByteArrayInputStream(b), Paths.get(ROOT, fileName));
map.put("imageUrl",request.getContextPath() + "/images/" + fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 上传文件
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
    public Map handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map map = new HashMap();
        if (!file.isEmpty()) {
            try {
                String name = file.getOriginalFilename();
                String fileExtendName = name.substring(name.indexOf(".") + 1, name.length());
                String fileName=UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtendName;
                Files.copy(file.getInputStream(), Paths.get(ROOT,fileName ));
                map.put("errno", 0);
                map.put("data", new String[]{request.getContextPath() + "/images/" + fileName});
                return map;
            } catch (IOException e) {
                e.printStackTrace();
                map.put("errno", 1);
                map.put("data", new String[]{request.getContextPath() + "/images/" + file.getOriginalFilename()});
                return map;
            }
        } else {
            map.put("errno", 1);
            map.put("data", new String[]{request.getContextPath() + "/images/" + file.getOriginalFilename()});
            return map;
        }

    }

    /**
     * 显示图片
     *
     * @param filename
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 测试转pdf
     */
    @RequestMapping("downloadArticle")
    public void test(HttpServletRequest request, HttpServletResponse response,String id) throws IOException, DocumentException {


        String basePath = this.getClass().getResource("/").getPath();

        /* 创建配置 */
        Configuration cfg = new Configuration();
        /* 指定模板存放的路径 */
        try {
            cfg.setDirectoryForTemplateLoading(new File(basePath + "/templates/freemarker"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        // cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template temp = null;
        try {
            temp = cfg.getTemplate("printpdf.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 创建数据模型 */
        Map root = new HashMap();
        Article article=articleServiceImpl.getArticle(Integer.parseInt(id));
        article.setContent(article.getContent().replace("<br>","<br></br>"));
        root.put("article", article);
        root.put("tages", Arrays.asList(article.getTages().split(",")));
        /* 将生成的内容写入hello .html中 */
        String file1 = basePath + "templates/freemarker/temp.html";
        File file = new File(file1);
        if (!file.exists())
            file.createNewFile();
        // Writer out = new FileWriter(file);
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Writer out = new OutputStreamWriter(System.out);
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        File file2=Paths.get(ROOT+"/pdf",article.getTitle()+".pdf" ).toFile();
        OutputStream os = new FileOutputStream(file2);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(file1);
        String aa = this.getClass().getResource("/static").getPath();
        renderer.getFontResolver().addFont("C:/WINDOWS/Fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.getSharedContext().setBaseURL(new File(aa).toURI().toURL().toString());
        renderer.layout();
        try {
            renderer.createPDF(os);
String filename=article.getTitle()+".pdf";
            String userAgent = request.getHeader("User-Agent");
//针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                try {
                    filename = java.net.URLEncoder.encode(filename, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
//非IE浏览器的处理：
                filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
            }

            if(file2.exists()){ //判断文件父目录是否存在
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
                response.setCharacterEncoding("UTF-8");
                byte[] buffer = new byte[1024];
                FileInputStream fis = null; //文件输入流
                BufferedInputStream bis = null;

                OutputStream os2 = null; //输出流
                try {
                    os2 = response.getOutputStream();
                    fis = new FileInputStream(file2);
                    bis = new BufferedInputStream(fis);
                    int i = bis.read(buffer);
                    while(i != -1){
                        os2.write(buffer);
                        i = bis.read(buffer);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        os.close();

    }
    /**
     * 处理标签
     */
    public void getTagContents(List<Article> articles){
tages=new HashMap<>();
        for(Article article:articles){
            if(article.getTages()!=null){
            String[] articleTages=article.getTages().split(",");
            for(String tag:articleTages){
                handlerTag(tag,article);
            }}
        }
    }

    public void handlerTag(String tag, final Article article){
            if(tages.get(tag)==null){
                tages.put(tag,new ArrayList<Article>(){{add(article);}});
            }else{
                List<Article> list=tages.get(tag);
                list.add(article);
            }
    }
}
