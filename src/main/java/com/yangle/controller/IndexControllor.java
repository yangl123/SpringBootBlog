package com.yangle.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yangle on 2017/11/30.
 */
@Controller
public class IndexControllor {
    private static final java.lang.String ROOT ="D:\\blog-files\\";
    private final ResourceLoader resourceLoader;

    @Autowired
    public IndexControllor(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/to_write")
public String to_write(){
        return "writearticle";
}

@RequestMapping("/to_manage_article")
public String to_manage_article(){
        return "glwz";
}
@RequestMapping("/to_timezhou")
public String to_timezhou(){
        return "timezhou";
}
@RequestMapping("/to_tages")
public String to_tages(){
        return "tages";
}



@ResponseBody
@RequestMapping("/uploadImage")
public Map uploadImage(HttpServletRequest request, String img,String fileExtendName ){
    Map map=new HashMap();
    map.put("msg","上传成功");
    System.out.println(img);
    BASE64Decoder decoder = new BASE64Decoder();
try {
img=img.substring(img.indexOf(",")+1,img.length());
byte[] b = decoder.decodeBuffer(img);

for (int i = 0; i < b.length; ++i) {
if (b[i] < 0) {
b[i] += 256;
}
}

    Files.copy(new ByteArrayInputStream(b) , Paths.get(ROOT, UUID.randomUUID().toString().replaceAll("-", "")+"."+fileExtendName));


} catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }return map;}
/**
 * 上传文件
 */
@ResponseBody
@RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
public Map handleFileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
    Map map=new HashMap();
    if (!file.isEmpty()) {
        try {
            String name=file.getOriginalFilename();
            String fileExtendName=name.substring(name.indexOf(".")+1,name.length());
            Files.copy(file.getInputStream() , Paths.get(ROOT, UUID.randomUUID().toString().replaceAll("-", "")+"."+fileExtendName));
            map.put("errno",0);
            map.put("data",new String[]{request.getContextPath()+"/images/"+file.getOriginalFilename()});
            return map;
        }  catch (IOException e) {
            e.printStackTrace();
            map.put("errno",1);
            map.put("data",new String[]{request.getContextPath()+"/images/"+file.getOriginalFilename()});
            return map;
        }
}  else {
        map.put("errno",1);
        map.put("data",new String[]{request.getContextPath()+"/images/"+file.getOriginalFilename()});
        return map;
    }

}

    /**
     * 显示图片
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
    @RequestMapping("testpdf")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {


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
            temp = cfg.getTemplate("login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 创建数据模型 */
        Map root = new HashMap();
        root.put("user", "Big Joe");
        // Map latest = new HashMap();
        // root.put("latestProduct", latest);
        // latest.put("name", "green mouse");

        /* 将生成的内容写入hello .html中 */

        String file1 = basePath + "templates/contractTemplate.html";
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

        String url = new File(basePath + "templates/freemarker/printpdf.html").toURI().toURL().toString();
        String outputFile = basePath + "templates/contractTemplate.pdf";
        OutputStream os = new FileOutputStream(outputFile);

        ITextRenderer renderer = new ITextRenderer();
        // PDFEncryption pdfEncryption = new
        // PDFEncryption(null,null,PdfWriter.ALLOW_PRINTING);
        // renderer.setPDFEncryption(pdfEncryption); //只有打印权限的
        renderer.setDocument(url);
        String aa=this.getClass().getResource("/static").getPath();
        renderer.getFontResolver().addFont("C:/WINDOWS/Fonts/SIMSUN.TTC",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.getSharedContext().setBaseURL( new File(aa).toURI().toURL().toString());

        renderer.layout();
        try {
            renderer.createPDF(os);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("转换成功！");
        os.close();
    }
}
