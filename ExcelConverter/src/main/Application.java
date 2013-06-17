package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import readers.WorkbookReader;
import readers.XLSReader;
import readers.XLSXReader;

import model.SheetLine;

import exceptions.*;

public class Application {
	private static String filename;

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

		// TODO
		SheetLine sl;
		try {
			sl = wbr.getLine(1);
		} catch (UnsupportedFormatOfInputFileException e) {
			System.out.println(e);
			return;
		}

		System.out.print(sl.getNumber() + " ");
		System.out.print(sl.getType() + " ");
		System.out.print(sl.getName() + " ");
		System.out.print(sl.getAddress() + " ");
		System.out.print(sl.getUnp() + " ");
		System.out.print(sl.getOkpo() + " ");
		System.out.print(sl.getAccount() + " ");
		System.out.println(sl.isNets());
	}

}
