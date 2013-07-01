package logic.readers;

import java.io.IOException;

import logic.entity.Report;
import logic.exceptions.InputFileNotFoundException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class XLSReader extends WorkbookReader {

	public XLSReader(String filename, Report r)
			throws InputFileNotFoundException, IOException {
		super(filename, r);

		wb = new HSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
