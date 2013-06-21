package by.ericpol.xls_to_db_converter.exceptions;

public class UnsupportedExtOfInputFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public String toString() {
		return "Расширение входного файла не поддерживается";
	}

}
