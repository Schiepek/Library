package Views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
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
	private JList bookList;
	private Library library;
	private BookListModel listModel;

	public BookMaster(Library library) {
		this.library = library;
		
		
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
		
		JPanel statisticPanel = new JPanel();
		statisticPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		bookLayeredPane.add(statisticPanel);
		statisticPanel.setBorder(new TitledBorder(null, "Inventar Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		

		JPanel inventoryPanel = new JPanel();
		bookLayeredPane.add(inventoryPanel);
		inventoryPanel.setBorder(new TitledBorder(null, "Buch-Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_inventoryPanel = new GridBagLayout();
		gbl_inventoryPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_inventoryPanel.rowHeights = new int[]{0, 0, 0};
		gbl_inventoryPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_inventoryPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		inventoryPanel.setLayout(gbl_inventoryPanel);

		
		final JLabel Selectedlabel = new JLabel("Ausgewählt: 0");
		Selectedlabel.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_Selectedlabel = new GridBagConstraints();
		gbc_Selectedlabel.anchor = GridBagConstraints.WEST;
		gbc_Selectedlabel.insets = new Insets(0, 0, 5, 5);
		gbc_Selectedlabel.gridx = 0;
		gbc_Selectedlabel.gridy = 0;
		inventoryPanel.add(Selectedlabel, gbc_Selectedlabel);
		
		JButton showselectButton = new JButton("Selektierte Anzeigen");
		showselectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Book book : (List<Book>)bookList.getSelectedValuesList()) {
					initBookDetail(book);
				}
			}
		});
		GridBagConstraints gbc_showselectButton = new GridBagConstraints();
		gbc_showselectButton.insets = new Insets(0, 0, 5, 5);
		gbc_showselectButton.gridx = 1;
		gbc_showselectButton.gridy = 0;
		inventoryPanel.add(showselectButton, gbc_showselectButton);
		
		JButton newbookButton = new JButton("Neues Buch hinzuf\u00FCgen");
		newbookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					initBookDetail(null);
			}
		});
		GridBagConstraints gbc_newbookButton = new GridBagConstraints();
		gbc_newbookButton.insets = new Insets(0, 0, 5, 0);
		gbc_newbookButton.gridx = 2;
		gbc_newbookButton.gridy = 0;
		inventoryPanel.add(newbookButton, gbc_newbookButton);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		inventoryPanel.add(scrollPane, gbc_scrollPane);
		statisticPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblBookcountlabel = new JLabel("Anzahl Bücher: " + library.getBooks().size());
		statisticPanel.add(lblBookcountlabel);
		
		JLabel lblBooktotallabel = new JLabel("Anzahl Exemplare: " + library.getCopies().size());
		statisticPanel.add(lblBooktotallabel);	

		bookList = new JList();
		listModel = new BookListModel(library);
		bookList.setModel(listModel);
		bookList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("deprecation")
			public void valueChanged(ListSelectionEvent e) {
				Selectedlabel.setText("Ausgewählt: " + bookList.getSelectedIndices().length);
			}
		});

		
		scrollPane.setViewportView(bookList);		
		JLayeredPane loanLayeredPane = new JLayeredPane();
		tabbedPane.addTab("Ausleihen", null, loanLayeredPane, null);
		
		JLabel lblSwinginglibrarytextpanel = new JLabel("Swinging Library");
		lblSwinginglibrarytextpanel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSwinginglibrarytextpanel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblSwinginglibrarytextpanel, BorderLayout.NORTH);
		

	}
	
	
	private void initBookDetail(final Book book) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
						BookDetail frame = new BookDetail(library,book);
						frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
