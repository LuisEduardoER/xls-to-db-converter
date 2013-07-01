package entity;

import java.io.FileWriter;
import java.io.IOException;

public class Report {

	private FileWriter fileWriter;

	public Report(String filename) throws IOException {
		fileWriter = new FileWriter(filename);
	}

	/**
	 * Close the file and save changes
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		fileWriter.close();
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
