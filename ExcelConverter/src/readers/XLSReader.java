package readers;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XLSReader extends WorkbookReader {

	public XLSReader(String filename) throws IOException {
		super(filename);

		wb = new HSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
