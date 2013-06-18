package jdbc;

import java.util.LinkedList;
import java.util.Random;

import exceptions.DataBaseException;
import model.Reporter;
import model.SheetLine;

public class TestApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String 					orgType[] 	= {"ООО", "ИП", "ЧУП", "ОАО", "ИООО"};
		String					street[]	= {"Пушкинская ", "Московская ", "Мичурина ", "Гоголя ", "Ленина ", "Советская "};
		Random 					rand 		= new Random();
		LinkedList <SheetLine> 	l 			= new LinkedList<SheetLine>();
		SheetLine 				l4[] 		= new SheetLine[10];
		SheetLine				l1;						// Для теста добавления дублируемых организаций
		int 					num 		= 1;
		
		for (SheetLine sl: l4){
			sl = new SheetLine();
			sl.setNumber	("#" + rand.nextInt(10000000));
			sl.setRow		(num++);
			sl.setType		(orgType[rand.nextInt(5)]);
			sl.setName		("Организация " + rand.nextInt(1000));
			sl.setAddress	(street[rand.nextInt(6)] + rand.nextInt(300));
			sl.setUnp		(rand.nextInt(1000000000));
			sl.setOkpo		(rand.nextInt(1000000000));
			sl.setAccount	(rand.nextInt(1000000000));
			sl.setNets		(rand.nextInt(2)==1?true:false);
			l.add(sl);
		}
		
		l1 = new SheetLine();
		l1.setNumber	("#11111");
		l1.setRow		(num++);
		l1.setType		("ООО");
		l1.setName		("Повтор incorporation");
		l1.setAddress	("ул. Пушкинская 1");
		l1.setUnp		(10101010);
		l1.setOkpo		(2020202020l);
		l1.setAccount	(3030303030l);
		l1.setNets		(false);
		l.add(l1);
		
		/**
		 * TODO
		 * Пример работы с файлом
		 */
		Reporter r = new Reporter();
		r.createFile("report.txt");
		
		
		
		
		/**
		 * TODO
		 * пример работы с DBWorker
		 */
		DBWorker db;
		try {
			db = new DBWorker();
			db.сonnect();
			db.sendToDB(l, r);
			db.disconnect();
		}
		catch (DataBaseException exception){
			System.out.println("Сработало исключение DataBaseException. Программа остановлена.");
			System.out.println("Причина: " + exception.getMessage());
		}
		
		r.closeFile();
	}

}
