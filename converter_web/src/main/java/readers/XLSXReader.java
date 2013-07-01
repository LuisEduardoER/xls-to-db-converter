package readers;

import java.io.IOException;


import org.apache.poi.xssf.usermodel.*;

import entity.Report;
import exceptions.InputFileNotFoundException;


public class XLSXReader extends WorkbookReader {

	public XLSXReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new XSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);

	}

}
