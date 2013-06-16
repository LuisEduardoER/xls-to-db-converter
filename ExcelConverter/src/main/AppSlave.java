package main;

import exceptions.*;

public class AppSlave {

	// returns 0 if extension is .xls, 1 if .xlsx
	// returns -1 if couldn't identify the extension
	public static int extAnalyzer(String filename)
			throws UnsupportedExtOfInputFileException {
		if (filename.endsWith(".xlsx"))
			return 1;
		if (filename.endsWith(".xls"))
			return 0;
		throw new UnsupportedExtOfInputFileException();
	}

	public static boolean commandLineArgumentsTester(String[] args)
			throws CommandLineArgumentException {
		if (args.length == 0)
			throw new CommandLineArgumentException();
		return true;
	}

}
