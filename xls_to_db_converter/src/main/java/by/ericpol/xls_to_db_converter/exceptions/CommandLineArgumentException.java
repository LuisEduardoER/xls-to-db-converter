package by.ericpol.xls_to_db_converter.exceptions;

public class CommandLineArgumentException extends Exception {

	private static final long serialVersionUID = 1L;

	public String toString() {
		return "Не указаны аргументы командной строки";
	}
}
