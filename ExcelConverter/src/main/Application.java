package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import jdbc.DBWorker;

import readers.WorkbookReader;
import readers.XLSReader;
import readers.XLSXReader;

import model.SheetLine;

import exceptions.*;

public class Application {
	private static String filename;
	private static LinkedList<SheetLine> list;

	public static void main(String[] args) throws IOException {
		try {
			AppSlave.commandLineArgumentsTester(args);
		} catch (CommandLineArgumentException e) {
			System.out.println(e);
			return;
		}

		WorkbookReader wbr = null;
		filename = args[0];

		try {
			switch (AppSlave.extAnalyzer(filename)) {
			case XLS:
				wbr = new XLSReader(filename);
				break;
			case XLSX:
				wbr = new XLSXReader(filename);
				break;
			}
		} catch (UnsupportedExtOfInputFileException e) {
			System.out.println(e);
			return;
		} catch (FileNotFoundException e) {
			System.out.println(e);
			return;
		}

		list = wbr.fillList();
		Iterator<SheetLine> iter = null;

		try {
			iter = list.iterator();
		} catch (NullPointerException e) {
			System.out.println("Не удалось заполнить LinkedList");
			return;
		}
		while (iter.hasNext()) {
			SheetLine el = iter.next();
			System.out.println(el);
		}
		
		/*try {
			DBWorker db = new DBWorker();
			db.сonnect();
			db.sendToDB("Org", list, "report.txt");
			db.disconnect();
		}
		catch (DataBaseException e){
			System.out.println(e.getMessage());			
		}*/
	}

}
