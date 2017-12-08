package com.yangle.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by yangle on 2017/12/8.
 */
public class ServletUtils {
    public static String forward(HttpServletRequest request, HttpServletResponse response, String src) {
        try{
/* ↓↓↓↓↓重新构造response，修改response中的输出流对象，使其输出到字节数组↓↓↓↓↓ */
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ServletOutputStream servletOuputStream = new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    byteArrayOutputStream.write(b);
                }
                @Override
                public boolean isReady() {
                    return false;
                }
                @Override
                public void setWriteListener(WriteListener writeListener) {
                }
            };
            final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, "UTF-8"));
            response = new HttpServletResponseWrapper(response) {
                @Override
                public ServletOutputStream getOutputStream() {
                    return servletOuputStream;
                }
                @Override
                public PrintWriter getWriter() {
                    return printWriter;
                }
            };
/* ↑↑↑↑↑↑重新构造response，修改response中的输出流对象，使其输出到字节数组↑↑↑↑↑↑ */
//执行forward操作
            request.getRequestDispatcher(src).forward(request,response);
//把字节流中的内容太转为字符串
            return new String(byteArrayOutputStream.toByteArray(),"utf-8");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
