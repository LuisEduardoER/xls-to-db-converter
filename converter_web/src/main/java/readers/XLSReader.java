package readers;

import java.io.IOException;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import entity.Report;
import exceptions.InputFileNotFoundException;


public class XLSReader extends WorkbookReader {

	public XLSReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new HSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
