/**
 * CCT-Tool (Crypto Currency Tax Tool)
 *
 * Erstellt 2018 von <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 */
package de.be.rhi.crypto.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

/**
 * TODO RHildebrand JavaDoc
 *
 * @author <a href="mailto:umdiecke@gmx.de">René Hildebrand</a>
 * @version 1.0
 * @since 31.03.2018, 21:05:27
 *
 */
public class CCTToolWindow {

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					CCTToolWindow window = new CCTToolWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private JFrame frame;

	private JTable table;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();

	/**
	 * Create the application.
	 */
	public CCTToolWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 763, 398);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem mntmDateiImportieren = new JMenuItem("Datei importieren");

		mnDatei.add(mntmDateiImportieren);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem mntmber = new JMenuItem("Über...");
		mnHilfe.add(mntmber);

		JProgressBar progressBar = new JProgressBar();
		this.frame.getContentPane().add(progressBar, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		this.frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.table = new JTable();
		scrollPane.setViewportView(this.table);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(Action.NAME, "SwingAction");
			putValue(Action.SHORT_DESCRIPTION, "Some short description");
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
		}
	}

	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(Action.NAME, "SwingAction_1");
			putValue(Action.SHORT_DESCRIPTION, "Some short description");
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
		}
	}
}
