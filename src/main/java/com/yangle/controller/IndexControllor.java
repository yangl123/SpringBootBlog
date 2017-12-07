package com.yangle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

            Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));
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
}
