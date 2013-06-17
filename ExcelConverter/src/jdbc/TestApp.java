package jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Random;

import exceptions.DataBaseException;
import model.SheetLine;

public class TestApp {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String orgType[] = {"ООО", "ИП", "ЧУП", "ОАО", "ИООО"};
		Random rand = new Random();
		LinkedList <SheetLine> l = new LinkedList<SheetLine>();
		SheetLine l4[] = new SheetLine[10];
		int num = 1;
		for (SheetLine sl: l4){
			sl = new SheetLine();
			sl.setNumber("#" + rand.nextInt(10000000));
			sl.setRow(num++);
			sl.setType(orgType[rand.nextInt(5)]);
			sl.setName("ФАБРИКА " + rand.nextInt(1000));
			sl.setAddress("ул.№ " + rand.nextInt(1000));
			sl.setUnp(rand.nextInt(1000000000));
			sl.setOkpo(rand.nextInt(1000000000));
			sl.setAccount(rand.nextInt(1000000000));
			sl.setNets(true);
			l.add(sl);
		}
		
	
		
		DBWorker db;
		db = new DBWorker();
		try {
			db.сonnect();
			db.sendToDB("Org", l, "report.txt");
			db.disconnect();
		}
		catch (DataBaseException exception){
			System.out.println(exception.getMessage());
		}
	}

}
