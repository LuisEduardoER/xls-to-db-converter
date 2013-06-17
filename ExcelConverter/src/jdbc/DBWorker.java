package jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import exceptions.DataBaseException;

import model.SheetLine;

/**
 * Класс для отправки извлечённых из Excel файла данных в БД
 * 
 * @throws DataBaseException генерируется при возникающих ошибках при SQL-запросах, создании файла отчёта и др.
 */
public class DBWorker {

	private String url;					// Переменные для информации для подключения к БД
	private String driver;
	private String user;
	private String password;
	private String useUnicode;
	private String characterEncoding;
	
	private Connection connection;		// Ссылки на объекты для подключения к БД
	private Statement  statement;
	private ResultSet  resultSet;
	private Properties propertiesForConnect; 
		
	private String [] typeOrgStr;		// Для хранения типов организаций (ООО, ИП, ЧУП...)
	private long   [] typeOrgId;		// и их ID в БД (1, 2, 3...)
	
	/**
	 * Стандартный конструктор данные для подключения берутся из файла database.properties
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public DBWorker() throws FileNotFoundException, IOException{
		Properties properties = new Properties();
		properties.load(new FileInputStream("resources/database.properties"));
		
		this.url    			= properties.getProperty("db.url") + "/" + properties.getProperty("db.schema");
		this.driver 			= properties.getProperty("db.driver");
		this.user   			= properties.getProperty("db.user");
		this.password 			= properties.getProperty("db.password");
		this.useUnicode 		= properties.getProperty("db.useUnicode");
		this.characterEncoding	= properties.getProperty("db.characterEncoding");
		
		propertiesForConnect = new Properties();
		propertiesForConnect.setProperty("user", user);
		propertiesForConnect.setProperty("password", password);
		propertiesForConnect.setProperty("useUnicode", useUnicode);
		propertiesForConnect.setProperty("characterEncoding", characterEncoding);
	}
		
	void finaly() {
		try {
			disconnect();
		}
		catch (DataBaseException exception){
			System.out.println("Ошибка закрытия соединения в методе finaly");
		}
	}
	
	/**
	 * Выполнение подключения к БД, настройки к которой задаются при создании объекта класса <code>DBWorker</code>
	 * @throws DataBaseException генерируется в случае ошибки создания объектов класса и ошибок при формировании SQL-запросов
	 */
	public void сonnect() throws DataBaseException {
		try {
            Class.forName(driver);																// Регистрируем драйвер
            connection = (Connection) DriverManager.getConnection(url, propertiesForConnect);	// Выполняем подключение к БД
            statement  = (Statement) connection.createStatement();
                     
            resultSet = statement.executeQuery("set character_set_client='utf8'");				// Настраиваем MySQL на получение данных в utf8 
            resultSet = statement.executeQuery("set character_set_results='utf8'");				// Настраиваем MySQL на возврат данных в utf8
            resultSet = statement.executeQuery("set collation_connection='utf8_general_ci'");
                 
            // Определяем все возможные типы организаций
            resultSet = statement.executeQuery("SELECT COUNT(type) FROM OrgType");				// Количество разных типов организаций получаем из 2 таблицы
    		resultSet.next();
               		
    		typeOrgStr = new String[resultSet.getInt(1)];										// Массив для хранения полученных сокращений организаций (ООО, ЧУП, ИП...) 
    		typeOrgId  = new long[resultSet.getInt(1)];											// Массив для хранения ID в БД полученных сокращений организаций (1, 2, 3...)
            
            resultSet = statement.executeQuery("SELECT title, type FROM OrgType");				// Получаем все типы оргнанизации из 2 таблицы
    		for (int i = 0; resultSet.next(); i++){
    			typeOrgStr[i] = resultSet.getString(1);
    			typeOrgId[i]  = resultSet.getLong(2);
    			/**
    			 * TODO только для отладки
    			 */
    			System.out.println(resultSet.getString(1) + " " + resultSet.getLong(2));
    		}
    	} 
		catch (ClassNotFoundException exception) {
            throw new DataBaseException("Ошибка создания объекта класса", exception);
		}
		catch (SQLException exception) {
			throw new DataBaseException("Ошибка SQL-запроса", exception);
		}
	}
	
	/**
	 * Отключение от БД (закрытие соединения)
	 * @throws DataBaseException генерируется при ошибки закрытия соединения
	 */
	public void disconnect() throws DataBaseException {
		try {
            if (connection != null){
            	connection.close();
            	connection = null;
            }
        }
		catch (SQLException exception) {
			throw new DataBaseException("Ошибка закрытия соединения", exception);
		}
	}
	
