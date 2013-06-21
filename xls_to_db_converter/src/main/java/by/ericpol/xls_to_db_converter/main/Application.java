package by.ericpol.xls_to_db_converter.main;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import by.ericpol.xls_to_db_converter.entity.Report;
import by.ericpol.xls_to_db_converter.entity.SheetLine;
import by.ericpol.xls_to_db_converter.exceptions.*;
import by.ericpol.xls_to_db_converter.jdbc.DBWorker;
import by.ericpol.xls_to_db_converter.readers.WorkbookReader;
import by.ericpol.xls_to_db_converter.readers.XLSReader;
import by.ericpol.xls_to_db_converter.readers.XLSXReader;






public class Application {
	private static String filename;
	private static LinkedList<SheetLine> list;
	private static Report report;

	public static void main(String[] args) throws IOException {
		try {
			report = new Report("report.txt");
		} catch (IOException e) {
			System.out.println("Ошибка при создании файла отчета");
			return;
		}

		try {
			AppSlave.commandLineArgumentsTester(args);
		} catch (CommandLineArgumentException e) {
			System.out.println(e);
			report.close();
			return;
		}

		WorkbookReader wbr = null;
		filename = args[0];

		try {
			switch (AppSlave.extAnalyzer(filename)) {
			case XLS:
				wbr = new XLSReader(filename, report);
				break;
			case XLSX:
				wbr = new XLSXReader(filename, report);
				break;
			}
		} catch (UnsupportedExtOfInputFileException e) {
			report.writeln(e.toString());
			report.close();
			return;
		} catch (InputFileNotFoundException e) {
			report.writeln(e.toString());
			report.close();
			return;
		}

		list = wbr.fillList();
		Iterator<SheetLine> iter = null;

		try {
			iter = list.iterator();
		} catch (NullPointerException e) {
			report.writeln("Не удалось заполнить LinkedList");
			report.close();
			return;
		}

		while (iter.hasNext()) {
			SheetLine el = iter.next();
			System.out.println(el);
		}

		// Working with database
		try {
			DBWorker db = new DBWorker();
			db.сonnect();
			db.sendToDB(list, report);
			db.disconnect();
		} catch (DataBaseException e) {
			report.writeln(e.getMessage());
		}
		report.close();
	}
}
