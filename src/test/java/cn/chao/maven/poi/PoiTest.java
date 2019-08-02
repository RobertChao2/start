package cn.chao.maven.poi;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 	完成 POI 的测试，在对执行 Excel 的工作才能更好的进行。
 */
@Log4j2
public class PoiTest {

	private final static String excelFilePath= "C:\\Users\\RebortChao\\Desktop\\poiTest.xls";

	public static void main(String[] args) {
		log.debug("开始执行 main 方法 ……");
		File file = new File(excelFilePath);
		if(!file.exists()){
			log.debug("文件不存在，已新建用户 ……");
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.debug("新建文件异常，请重试 ……");
				e.printStackTrace();
			}
		}
		multiThreadWrite();
		log.debug("结束执行 main 方法 ……");
	}
	
	/**
	 * 使用多线程进行 Excel 写操作，提高写入效率。
	 */
	public static void multiThreadWrite() {
		/**
		 * 使用线程池进行线程管理。
		 */
		ExecutorService es = Executors.newCachedThreadPool();
		/**
		 * 使用计数栅栏
		 */
		CountDownLatch doneSignal = new CountDownLatch(3);		// 3 个线程，开启三个计数栅栏。
 
		HSSFWorkbook wb;
		try {
			wb = new HSSFWorkbook(new FileInputStream(excelFilePath));
			HSSFSheet sheet = wb.getSheetAt(0);
			es.submit(new PoiWriter(doneSignal, sheet, 1, 19999));		// 调用后边线程方法
			es.submit(new PoiWriter(doneSignal, sheet, 20000, 39999));
			es.submit(new PoiWriter(doneSignal, sheet, 40000, 59999));
			/**
			 * 使用 CountDownLatch 的 await 方法，等待所有线程完成 sheet 操作
			 */
			doneSignal.await();
			es.shutdown();
			FileOutputStream os = new FileOutputStream(excelFilePath);
			wb.write(os);
			os.flush();
			os.close();
			System.out.println("Excel completed ......");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//	设置文档的基本信息，（可选内容）
	public void setDocumentSummaryInformation(HSSFWorkbook workbook){
		//	获取文档信息，并配置
		DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
		//	文档类别
		dsi.setCategory("Document Type");
		//	设置文档管理员
		dsi.setManager("Document Manager");
		//	设置组织机构
		dsi.setCompany("Document Company");
		//	获取摘要信息并配置
		SummaryInformation si = workbook.getSummaryInformation();
		//	设置文档主题
		si.setSubject("Document Theme");
		//	设置文档标题
		si.setTitle("Document Title");
		//	设置文档作者
		si.setAuthor("Document Author --> RobertChao");
		//	设置文档备注
		si.setComments("Document Comments");
	}


	/**
	 * 测试基本的 POI 写操作
	 */
	public static void poiBasicWriteTest() {
		try {
			//	首先，创建 Excel 文档。
			//	HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFilePath));	// 创建一个 Excel 文档，并为 输入流。
			//	1、创建一个 Excel 表单,参数为 sheet 的名字
			//	HSSFSheet sheet = workbook.createSheet("XXX集团员工信息表");
			//	2、创建一行
			//	HSSFRow headerRow = sheet.createRow(0);
			//	3、第一行中创建第一个单元格，并设置数据
			//	HSSFCell cell0 = headerRow.createCell(0);
			//	cell0.setCellValue("编号");
			//	4、将 Excel 上述内容写到 字节输出流 。
			//	baos = new ByteArrayOutputStream();
			//	workbook.write(baos);
			//  5、返回如下内容，则开启浏览器下载
			//  return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
			HSSFSheet sheet = wb.getSheetAt(0);		// 获得第一张 Sheet 表内容
			HSSFRow row = sheet.createRow(0);		// 表上创建一个行元素，位置在第一位
			HSSFCell contentCell = row.createCell(0);	// 行上创建一个元素，位置在第一位
			contentCell.setCellValue("abc");	// 设置元素值
			FileOutputStream os = new FileOutputStream(excelFilePath);	// 打卡输出流内容
			wb.write(os);	// 写到输入流中
			os.flush();		// 刷新输出流
		} catch (FileNotFoundException e) {
			log.debug("没有找到指定文件："+e.getCause());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sheet 的 row 使用 treeMap 存储的，是非线程安全的，所以在创建 row 时需要进行同步操作。
	 * @param sheet
	 * @param rownum
	 * @return
	 */
	private static synchronized HSSFRow getRow(HSSFSheet sheet, int rownum) {
		return sheet.createRow(rownum);
	}
	
	/**
	 * 进行 sheet 写操作的 sheet。
	 * implements Runnable 跟线程有关的操作。
	 * @author alex
	 *
	 */
	protected static class PoiWriter implements Runnable {
 
		private final CountDownLatch doneSignal;
		private HSSFSheet sheet;
		private int start;
		private int end;
 
		public PoiWriter(CountDownLatch doneSignal, HSSFSheet sheet, int start, int end) {
			this.doneSignal = doneSignal;
			this.sheet = sheet;
			this.start = start;
			this.end = end;
		}
 
		public void run() {
			int i = start;
			try {
				while (i <= end) {
					HSSFRow row = getRow(sheet, i);		// 找到对应表 （sheet） 中的对应行（i），
					HSSFCell contentCell = row.getCell(0);	// 获得行中的第一个元素。
					if (contentCell == null) {		// 如果行中的内容是空的
						contentCell = row.createCell(0);	// 则创建元素
					}
					contentCell.setCellValue(i + 1);	// 并设置值为 （行号 + 1）
					++i;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				doneSignal.countDown();
				System.out.println("start: " + start + " end: " + end + " Count: " + doneSignal.getCount());
			}
		}
	}
}
