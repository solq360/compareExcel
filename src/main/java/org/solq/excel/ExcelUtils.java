package org.solq.excel;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author solq
 */
public abstract class ExcelUtils {

	public static Map<String, Map<Integer, List<String>>> readExcelData(
			String file) {
		InputStream ips = null;
		Workbook readBook = null;
		try {
			ips = new FileInputStream(file);

			if (!ips.markSupported()) {
				ips = new PushbackInputStream(ips, 8);
			}
			if (POIFSFileSystem.hasPOIFSHeader(ips)) {
				readBook = new HSSFWorkbook(ips);
			}
			if (POIXMLDocument.hasOOXMLHeader(ips)) {
				readBook = new XSSFWorkbook(OPCPackage.open(ips));
			}
			List<Sheet> sheets = getSheet(readBook);
			return getBody(sheets);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(ips);
		}

	}

	// public static Workbook create(String file) {
	// InputStream ips = null;
	// Workbook readBook = null;
	// try {
	// ips = new FileInputStream(file);
	//
	// if (!ips.markSupported()) {
	// ips = new PushbackInputStream(ips, 8);
	// }
	// if (POIFSFileSystem.hasPOIFSHeader(ips)) {
	// readBook = new HSSFWorkbook(ips);
	// }
	// if (POIXMLDocument.hasOOXMLHeader(ips)) {
	// readBook = new XSSFWorkbook(OPCPackage.open(ips));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return readBook;
	// }

	/**
	 * 获取工作溥
	 * 
	 * @param book
	 **/
	public static List<Sheet> getSheet(Workbook book) {
		List<Sheet> result = new LinkedList<Sheet>();
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			Sheet sheet = book.getSheetAt(i);
			Row row = sheet.getRow(0);
			if (row == null) {
				continue;
			}
			Cell cell = row.getCell(0);
			if (cell == null) {
				continue;
			}
			result.add(sheet);
		}
		return result;
	}

	/**
	 * 返回表格内容
	 * 
	 * @param sheets
	 *            工作薄集合
	 **/
	public static Map<String, Map<Integer, List<String>>> getBody(
			List<Sheet> sheets) {
		Map<String, Map<Integer, List<String>>> result = new HashMap<String, Map<Integer, List<String>>>();
		for (Sheet sheet : sheets) {
			String sheetName = sheet.getSheetName();
			result.put(sheetName, new HashMap<Integer, List<String>>());
			Map<Integer, List<String>> tmp = result.get(sheetName);

			int end = sheet.getLastRowNum();
			// 循环行
			for (int start = 0; start <= end; start++) {
				tmp.put(start, new LinkedList<String>());
				List<String> celDatas = tmp.get(start);
				Row row = sheet.getRow(start);
				// 循环列
				for (int c = 0; c < row.getLastCellNum(); c++) {
					celDatas.add(getCellContent(row, c));
				}

			}
		}
		return result;
	}

	// ///////////////////
	public static void clone(String readFile, String outFile,
			CloneBookCallBack callBack) {
		InputStream ips = null;
		OutputStream ops = null;
		// Workbook outBook = null;
		Workbook readBook = null;
		File check = new File(outFile);
		if (!check.exists()) {
			check.getParentFile().mkdirs();
		}
		File checkOoutFile = new File(outFile);
		if (!checkOoutFile.exists()) {
			checkOoutFile.getParentFile().mkdirs();
		}
		try {
			ips = new FileInputStream(readFile);
			ops = new FileOutputStream(outFile);
			// outBook = new HSSFWorkbook();

			if (!ips.markSupported()) {
				ips = new PushbackInputStream(ips, 8);
			}

			if (POIFSFileSystem.hasPOIFSHeader(ips)) {
				readBook = new HSSFWorkbook(ips);
			}
			if (POIXMLDocument.hasOOXMLHeader(ips)) {
				readBook = new XSSFWorkbook(OPCPackage.open(ips));
			}

			if (callBack != null)
				callBack.run(readBook);

			readBook.write(ops);
			ops.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			close(ips);
			close(ops);
		}

		// return outBook;
	}

	public static interface CloneBookCallBack {
		void run(Workbook readBook);
	}

	// 内部方法

	private static void close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getCellContent(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null) {
			return null;
		}
		return getCellContent(cell);
	}

	private static String getCellContent(Cell cell) {

		if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		if (cell.getStringCellValue() == null)
			return null;

		return cell.getStringCellValue().trim();
	}

}
