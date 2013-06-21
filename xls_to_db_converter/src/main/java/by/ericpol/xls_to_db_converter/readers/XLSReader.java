package by.ericpol.xls_to_db_converter.readers;

import java.io.IOException;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import by.ericpol.xls_to_db_converter.entity.Report;
import by.ericpol.xls_to_db_converter.exceptions.InputFileNotFoundException;


public class XLSReader extends WorkbookReader {

	public XLSReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new HSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
