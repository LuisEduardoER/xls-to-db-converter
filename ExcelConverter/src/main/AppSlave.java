package main;

import exceptions.*;

enum Extensions {
	XLS, XLSX
}

public class AppSlave {

	public static Extensions extAnalyzer(String filename)
			throws UnsupportedExtOfInputFileException {
		if (filename.endsWith(".xlsx"))
			return Extensions.XLSX;
		if (filename.endsWith(".xls"))
			return Extensions.XLS;
		throw new UnsupportedExtOfInputFileException();
	}

	public static boolean commandLineArgumentsTester(String[] args)
			throws CommandLineArgumentException {
		if (args.length == 0)
			throw new CommandLineArgumentException();
		return true;
	}

}
