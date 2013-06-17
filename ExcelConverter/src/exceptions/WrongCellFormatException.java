package exceptions;

public class WrongCellFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	private int wrongCell;
	private int wrongRow;

	public WrongCellFormatException(int wrongCell, int wrongRow) {
		this.wrongCell = wrongCell;
		this.wrongRow = wrongRow;
	}

	public String toString() {
		return "Неверный формат ячейки: столбец " + wrongCell + ", строка "
				+ wrongRow;
	}
}
