package web.jdbc;

//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Properties;

import logic.entity.Report;
import logic.entity.SheetLine;
import logic.exceptions.DataBaseException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;



/**
 * Класс для отправки извлечённых из Excel-файла данных в БД
 * 
 * @throws DataBaseException
 *             генерируется при возникающих ошибках при SQL-запросах, создании
 *             файла отчёта и др.
 */
public class DBWorker {

	private String url; 					// Переменные для информации для подключения к БД
	private String driver;
	private String user;
	private String password;
	private String useUnicode;
	private String characterEncoding;
	private String dbTableOfOrg;	
	private String dbTableOfOrgType;
	
	//private String dbID;					// Переменные для обращения к полям БД
	private String dbNum;
	private String dbType;
	private String dbName;
	private String dbAddress;
	private String dbUnp;
	private String dbOkpo;
	private String dbAccount;
	private String dbIsNet;
	
	private String dbIDType;
	private String dbTitle;
	//private String dbFullname;
	
	private Formatter 	  formatter;
	
	private Connection connection; 			// Ссылки на объекты для подключения к БД
	private Statement  statement;
	private ResultSet  resultSet;
	private Properties propertiesForConnect;

	private String[] typeOrgStr; 			// Для хранения типов организаций (ООО, ИП, ЧУП...)
	private long[] typeOrgId; 				// и их ID в БД (1, 2, 3...)

	private final int  MAX_UNP = 999999999; 		// Минимальные и максимальные значения УНП, ОКПО, Р/с
	private final int  MIN_UNP = 100000000;
	private final long MAX_OKPO = 999999999999l;
	private final long MIN_OKPO = 100000000000l;
	private final long MAX_ACCOUNT = 9999999999999l;
	private final long MIN_ACCOUNT = 1000000000000l;

	/**
	 * Стандартный конструктор данные для подключения берутся из файла
	 * database.properties
	 * 
	 * @throws DataBaseException
	 *             генерируется при ошибке открытия или внутренней структуры
	 *             файла database.properties
	 */
	public DBWorker() throws DataBaseException {
		try {
			InputStream in;
			in = ClassLoader.getSystemClassLoader().getResourceAsStream("database.properties");
			Properties properties = new Properties();
			properties.load(in);

			this.url 				= properties.getProperty("db.url") + "/" + properties.getProperty("db.schema");
			this.driver 			= properties.getProperty("db.driver");
			this.user 				= properties.getProperty("db.user");
			this.password 			= properties.getProperty("db.password");
			this.useUnicode 		= properties.getProperty("db.useUnicode");
			this.characterEncoding 	= properties.getProperty("db.characterEncoding");
			this.dbTableOfOrg 		= properties.getProperty("db.dbTableOfOrg");
			this.dbTableOfOrgType 	= properties.getProperty("db.dbTableOfOrgType");
			
			//this.dbID				= properties.getProperty("db.Org.id");			
			this.dbNum				= properties.getProperty("db.Org.num");
			this.dbType				= properties.getProperty("db.Org.type");
			this.dbName				= properties.getProperty("db.Org.name");
			this.dbAddress			= properties.getProperty("db.Org.address");
			this.dbUnp				= properties.getProperty("db.Org.unp");
			this.dbOkpo				= properties.getProperty("db.Org.okpo");
			this.dbAccount			= properties.getProperty("db.Org.account");
			this.dbIsNet			= properties.getProperty("db.Org.isNet");
			
			this.dbIDType			= properties.getProperty("db.OrgType.idType");
			this.dbTitle			= properties.getProperty("db.OrgType.title");
			//this.dbFullname		= properties.getProperty("db.OrgType.fullname");
					
		} catch (FileNotFoundException exception) {
			throw new DataBaseException("Не найден файл database.properties", exception);
		} catch (IOException exception) {
			throw new DataBaseException("Ошибка ввода/вывода в файле database.properties", exception);
		}

		try {
			propertiesForConnect = new Properties();
			propertiesForConnect.setProperty("user", user);
			propertiesForConnect.setProperty("password", password);
			propertiesForConnect.setProperty("useUnicode", useUnicode);
			propertiesForConnect.setProperty("characterEncoding", characterEncoding);
		} catch (NullPointerException exception) {
			throw new DataBaseException("Ошибка данных файла database.properties", exception);
		}
		
	}

