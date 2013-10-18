package Views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.JButton;

import viewModels.BookListModel;
import domain.Book;
import domain.Library;

import java.awt.Insets;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BookMaster extends JFrame{

	private JPanel contentPane;
	private JList bookJList;
	private Library library;
	private BookListModel listModel;
	private JLabel bookCountJLabel;
	private JLabel copiesCountJLabel;
	private JButton showSelectedJButton;

	public BookMaster(Library library) {
		super();
		this.library = library;
		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initGUI() {
		
		this.setMinimumSize(new Dimension(800, 500));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 633, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		
		JLayeredPane bookLayeredPane = new JLayeredPane();
		tabbedPane.addTab("B\u00FCcher", null, bookLayeredPane, null);
		bookLayeredPane.setLayout(new BoxLayout(bookLayeredPane, BoxLayout.Y_AXIS));
		
		JPanel statisticJPanel = new JPanel();
		statisticJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		bookLayeredPane.add(statisticJPanel);
		statisticJPanel.setBorder(new TitledBorder(null, "Inventar Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		

		JPanel inventoryJPanel = new JPanel();
		bookLayeredPane.add(inventoryJPanel);
		inventoryJPanel.setBorder(new TitledBorder(null, "Buch-Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_inventoryJPanel = new GridBagLayout();
		gbl_inventoryJPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_inventoryJPanel.rowHeights = new int[]{0, 0, 0};
		gbl_inventoryJPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_inventoryJPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		inventoryJPanel.setLayout(gbl_inventoryJPanel);

		
		final JLabel selectedJLabel = new JLabel("Ausgewaehlt: 0");
		selectedJLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_selectedJLabel = new GridBagConstraints();
		gbc_selectedJLabel.anchor = GridBagConstraints.WEST;
		gbc_selectedJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectedJLabel.gridx = 0;
		gbc_selectedJLabel.gridy = 0;
		inventoryJPanel.add(selectedJLabel, gbc_selectedJLabel);
		
		showSelectedJButton = new JButton("Selektierte Anzeigen");
		showSelectedJButton.setEnabled(false);
		showSelectedJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Book book : (List<Book>)bookJList.getSelectedValuesList()) {
					//initBookDetail(book);
					new BookDetail(library, book);
				}
			}
		});
		GridBagConstraints gbc_showSelectedJButton = new GridBagConstraints();
		gbc_showSelectedJButton.insets = new Insets(0, 0, 5, 5);
		gbc_showSelectedJButton.gridx = 1;
		gbc_showSelectedJButton.gridy = 0;
		inventoryJPanel.add(showSelectedJButton, gbc_showSelectedJButton);
		
		JButton newBookJButton = new JButton("Neues Buch hinzufügen");
		newBookJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BookDetail(library, null);
					//initBookDetail(null);
			}
		});
		GridBagConstraints gbc_newBookJButton = new GridBagConstraints();
		gbc_newBookJButton.insets = new Insets(0, 0, 5, 0);
		gbc_newBookJButton.gridx = 2;
		gbc_newBookJButton.gridy = 0;
		inventoryJPanel.add(newBookJButton, gbc_newBookJButton);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		inventoryJPanel.add(scrollPane, gbc_scrollPane);
		statisticJPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		bookCountJLabel = new JLabel("Anzahl Buecher: " + library.getBooks().size());
		statisticJPanel.add(bookCountJLabel);
		
		copiesCountJLabel = new JLabel("Anzahl Exemplare: " + library.getCopies().size());
		statisticJPanel.add(copiesCountJLabel);	

		bookJList = new JList();
		bookJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 if(bookJList.getSelectedIndex() != -1)  {
					 if (e.getClickCount() == 2) {
						 new BookDetail(library, (Book)bookJList.getSelectedValue());
					 }
			     }
			}
		});
		listModel = new BookListModel(library);
		listModel.addListDataListener(new ListDataListener() {
			@Override
			public void contentsChanged(ListDataEvent arg0) {
				refreshLabelCount();
			}
			@Override
			public void intervalAdded(ListDataEvent arg0) {
				refreshLabelCount();
			}
			@Override
			public void intervalRemoved(ListDataEvent arg0) {
				refreshLabelCount();
			}
			
		});
		bookJList.setModel(listModel);
		bookJList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("deprecation")
			public void valueChanged(ListSelectionEvent e) {
				selectedJLabel.setText("Ausgewaehlt: " + bookJList.getSelectedIndices().length);
				if (bookJList.getSelectedIndices().length > 0) {
					showSelectedJButton.setEnabled(true);
				} else {
					showSelectedJButton.setEnabled(false);
				}
			}
		});

		
		scrollPane.setViewportView(bookJList);		
		JLayeredPane loanLayeredPane = new JLayeredPane();
		tabbedPane.addTab("Ausleihen", null, loanLayeredPane, null);
		
		JLabel titleJLabel = new JLabel("Swinging Library");
		titleJLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titleJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(titleJLabel, BorderLayout.NORTH);
		

	}
	
	private void refreshLabelCount() {
		bookCountJLabel.setText("Anzahl Buecher: " + library.getBooks().size());
		copiesCountJLabel.setText("Anzahl Exemplare: " + library.getCopies().size());
	}
	
	
}
