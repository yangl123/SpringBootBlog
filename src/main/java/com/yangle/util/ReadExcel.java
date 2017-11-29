package com.yangle.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangle on 2017/10/9.
 */
    public class ReadExcel {
    public static String getAnalyzeMetgodName(String fileName){
        String temp=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        return temp;
    }

    public static XSSFSheet getXLSXContent(InputStream is){
        XSSFWorkbook book = null;
        try {
            book = new XSSFWorkbook(ReadExcel.class.getClassLoader().getResourceAsStream("web-info.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = book.getSheetAt(0);
        for(int i=1; i<sheet.getLastRowNum()+1; i++) {
            XSSFRow row = sheet.getRow(i);
            String name = row.getCell(0).getStringCellValue(); //名称
            String url = row.getCell(1).getStringCellValue(); //url
            String username = row.getCell(2).getStringCellValue();
            String password = row.getCell(3).getStringCellValue();
            Integer readCount = (int) row.getCell(4).getNumericCellValue();
            System.out.println(name+":"+url+":"+username+":"+password+":"+readCount);
        }
        return sheet;

    }

    public static HSSFSheet getXLSContent(InputStream is){
        HSSFWorkbook book = null;
        try {
            book = new HSSFWorkbook(ReadExcel.class.getClassLoader().getResourceAsStream("web-info.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = book.getSheetAt(0);
        return sheet;

    }

    public static void main(String[] args) {
        ReadExcel.getXLSXContent(null);
    }
}
