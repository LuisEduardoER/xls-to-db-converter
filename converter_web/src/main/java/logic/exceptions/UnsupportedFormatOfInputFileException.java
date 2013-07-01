package logic.exceptions;

public class UnsupportedFormatOfInputFileException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public String toString() {
		return "Структура файла не соответствует установленной";
	}

}
