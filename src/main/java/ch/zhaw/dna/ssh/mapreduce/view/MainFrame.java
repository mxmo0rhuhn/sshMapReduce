package ch.zhaw.dna.ssh.mapreduce.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ch.zhaw.dna.ssh.mapreduce.controller.OutputController;
import ch.zhaw.dna.ssh.mapreduce.controller.OutputController.OUTPUT_STRATEGY;

/**
 * Dieses ist das Hauptfenster der Applikation
 * 
 * @author Max
 * 
 */
@SuppressWarnings("serial") // Das GUI soll nicht serialisiert werden!
public class MainFrame extends JFrame implements Observer {

	// Der Controller wie Logfiles aufbereitet werden sollen
	private OutputController curOutputController;

	// Der letzte Pfad in dem eine Datei geöffnet wurde
	private String lastPath;

	// Eingabe für die URL, die durchsucht werden soll
	private JTextField urlJTextField;

	// Eingabe für das Wort nachdem gesucht werden soll
	private JTextField specialWorfField;

	// Ausgabe für die derzeitige Schachtlungstiefe
	private JPanel currentTiefePanel;

	// Ausgabe für die bereits durchsuchten Websites
	private JPanel currentPagesPanel;

	// Ausgabe für die bereits verstrichene Zeit
	private JPanel currentZeitPanel;

	// Ausgabe für die bereits erfolgten Treffer
	private JPanel currentVorkommenPanel;

	// Checkboxen für spezielle HTML-Elemente
	private JCheckBox h1CheckBox;
	private JCheckBox h2CheckBox;
	private JCheckBox h3CheckBox;
	private JCheckBox pCheckBox;
	private JCheckBox aCheckBox;

	// Dropdown für eine Schachtlungstiefe
	private JComboBox schachtlungsComboBox;

