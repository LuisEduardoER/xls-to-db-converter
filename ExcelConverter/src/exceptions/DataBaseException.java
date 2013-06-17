package exceptions;

/**
 * Исключение генерируемое при работе с объектом класса <code>DBWorker</code>
 * 
 * @author Андрей В
 */
public class DataBaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DataBaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DataBaseException(String message){
		super(message);
	}
		
}
