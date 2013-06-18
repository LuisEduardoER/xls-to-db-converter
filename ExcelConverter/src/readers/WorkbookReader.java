package readers;

import java.io.FileInputStream;
import java.io.IOException; //import java.util.LinkedList;
import java.util.LinkedList;

import model.SheetLine;

import org.apache.poi.ss.usermodel.*;

import exceptions.UnsupportedFormatOfInputFileException;
import exceptions.WrongCellFormatException;

enum CellType {
	STRING, INT, LONG
}

enum Cells {
	CELL0(CellType.STRING), CELL1(CellType.STRING), CELL2(CellType.STRING), CELL3(
			CellType.STRING), CELL4(CellType.INT), CELL5(CellType.LONG), CELL6(
			CellType.LONG), CELL7(CellType.STRING);

	private CellType type;

	Cells(CellType t) {
		type = t;
	}

	public CellType getType() {
		return type;
	}
}

public class WorkbookReader {

	protected Workbook wb;
	protected Sheet sheet;
	protected FileInputStream fileIn;

	WorkbookReader(String filename) throws IOException {
		fileIn = new FileInputStream(filename);
	}

	private SheetLine getLine(int rowNum)
			throws UnsupportedFormatOfInputFileException,
			WrongCellFormatException {

		SheetLine sheetLine = new SheetLine();

		Row row = sheet.getRow(rowNum);
		int cellNum = 0;
		String mark = null;
		Double tmp;

		try {
			sheetLine.setRow(rowNum);

			sheetLine.setNumber((String) readCell(row, cellNum++));
			sheetLine.setType((String) readCell(row, cellNum++));
			sheetLine.setName((String) readCell(row, cellNum++));
			sheetLine.setAddress((String) readCell(row, cellNum++));
			tmp = (Double) readCell(row, cellNum++);
			sheetLine.setUnp(tmp.intValue());
			tmp = (Double) readCell(row, cellNum++);
			sheetLine.setOkpo(tmp.longValue());
			tmp = (Double) readCell(row, cellNum++);
			sheetLine.setAccount(tmp.longValue());

			if (row.getCell(cellNum) != null) {
				mark = (String) readCell(row, cellNum);
				if (mark.compareTo("с"/* rus */) == 0
						|| mark.compareTo("С"/* RUS */) == 0
						|| mark.compareTo("c"/* eng */) == 0
						|| mark.compareTo("C"/* ENG */) == 0)
					sheetLine.setNets(true);
				else
					sheetLine.setNets(false);
			} else
				sheetLine.setNets(false);
		}
		// TODO Обработка исключений
		catch (NumberFormatException e) {
			// TODO: Правильно рассчитать номер столбца и строки
			throw new WrongCellFormatException(cellNum - 1, rowNum + 1);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new UnsupportedFormatOfInputFileException();
		}

		return sheetLine;

	}

	private ReturnedCell<?> readCell(Row row, int cellNum) {
		
		Cell cell = row.getCell(cellNum);
		ReturnedCell<String> retStringCell = null;
		ReturnedCell<Double> retDoubleCell = null;
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			retStringCell = new ReturnedCell<String>();
			retStringCell.setValue(cell.getStringCellValue());
			return retStringCell;
		case Cell.CELL_TYPE_NUMERIC:
			retDoubleCell = new ReturnedCell<Double>();
			retDoubleCell.setValue(cell.getNumericCellValue());
			return retDoubleCell;
		}
		
		return null;

	}

	public LinkedList<SheetLine> fillList() {
		LinkedList<SheetLine> list = new LinkedList<SheetLine>();
		try {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				list.add(getLine(i));
			}
		} catch (WrongCellFormatException e) {
			System.out.println(e);
		} catch (UnsupportedFormatOfInputFileException e) {
			System.out.println(e);
			return null;
		}

		return list;
	}
}
