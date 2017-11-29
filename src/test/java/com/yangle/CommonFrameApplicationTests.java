package com.yangle;

import com.yangle.queue.QueueHandler;
import com.yangle.util.EmailSender;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonFrameApplicationTests {
@Autowired
private QueueHandler queueHandler;
	@Autowired
	private EmailSender emailSender;
	@Test
	public void contextLoads() {
	}
	@Test
	public void testLogQueue(){
		queueHandler.sendLogQueue("第一个日志队列");
	}
@Test
	public void testEmail(){
	emailSender.sendEmail("884454075@qq.com","天气数据获取","今天晴转阴，请加外套");
}

	@Test
	public void testHtmlMail() throws Exception {
		String content="<html>\n" +
				"<body>\n" +
				"    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
				"</body>\n" +
				"</html>";
		emailSender.sendHtmlEmail("884454075@qq.com","test simple mail",content);
	}
@Test
	public void testAttachEmail(){
	emailSender.sendAttachEmail("884454075@qq.com","详情请见附件","hello world","C:\\Users\\yangle\\Desktop\\写博客.bat");
}

@Test
	public void testPOIExcel() throws IOException {
	HSSFWorkbook book = new HSSFWorkbook(this.getClass().getClassLoader().getResourceAsStream("web-in.xls"));
	HSSFSheet sheet = book.getSheetAt(0);
	for(int i=1; i<sheet.getLastRowNum()+1; i++) {
		HSSFRow row = sheet.getRow(i);
		String name = row.getCell(0).getStringCellValue(); //名称
		String url = row.getCell(1).getStringCellValue(); //url
		String username = row.getCell(2).getStringCellValue();
		String password = row.getCell(3).getStringCellValue();
		Integer readCount = (int) row.getCell(4).getNumericCellValue();
		System.out.println(name+":"+url+":"+username+":"+password+":"+readCount);
	}
}
	@Test
	public void testPOIExcelXLSX() throws IOException {
		XSSFWorkbook book = new XSSFWorkbook(this.getClass().getClassLoader().getResourceAsStream("web-info.xlsx"));
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
	}
}