	/**
	 * Erstellt das Frame und stellt das look and feel
	 */
	public MainFrame(OutputController curOutputController) {

		this.curOutputController = curOutputController;
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "Konnte Interface nicht richtig aufbauen. Wechsle auf default interface.\n" + e.getMessage(), "Fehler", JOptionPane.OK_CANCEL_OPTION);
		}

		this.setTitle("SSH MapReduce");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setJMenuBar(createMenu());

		this.buildFrame();

		this.pack();
		this.setVisible(true);
	}

	/**
	 * Erstellt die Menüleiste der Applikation mit allen Unterpunkten.
	 * 
	 * @return die Menüleiste der Applikation
	 */
	private JMenuBar createMenu() {

		JMenuBar menuBar = new JMenuBar();

		menuBar.add(buildDateiMenu());

		return menuBar;
	}

	/**
	 * Erstellt eine Menüleiste mit der input und Output der Applikation festgelegt werden kann
	 * 
	 * @return eine Menü Leiste zum Festlegen von Input und Output
	 */
	private JMenuItem buildDateiMenu() {

		JMenu file = new JMenu("Datei");

		JMenuItem load = new JMenuItem("Laden");
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = null;
				if (lastPath != null) {
					chooser = new JFileChooser(lastPath);
				} else {
					chooser = new JFileChooser();
				}
				int returnVal = chooser.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// try {
					lastPath = chooser.getSelectedFile().getAbsolutePath();
					// TODO Hier muss ein MapReduce Task gestartet werden

					// } catch (IOException e1) {
					// JOptionPane.showMessageDialog(null, "Datei " + chooser.getSelectedFile().getName()
					// + " konnte nicht geöffnet werden", "Fehler", JOptionPane.ERROR_MESSAGE);
					// }
				}
			}
		});

		file.add(load);
		file.addSeparator();
		file.add(new JLabel("Log: "));

		ButtonGroup outOptions = new ButtonGroup();
		JRadioButtonMenuItem consoleMenuItem = new JRadioButtonMenuItem("Konsole");
		consoleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curOutputController.setOutput(OUTPUT_STRATEGY.CONSOLE);
			}
		});
		consoleMenuItem.setSelected(true);
		outOptions.add(consoleMenuItem);

		file.add(consoleMenuItem);

		JRadioButtonMenuItem fileMenuItem = new JRadioButtonMenuItem("Textdatei");
		fileMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curOutputController.setOutput(OUTPUT_STRATEGY.TEXTFILE);
			}
		});
		outOptions.add(fileMenuItem);
		file.add(fileMenuItem);

		return file;
	}

	/**
	 * Erstellt das content Pane und bestimmt die Ausrichtung des GUI.
	 */
	private void buildFrame() {
		this.setLayout(new BorderLayout());
		this.add(createInputPanel(), BorderLayout.CENTER);
		this.add(createStatusPanel(), BorderLayout.SOUTH);

		this.setMinimumSize(new Dimension(100, 100));
		this.setPreferredSize(new Dimension(500, 300));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setLocation((int) center.getX() - 250, (int) center.getY() - 150);

		this.setResizable(false);
	}

	/**
	 * Erstellt ein Panel mit verschiedenen Eingabemöglichkeiten für die Applikation.
	 * 
	 * @return ein Panel um Input aufzunehmen
	 */
	private Component createInputPanel() {

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(6, 1));

		// Zeile 1 : URL
		JPanel urlPanel = new JPanel();
		urlPanel.setLayout(new GridLayout(1, 2));
		urlPanel.add(new JLabel("URL"));
		urlJTextField = new JTextField();
		urlPanel.add(urlJTextField);
		inputPanel.add(urlPanel);

		// Zeile 2 : Spezielles Wort
		JPanel wortPanel = new JPanel();
		wortPanel.setLayout(new GridLayout(1, 2));
		wortPanel.add(new JLabel("Gesuchtes Wort"));
		specialWorfField = new JTextField();
		wortPanel.add(specialWorfField);
		inputPanel.add(wortPanel);

		// Zeile 3 HTML-Tags h1, h2, h3
		JPanel htmlTags1Panel = new JPanel();
		htmlTags1Panel.setLayout(new GridLayout(1, 3));

		h1CheckBox = new JCheckBox("<h1>");
		htmlTags1Panel.add(h1CheckBox);

		h2CheckBox = new JCheckBox("<h2>");
		htmlTags1Panel.add(h2CheckBox);

		h3CheckBox = new JCheckBox("<h3>");
		htmlTags1Panel.add(h3CheckBox);
		inputPanel.add(htmlTags1Panel);

		// Zeile 4 HTML-Tags p, a
		JPanel htmlTags2Panel = new JPanel();
		htmlTags2Panel.setLayout(new GridLayout(1, 3));

		pCheckBox = new JCheckBox("<p>");
		htmlTags2Panel.add(pCheckBox);

		aCheckBox = new JCheckBox("<a>");
		htmlTags2Panel.add(aCheckBox);

		htmlTags2Panel.add(new JPanel());
		inputPanel.add(htmlTags2Panel);

		// Zeile 5 Schachtlungstiefe
		JPanel schachtlungsPanel = new JPanel();
		schachtlungsPanel.setLayout(new GridLayout(1, 2));
		schachtlungsPanel.add(new JLabel("Schachtlungstiefe"));

		// TODO hier muss noch eine maximale Schachtlungstiefe hin
		schachtlungsComboBox = new JComboBox(new String[] { "Work needs to be done" });
		schachtlungsPanel.add(schachtlungsComboBox);
		inputPanel.add(schachtlungsPanel);

		// Zeile 6 : Senden Button
		JButton runButton = new JButton("Los geht's!");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Sth. has to happen here!
				for(int i = 0; i< 200 ; i++) {
					MainFrame.this.curOutputController.println("Test");
				}
			}
		});
		inputPanel.add(runButton);

		return inputPanel;
	}

	/**
	 * Erstellt ein Panel mit den Statusanzeigen des laufenden MapReduce
	 * 
	 * @return ein Panel mit allen gewünschten Statusanzeigen.
	 */
	private Component createStatusPanel() {

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(2, 4));

		// Zeile 1 Vorkommen und Tiefe
		statusPanel.add(new JLabel("Vorkommen:"));

		currentVorkommenPanel = new JPanel();
		statusPanel.add(currentVorkommenPanel);

		statusPanel.add(new JLabel("Tiefe:"));

		currentTiefePanel = new JPanel();
		statusPanel.add(currentTiefePanel);

		// Zeile 2 : Seiten Durchsucht und Zeit
		statusPanel.add(new JLabel("Seiten durchsucht:"));

		currentPagesPanel = new JPanel();
		statusPanel.add(currentPagesPanel);

		statusPanel.add(new JLabel("Zeit"));

		currentZeitPanel = new JPanel();
		statusPanel.add(currentZeitPanel);

		return statusPanel;
	}

	/** {@inheritDoc} */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Hier müssen dann die Felder ses StatusPanels aktualisiert werden.

	}
}