package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Reporter {
	File     	file;	
	PrintWriter out;
	
	public void createFile(String reportFilename) {
		try {
			file = new File(reportFilename);
			if (!file.exists())								// Проверяем, если файл не существует,
				file.createNewFile();						// то создаём его
			
			out = new PrintWriter(file.getAbsoluteFile()); 	// PrintWriter обеспечит возможности записи в файл
		} 
		catch (IOException exception) {
			/**
			 * TODO Для отладки. Сделать обработку исключений
			 */
			System.out.println("Ошибка создания файла");
		}
	}
	
	public void closeFile() {
		out.close();
	}
	
	public void writeln(String text) {
		out.println(text);
	}
	
	public void write(String text) {
		out.print(text);
	}
	
}
