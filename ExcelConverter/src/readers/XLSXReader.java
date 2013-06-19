package readers;

import java.io.IOException;

import model.Report;

import org.apache.poi.xssf.usermodel.*;

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
