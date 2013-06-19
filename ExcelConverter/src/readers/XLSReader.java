package readers;

import java.io.IOException;

import model.Report;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
