/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.view.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Schreibt den User-Output in ein Textfile
 * 
 * @author Max
 * 
 */
public class FileOutput implements OutputInterface {

	private final File selectedFile;
	private final String curFileEnding;

	/**
	 * Erstellt den FileOutput Dazu muss abgefragt werden wohin das File gespeichert wird
	 */
	public FileOutput() {

		JFileChooser chooser = new JFileChooser("Logfile");
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			selectedFile = chooser.getSelectedFile();
		} else {
			selectedFile = new File(System.getProperty("java.io.tmpdir") + "/Log");
		}
		curFileEnding = ".txt";
	}

	/** {@inheritDoc} */
	@Override
	public void println(String line) {
		BufferedWriter curFW = null;
		try {
			try {
				curFW = new BufferedWriter(new FileWriter(selectedFile.getAbsolutePath() + curFileEnding));
				curFW.newLine();
				curFW.write(line);
			} finally {
				if (curFW != null) {
					curFW.close();
				}
			}
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, "Konnte nicht in File schreiben.\n" + e.getMessage(), "Fehler", JOptionPane.OK_CANCEL_OPTION);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		// File muss nicht geschlossen werden
	}

}
