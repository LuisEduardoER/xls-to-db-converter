package readers;

import java.io.FileInputStream;
import java.io.IOException; //import java.util.LinkedList;

import model.SheetLine;

import org.apache.poi.ss.usermodel.*;

import exceptions.UnsupportedFormatOfInputFileException;

enum Cells {
	CELL0(Cell.CELL_TYPE_STRING), CELL1(Cell.CELL_TYPE_STRING), CELL2(
			Cell.CELL_TYPE_STRING), CELL3(Cell.CELL_TYPE_STRING), CELL4(
			Cell.CELL_TYPE_NUMERIC), CELL5(Cell.CELL_TYPE_NUMERIC), CELL6(
			Cell.CELL_TYPE_NUMERIC), CELL7(Cell.CELL_TYPE_STRING);

	private int type;

	Cells(int t) {
		type = t;
	}

	public int gettType() {
		return type;
	}
}

public class WorkbookReader {

	private final int NUM_OF_CELLS = 8;

	protected Workbook wb;
	protected Sheet sheet;
	protected FileInputStream fileIn;

	WorkbookReader(String filename) throws IOException {
		fileIn = new FileInputStream(filename);
	}

	public SheetLine getLine(int rowNum)
			throws UnsupportedFormatOfInputFileException {

		SheetLine sheetLine = new SheetLine();

		Row row = sheet.getRow(rowNum);
		Cell cell;
		int cellNum = 0;
		String mark = null;

		try {
			sheetLine.setRow(rowNum);

			sheetLine.setNumber((String) readCell(row, cellNum++));
			sheetLine.setType((String) readCell(row, cellNum++));
			sheetLine.setName((String) readCell(row, cellNum++));
			sheetLine.setAddress((String) readCell(row, cellNum++));
			sheetLine.setUnp((Integer) readCell(row, cellNum++));
			sheetLine.setOkpo((Long) readCell(row, cellNum++));
			sheetLine.setAccount((Long) readCell(row, cellNum++));

			mark = (String) readCell(row, cellNum);
			if (mark.compareTo("с"/* rus */) == 0
					|| mark.compareTo("С"/* RUS */) == 0
					|| mark.compareTo("c"/* eng */) == 0
					|| mark.compareTo("C"/* ENG */) == 0)
				sheetLine.setNets(true);
			else
				sheetLine.setNets(false);
		}
		// TODO Обработка исключений
		catch (Throwable e) {
			e.printStackTrace();
			throw new UnsupportedFormatOfInputFileException();
		}

		return sheetLine;

	}

	private Object readCell(Row row, int cellNum) {
		Object ob = new Object();
		Cell cell = row.getCell(cellNum);

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			ob = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			ob = cell.getNumericCellValue();
			break;
		}

		return ob;
	}
	/*
	 * public LinkedList fillList() { LinkedList }
	 */
}
