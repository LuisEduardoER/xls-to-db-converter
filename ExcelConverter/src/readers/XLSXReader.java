package readers;

import java.io.IOException;

import model.Report;

import org.apache.poi.xssf.usermodel.*;

public class XLSXReader extends WorkbookReader {

	public XLSXReader(String filename, Report r) throws IOException {
		super(filename, r);

		wb = new XSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
