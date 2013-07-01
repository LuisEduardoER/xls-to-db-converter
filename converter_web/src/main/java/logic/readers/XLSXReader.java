package logic.readers;

import java.io.IOException;

import logic.entity.Report;
import logic.exceptions.InputFileNotFoundException;

import org.apache.poi.xssf.usermodel.*;


public class XLSXReader extends WorkbookReader {

	public XLSXReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new XSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);

	}

}
