package model;

import java.io.FileWriter;
import java.io.IOException;

public class Report {

	private FileWriter fileWriter;

	public Report(String filename) throws IOException {
		try {
			fileWriter = new FileWriter(filename);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Close the file and save changes
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			fileWriter.close();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Write string to the file and return carriage
	 * 
	 * @param text
	 *            output string
	 * @throws IOException
	 */
	public void writeln(String text) throws IOException {
		fileWriter.write(text + "\n");
	}

	/**
	 * Write string to the file
	 * 
	 * @param text
	 *            output string
	 * @throws IOException
	 */
	public void write(String text) throws IOException {
		fileWriter.write(text);
	}

	/**
	 * Close the file (if it wasn't) when collector will work
	 * 
	 * @throws IOException
	 */
	protected void finalize() throws IOException {
		if (fileWriter != null)
			close();
	}
}
