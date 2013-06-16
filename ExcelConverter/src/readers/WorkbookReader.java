package readers;

import java.io.FileInputStream;
import java.io.IOException;

import model.SheetLine;

import org.apache.poi.ss.usermodel.*;

public class WorkbookReader {

	protected Workbook wb;
	protected Sheet sheet;
	protected FileInputStream fileIn;

	WorkbookReader(String filename) throws IOException {
		fileIn = new FileInputStream(filename);
	}

	public SheetLine getLine(int rowNum) {

		SheetLine sheetLine = new SheetLine();

		Row row = sheet.getRow(rowNum);
		Cell cell;

		cell = row.getCell(0);
		sheetLine.setNumber(cell.getStringCellValue());

		cell = row.getCell(1);
		sheetLine.setType(cell.getStringCellValue());

		cell = row.getCell(2);
		sheetLine.setName(cell.getStringCellValue());

		cell = row.getCell(3);
		sheetLine.setAddress(cell.getStringCellValue());

		cell = row.getCell(4);
		sheetLine.setUnp((int) cell.getNumericCellValue());

		cell = row.getCell(5);
		sheetLine.setOkpo((long) cell.getNumericCellValue());

		cell = row.getCell(6);
		sheetLine.setAccount((long) cell.getNumericCellValue());

		cell = row.getCell(7);
		if (cell.getStringCellValue().compareTo("с") == 0)
			sheetLine.setNets(true);
		else
			sheetLine.setNets(false);

		return sheetLine;

	}

	/*
	 * private void readCell(Cell cell) {
	 * 
	 * }
	 */

}
