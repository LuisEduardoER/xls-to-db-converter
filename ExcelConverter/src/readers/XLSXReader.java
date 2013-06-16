package readers;

import java.io.IOException;
import org.apache.poi.xssf.usermodel.*;

public class XLSXReader extends WorkbookReader {

	public XLSXReader(String filename) throws IOException {
		super(filename);

		wb = new XSSFWorkbook(fileIn);

		fileIn.close();
		sheet = wb.getSheetAt(0);
	}

}
