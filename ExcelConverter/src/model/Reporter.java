package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Класс для создания файла отчёта и вывода в него информации
 * 
 */
public class Reporter {
	File     	file;	
	PrintWriter out;
	
	/**
	 * Создание файла отчёта на диске<br> 
	 * TODO доработать обработку исключений
	 * 
	 * @param reportFilename имя создаваемого файла
	 * @throws IOException генерируется при ошибке создания файла
	 */
	public void createFile(String reportFilename) throws IOException {
		try {
			file = new File(reportFilename);
			if (!file.exists())								// Проверяем, если файл не существует,
				file.createNewFile();						// то создаём его
			
			out = new PrintWriter(file.getAbsoluteFile()); 	// PrintWriter обеспечит возможности записи в файл
		} 
		catch (IOException exception) {
			/**
			 * TODO Для отладки.
			 */
			System.out.println("Ошибка создания файла");
			throw exception;
		}
	}
	
	/**
	 * Закрытие файла и сохранение в нём изменений
	 */
	public void closeFile() {
		out.close();
		out = null;
	}
	
	/**
	 * Вывод в файл текстовой строки, заканчивающейся переводом каретки
	 * 
	 * @param text выводимая строка
	 */
	public void writeln(String text) {
		out.println(text);
	}
	
	/**
	 * Вывод в файл текстовой строки
	 * 
	 * @param text выводимая строка
	 */
	public void write(String text) {
		out.print(text);
	}
	
	/**
	 * Закрытие файла (если он не был закрыт) при удалении объекта сборщиком мусора.
	 */
	void finaly(){
		if (out != null)
			closeFile();
	}
}
