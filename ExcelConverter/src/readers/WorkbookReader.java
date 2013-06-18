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
	//private final int NUM_OF_CELLS = 8;

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
			sheetLine.setRow(rowNum + 1);

			sheetLine.setNumber((String) readCell(row, cellNum++).getValue());
			sheetLine.setType((String) readCell(row, cellNum++).getValue());
			sheetLine.setName((String) readCell(row, cellNum++).getValue());
			sheetLine.setAddress((String) readCell(row, cellNum++).getValue());
			tmp = (Double) readCell(row, cellNum++).getValue();
			sheetLine.setUnp(tmp.intValue());
			tmp = (Double) readCell(row, cellNum++).getValue();
			sheetLine.setOkpo(tmp.longValue());
			tmp = (Double) readCell(row, cellNum++).getValue();
			sheetLine.setAccount(tmp.longValue());

			if (row.getCell(cellNum) != null) {
				mark = (String) readCell(row, cellNum).getValue();
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
			throw new WrongCellFormatException(cellNum - 1, rowNum + 1);
		} /*catch (Throwable e) {
			e.printStackTrace();
			throw new UnsupportedFormatOfInputFileException();
		}*/

		return sheetLine;

	}

	private ReturnedCell<?> readCell(Row row, int cellNum)
			throws WrongCellFormatException {

		Cell cell = row.getCell(cellNum);
		ReturnedCell<String> retStringCell = null;
		ReturnedCell<Double> retDoubleCell = null;
		ReturnedCell<?> retCell = null;
		CellType type = Cells.valueOf("CELL" + cellNum).getType();

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			retStringCell = new ReturnedCell<String>();
			retStringCell.setValue(cell.getStringCellValue());
			if (type != CellType.STRING) {
				retDoubleCell = new ReturnedCell<Double>();
				try {
					retDoubleCell.setValue(Double.parseDouble(retStringCell
							.getValue()));
					retCell = retDoubleCell;
				} catch (NumberFormatException e) {
					throw new WrongCellFormatException(cellNum + 1, row
							.getRowNum() + 1);
				}
			}
			retCell = retStringCell;
			break;
		case Cell.CELL_TYPE_NUMERIC:
			retDoubleCell = new ReturnedCell<Double>();
			retDoubleCell.setValue(cell.getNumericCellValue());
			if (type != CellType.INT && type != CellType.LONG) {
				retStringCell = new ReturnedCell<String>();
				try {
					retStringCell.setValue(Double.toString(retDoubleCell
							.getValue()));
					retCell = retStringCell;
				} catch (NumberFormatException e) {
					throw new WrongCellFormatException(cellNum + 1, row
							.getRowNum() + 1);
				}
			}
			retCell = retDoubleCell;
			break;
		default:
			throw new WrongCellFormatException(cellNum + 1, row.getRowNum() + 1);
		}

		return retCell;

	}

	public LinkedList<SheetLine> fillList() {
		LinkedList<SheetLine> list = new LinkedList<SheetLine>();
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			try {
				list.add(getLine(i));
			}
			catch (WrongCellFormatException e) {
				// TODO: Write this information to report
				System.out.println(e);
			} catch (UnsupportedFormatOfInputFileException e) {
				System.out.println(e);
				return null;
			}
		}

		return list;
	}
}