	/**
	 * Попытка отключения от БД (если отключение не было произведено) при
	 * удалении объекта сборщиком мусора.
	 */
	protected void finalize() {
		try {
			disconnect();
		} catch (DataBaseException exception) {
			System.out.println("Ошибка закрытия соединения в методе finalize");
		}
	}

	/**
	 * Выполнение подключения к БД, настройки к которой задаются при создании
	 * объекта класса <code>DBWorker</code>
	 * 
	 * @throws DataBaseException
	 *             генерируется в случае ошибки создания объектов класса и
	 *             ошибок при формировании SQL-запросов
	 */
	public void сonnect() throws DataBaseException {
		try {
			Class.forName(driver); // Регистрируем драйвер
			connection = (Connection) DriverManager.getConnection(url, propertiesForConnect); 	// Выполняем подключение к БД
			statement  = (Statement)  connection.createStatement();
			
			resultSet = statement.executeQuery("SET character_set_client='utf8'");				// Настраиваем MySQL на получение данных в utf8
			resultSet = statement.executeQuery("SET character_set_results='utf8'");				// Настраиваем MySQL на возврат данных в utf8
			resultSet = statement.executeQuery("SET collation_connection='utf8_general_ci'");

			// Определяем все возможные типы организаций
			resultSet = statement.executeQuery("SELECT COUNT(" + dbIDType + 					// Количество разных типов организаций
											   ") FROM " + dbTableOfOrgType); 					// получаем из 2 таблицы
			resultSet.next();
			
			typeOrgStr = new String[resultSet.getInt(1)];	// Массив для хранения полученных сокращений организаций (ООО, ЧУП, ИП...)
			typeOrgId  = new long  [resultSet.getInt(1)];	// Массив для хранения ID в БД полученных сокращений организаций (1, 2, 3...)

			// TODO следующая строка только для отладки
			System.out.println("Все извлечённые из БД типы организаций:");
			formatter = new Formatter();
			formatter.format("SELECT %s, %s FROM %s", dbTitle, dbType, dbTableOfOrgType);
			resultSet = statement.executeQuery(formatter.toString()); 	// Получаем все типы оргнанизации из 2 таблицы
			for (int i = 0; resultSet.next(); i++) {
				typeOrgStr[i] = resultSet.getString(1);
				typeOrgId[i]  = resultSet.getLong(2);
				// TODO следующая строка только для отладки
				System.out.println(resultSet.getString(1) + " " + resultSet.getLong(2));
			}
			// TODO следующая строка только для отладки
			System.out.println("---------------------------------------");
		} catch (ClassNotFoundException e) {
			throw new DataBaseException("Ошибка создания объекта класса", e);
		} catch (SQLException e) {
			throw new DataBaseException("Ошибка SQL-запроса", e);
		}
	}