	/**
	 * Метод вставляет в указанную таблицу одну строку об указанной организации
	 * 
	 * @param table имя таблицы в БД
	 * @param number № организации
	 * @param type тип организации (ООО, ИП, ЧУП...)
	 * @param name имя организации
	 * @param adress адрес органиации
	 * @param unp УНП организации
	 * @param okpo ОКПО организации
	 * @param account Р/с организации
	 * @param isNet сетевая организация
	 * 
	 * @return Код завершения:<br>  
	 * 				0 - запись добавлена<br>
	 * 				1 - запись не добавлена (тип организации отсутсвует в БД)<br>
	 * 				2 - запись не добавлена (несоответсвие параметров длине)<br>
	 * 				3 - запись не добавлена (неизвестная ошибка)
	 */
	private int insert(String table, String number, String type, String name, String adress, String unp, String okpo, String account, boolean isNet) {
		
		int typeOrg = -1;								// Хранение ID типа организации 
		
		for (int i = 0; i < typeOrgId.length; i++)		// Поиск ID типа организации 
			if (typeOrgStr[i].compareTo(type) == 0) {
				typeOrg = i;
				break;
			}
		
		if (typeOrg == -1)								// Тип не найден
			return 1;
		
		String insertQuerry;							// Добавление в таблицу типа органицзации с помощью SQL запроса INSERT
		insertQuerry = "INSERT INTO " + table + " (number, type, name, adress, unp, okpo, account, isNet) VALUES (\"" + number + "\", \"" + typeOrgId[typeOrg] + "\", \"" + name + "\", \"" + adress + "\", " + unp + ", " + okpo + ", " + account + ", " + isNet + ")";  
				
		/**
		 * TODO только для отладки
		 */
		System.out.println(insertQuerry);
		
		try {
			statement.executeUpdate(insertQuerry);
		}
		catch (SQLException e) {
			if (e.getErrorCode() == 1406){				// Несоответствие по длине
				/**
				 * TODO только для отладки
				 */
				System.out.println("Иcключение: несоответствие параметра по длине");
				return 2;
			}
			/**
			 * TODO только для отладки
			 */
			System.out.println("Исключение (неизвестная ошибка): getErrorCode = " + e.getErrorCode());
			return 3;
		}
		return 0;										// Успешное завершение (запись добавлена)
	}

	/**
	 * Метод для сохранения в подключённой БД записей, прочитанных из Excel файла и помещённых в коллекцию.
	 * Метод также генерирует файл отчёта.
	 * 
	 * @param tablename	имя таблицы БД
	 * @param sl коллекция объектов с разобранными строками файла
	 * @param filenameReport имя создаваемого файла отчёта
	 * @throws DataBaseException генерируется в случае ошибки создания файла отчёта
	 */
	public void sendToDB(String tablename, LinkedList<SheetLine> sl, String filenameReport) throws DataBaseException{
		File     	file;	
		PrintWriter out;
		
		try {
			file = new File(filenameReport);
			if (!file.exists())								// Проверяем, если файл не существует,
				file.createNewFile();						// то создаём его
			
			out = new PrintWriter(file.getAbsoluteFile()); 	// PrintWriter обеспечит возможности записи в файл
		} 
		catch (IOException exception) {
			/**
			 * TODO Для отладки
			 */
			System.out.println("Ошибка создания файла");
			throw new DataBaseException("Ошибка создания файла отчёта. Сохранение в БД не выполнено.", exception);
		}
		
		out.println("Обнаружено записей в файле: " + sl.size());
		int success = 0;									// Успешно добавлено в БД 
		
		for (int i = 0; i < sl.size(); i++){
			SheetLine sheetLine = sl.get(i);
			if (sheetLine.getUnp() > 999999999 || sheetLine.getUnp() < 0){
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Слишком длинный или отрицательный UNP.");
				continue;
			}
			if (sheetLine.getOkpo() > 999999999999l || sheetLine.getOkpo() < 0){
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Слишком длинный или отрицательный OKPO.");
				continue;
			}
			if (sheetLine.getAccount() > 9999999999999l || sheetLine.getAccount() < 0){
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Слишком длинный или отрицательный расчётный счёт.");
				continue;
			}
			
			switch (insert(tablename, sheetLine.getNumber(), sheetLine.getType(), sheetLine.getName(), sheetLine.getAddress(), 
					String.valueOf(sheetLine.getUnp()), String.valueOf(sheetLine.getOkpo()), String.valueOf(sheetLine.getAccount()), sheetLine.isNets())){
			case 0:
				success++;
				break;
			case 1:
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Указанный тип организации отсутсвует в БД");
				break;
			case 2:
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Параметры не соответсвуют по длине");
				break;
			case 3:
				out.println("ОШИБКА. Строка " + sheetLine.getRow() + " не добавлена в БД. Неуточнённая ошибка");
				break;	
			}
		}
		
		out.print("Успешно добавлено записей: " + success);
		out.close();
				
	}
}
