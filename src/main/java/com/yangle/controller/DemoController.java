package com.yangle.controller;

import com.yangle.domain.User;
import com.yangle.repository.UserRepository;
import com.yangle.service.IUserService;
import com.yangle.util.GuavaDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/9/24.
 */
@Controller
public class DemoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserService userServiceImpl;
    @Autowired
    private GuavaDataCache dataCache;
    private List<User> userList;
    private int s;
//    @RequestMapping("/home")
//    public String home(Model model,String name){
//
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//        String userName=userDetails.getUsername();
//        String passwd=userDetails.getPassword();
//        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
////
////         userList= (List<User>) userRepository.findAll();
////        List<Object[]> ss=userRepository.getUsers();
////        System.out.print(ss);
////      s=userRepository.getUserCount();
////        System.out.print(s);
////        System.out.printf("2222222222");
////        Loginer loginer=new Loginer();
////        loginer.setId("00000000001");
////        loginer.setName("杨乐");
////        model.addAttribute("s",s);model.addAttribute("name",name);
//        return "hello";
//    }
@RequestMapping("/mybatis")
public String testMapper(int id){
    User user=userServiceImpl.getUsers(id);
    System.out.print(user);
    List<Map<String,Object>> users=userServiceImpl.getUsers();
    System.out.print(users);
    return "hello";
}
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "fileupload";
    }

    // 访问路径为：http://ip:port/upload/batch
    @RequestMapping(value = "/upload/batch", method = RequestMethod.GET)
    public String batchUpload() {
        return "mutifileupload";
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // 这里只是简单例子，文件直接输出到项目路径下。
                // 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
                // 还有关于文件格式限制、文件大小限制，详见：中配置。
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     *
     * @param request
     * @return
     *
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @RequestMapping(value = "/upload/batch", method = RequestMethod.POST)
    public @ResponseBody String batchUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + i + " because the file was empty.";
            }
        }
        return "upload successful";
    }

    @RequestMapping("download")
    public String downLoad(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException {
        String filename="存在问题3.doc";
        String filePath = "C:\\Users\\yangle\\Desktop\\" ;
        File file = new File(filePath + "/" + filename);
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

        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
            response.setCharacterEncoding("UTF-8");
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
//测试缓存
    @RequestMapping("/putCache")
    @ResponseBody
    public String putCache(String key,String value){
        dataCache.put(key,value);
return  value;
    }

    @RequestMapping("/getCache")
    @ResponseBody
    public String getCache(String key){

        return (String) dataCache.query(key);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }
}