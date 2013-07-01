package exceptions;

import java.io.FileNotFoundException;

public class InputFileNotFoundException extends FileNotFoundException{
	
	private static final long serialVersionUID = 1L;
	private String unfFilename;
	
	public InputFileNotFoundException(String filename) {
		unfFilename = filename;
	}
	
	public String toString() {
		return "Невозможно найти файл с именем " + unfFilename;
	}

}
