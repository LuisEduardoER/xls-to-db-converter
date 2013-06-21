package by.ericpol.xls_to_db_converter.readers;

import java.io.IOException;


import org.apache.poi.xssf.usermodel.*;

import by.ericpol.xls_to_db_converter.entity.Report;
import by.ericpol.xls_to_db_converter.exceptions.InputFileNotFoundException;


public class XLSXReader extends WorkbookReader {

	public XLSXReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new XSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);

	}

}