	/**
	 * Отключение от БД (закрытие соединения)
	 * 
	 * @throws DataBaseException
	 *             генерируется при ошибки закрытия соединения
	 */
	public void disconnect() throws DataBaseException {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			throw new DataBaseException("Ошибка закрытия соединения", e);
		}
	}

	/**
	 * Метод вставляет в указанную таблицу одну строку об указанной организации
	 * 
	 * @param number
	 *            № организации
	 * @param type
	 *            тип организации (ООО, ИП, ЧУП...)
	 * @param name
	 *            имя организации
	 * @param address
	 *            адрес органиации
	 * @param unp
	 *            УНП организации
	 * @param okpo
	 *            ОКПО организации
	 * @param account
	 *            Р/с организации
	 * @param isNet
	 *            сетевая организация
	 * 
	 * @return Код завершения:<br>
	 *         0 - запись добавлена<br>
	 *         1 - запись не добавлена (тип организации отсутсвует в БД)<br>
	 *         2 - запись не добавлена (несоответсвие параметров длине)<br>
	 *         3 - запись не добавлена (неизвестная ошибка)<br>
	 *         4 - запись не добавлена (организация уже присутствует в БД)<br>
	 *         5 - запись не добавлена (не прошёл запрос на проверку
	 *         уникальности данной организации)
	 */
	private int insert(String number, String type, String name, String address,
			String unp, String okpo, String account, boolean isNet) {

		int typeOrg = -1; 		// Хранение ID типа организации (индекса массива typeOrgId)

		for (int i = 0; i < typeOrgId.length; i++)
			// Поиск ID типа организации
			if (typeOrgStr[i].compareTo(type) == 0) {
				typeOrg = i;
				break;
			}

		if (typeOrg == -1) // Тип не найден
			return 1;

		try {
			formatter = new Formatter();
			formatter.format("SELECT COUNT(*) FROM %s WHERE %s = \"%s\" AND %s = %d AND %s = \"%s\" " +
							 "AND %s = \"%s\" AND %s = %s AND %s = %s AND %s = %s AND %s = %b", 
							 dbTableOfOrg, dbNum, number, dbType, typeOrgId[typeOrg], dbName, name,
							 dbAddress, address, dbUnp, unp, dbOkpo, okpo, dbAccount, account,
							 dbIsNet, isNet);
			// TODO следующая строка только для отдаки
			System.out.println("Выполняем запрос на проверку уникальности: " + formatter.toString());
			resultSet = statement.executeQuery(formatter.toString());
			resultSet.next();
			if (resultSet.getInt(1) > 0) 					// Если в БД уже такая организация
				return 4;
		} catch (SQLException e) { 							// Если запрос на выборку неккорректен
			return 5;
		}

		formatter = new Formatter();																// Добавление в таблицу типа органицзации с помощью SQL-запроса INSERT
		formatter.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (\"%s\", " +
						 "%s, \"%s\", \"%s\", %s, %s, %s, %s)", dbTableOfOrg, dbNum,
						 dbType, dbName, dbAddress, dbUnp, dbOkpo, dbAccount, dbIsNet, number,
						 typeOrgId[typeOrg], name, address, unp, okpo, account, isNet);
		
		// TODO следующая строка только для отладки
		System.out.println("Выполняем запрос на добавление: " + formatter.toString());

		try {
			statement.executeUpdate(formatter.toString());
		} catch (SQLException e) {
			if (e.getErrorCode() == 1406) { // Несоответствие по длине
				// TODO следующая строка только для отладки
				System.out.println("Иcключение: несоответствие параметра по длине");
				return 2;
			}
			// TODO следующая строка только для отладки
			System.out.println("Исключение (неизвестная ошибка): getErrorCode = " + e.getErrorCode());
			return 3;
		}
		return 0; // Успешное завершение (запись добавлена)
	}

	/**
	 * Метод для сохранения в подключённой БД записей, прочитанных из Excel
	 * файла и помещённых в коллекцию.
	 * 
	 * @param sl
	 *            коллекция объектов с разобранными строками файла
	 * @param report
	 *            объект для работы с файлом отчёта
	 * @throws IOException
	 */
	public void sendToDB(LinkedList<SheetLine> sl, Report report) throws IOException {

		report.writeln("Обнаружено записей: " + sl.size());
		int success = 0; // Успешно добавлено в БД

		for (int i = 0; i < sl.size(); i++) {
			SheetLine sheetLine = sl.get(i);
			if (sheetLine.getUnp() > MAX_UNP || sheetLine.getUnp() < MIN_UNP) {
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. УНП вне допустимого диапазона.");
				continue;
			}
			if (sheetLine.getOkpo() > MAX_OKPO || sheetLine.getOkpo() < MIN_OKPO) {
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. ОКПО вне допустимого диапазона.");
				continue;
			}
			if (sheetLine.getAccount() > MAX_ACCOUNT || sheetLine.getAccount() < MIN_ACCOUNT) {
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Слишком длинный или отрицательный расчётный счёт.");
				continue;
			}

			switch (insert(sheetLine.getNumber(), sheetLine.getType(),
					sheetLine.getName(), sheetLine.getAddress(),
					String.valueOf(sheetLine.getUnp()),
					String.valueOf(sheetLine.getOkpo()),
					String.valueOf(sheetLine.getAccount()), sheetLine.isNets())) {
			case 0:
				success++;
				break;
			case 1:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Указанный тип организации отсутствует в БД");
				break;
			case 2:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Параметры не соответсвуют по длине");
				break;
			case 3:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Неизвестная ошибка");
				break;
			case 4:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Такая организация уже есть в БД");
				break;
			case 5:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Не прошёл SQL-запрос на проверку уникальности данной организации");
				break;
			default:
				report.writeln("ОШИБКА. Строка " + sheetLine.getRow()
						+ " не добавлена в БД. Ошибка программирования. Если вы видете эту надпись, значит ошибся программист");
				break;
			}
		}

		report.writeln("Успешно добавлено записей: " + success);
		// TODO: Правильный расчет недобавленных записей
		report.writeln("Не добавлено записей: " + (sl.size() - success));

	}
}
