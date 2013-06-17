package jdbc;

import java.util.LinkedList;
import java.util.Random;

import exceptions.DataBaseException;
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
		
	
		
		DBWorker db;
		try {
			db = new DBWorker();
			db.сonnect();
			db.sendToDB("Org", l, "report.txt");
			db.disconnect();
		}
		catch (DataBaseException exception){
			System.out.println("Сработало исключение DataBaseException. Программа остановлена.");
			System.out.println("Причина: " + exception.getMessage());
		}
	}

}
