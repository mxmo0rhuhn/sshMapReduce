package ch.zhaw.dna.ssh.mapreduce.view.util;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Diese Klasse stellt die Konsolenausgabe fuer den Server in einem eigenen
 * Fenster dar. Anweisungen wie System.out.println() nach dem Instanzieren auf
 * diesem Fenster dargestellt.
 * 
 * @author Max Schrimpf
 */
@SuppressWarnings("serial")
// Wird nicht Serialisiert
public class SysoFrame extends JFrame {
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final JPanel panel;
    private AtomicInteger msgCounter;

    /**
     * Erstellt ein neues Konsolen-Fenster, welches ab sofort die
     * Konsolenausgabe darstellt.
     */
    public SysoFrame() {
    	msgCounter = new AtomicInteger(0);

        this.textArea = new JTextArea(); 
        this.textArea.setEditable(false);
        this.textArea.setFont(new Font( Font.MONOSPACED, Font.PLAIN, 18 ));

        this.scrollPane = new JScrollPane(this.textArea);

        this.panel = new JPanel(new BorderLayout());
        this.panel.add(this.scrollPane, BorderLayout.CENTER);

        add(this.panel);

        redirectSystemStreams();

        setResizable(true);
        setTitle("Log");
        setSize(new Dimension(800, 300));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Schreibt Text in die Konsole.
     * 
     * @param text
     *            Der Text der geschrieben werden soll.
     */
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SysoFrame.this.textArea.append(text);
            }
        });
    }

    /**
     * Leitet die Standard Nachrichten Streams auf dieses Fenster um.
     */
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}