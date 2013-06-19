package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import jdbc.DBWorker;

import readers.WorkbookReader;
import readers.XLSReader;
import readers.XLSXReader;

import model.Report;
import model.SheetLine;

import exceptions.*;

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
			return;
		} catch (FileNotFoundException e) {
			report.writeln(e.toString());
			return;
		}

		list = wbr.fillList();
		Iterator<SheetLine> iter = null;

		try {
			iter = list.iterator();
		} catch (NullPointerException e) {
			report.writeln("Не удалось заполнить LinkedList");
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
			db.sendToDB(list,report);
			db.disconnect(); 
		} catch (DataBaseException e) {
			report.writeln(e.getMessage()); 
		}
		 
		report.close();
	}
}
