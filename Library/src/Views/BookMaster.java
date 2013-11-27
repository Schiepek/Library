package Views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.BoxLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JButton;

import viewModels.BookTableModel;
import viewModels.LoanTableModel;
import domain.Book;
import domain.Customer;
import domain.Library;
import domain.Loan;

import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import java.awt.Component;

public class BookMaster extends JFrame{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Library library;
	private BookTableModel bookTableModel;
	private JLabel bookCountJLabel;
	private JLabel copiesCountJLabel;
	private JButton showSelectedBookJButton;
	private JTable bookJTable;
	private JCheckBox availableJCheckBox;
	private JTextField searchBookJTextField;
	private TableRowSorter<BookTableModel> bookSorter;
	private TableRowSorter<LoanTableModel> loanSorter;
	private RowFilter<Object, Object> availableFilter;
	private RowFilter<Object, Object> overdueFilter;
	private boolean showAvailable = true;
	private boolean showOverdue = true;
	private JTextField searchLoanJTextField;
	private JTable loanJTable;
	private LoanTableModel loanTableModel;
	private JButton showSelectedLoanJButton;
	private JLabel countLoanJLabel;
	private JLabel currentLoanJLabel;
	private JLabel overdueLoanJLabel;	
	private ArrayList<Book> openedBooks = new ArrayList<Book>();
	private ArrayList<Customer> openedCustomers = new ArrayList<Customer>();

	public BookMaster(Library library) {
		super();
		setTitle("Swinging Library");
		this.library = library;
		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initGUI() {
		
		this.setMinimumSize(new Dimension(1200, 700));
		bookTableModel= new BookTableModel(library);
		
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
		gbl_inventoryJPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_inventoryJPanel.rowHeights = new int[]{0, 0, 0};
		gbl_inventoryJPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_inventoryJPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		inventoryJPanel.setLayout(gbl_inventoryJPanel);

		
		final JLabel selectedBookJLabel = new JLabel("Ausgewählt: 0");
		selectedBookJLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_selectedJLabel = new GridBagConstraints();
		gbc_selectedJLabel.anchor = GridBagConstraints.WEST;
		gbc_selectedJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectedJLabel.gridx = 0;
		gbc_selectedJLabel.gridy = 0;
		inventoryJPanel.add(selectedBookJLabel, gbc_selectedJLabel);
		
		showSelectedBookJButton = new JButton("Selektierte Anzeigen");
		showSelectedBookJButton.setEnabled(false);
		showSelectedBookJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i : bookJTable.getSelectedRows()) {
					openBookDetail(library.getBooks().get(bookJTable.convertRowIndexToModel(i)));
				}
			}
		});
		
		searchBookJTextField = new JTextField();
		searchBookJTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				applyBookFilter();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				applyBookFilter();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}			
		});
		GridBagConstraints gbc_searchJTextField = new GridBagConstraints();
		gbc_searchJTextField.insets = new Insets(0, 0, 5, 5);
		gbc_searchJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchJTextField.gridx = 1;
		gbc_searchJTextField.gridy = 0;
		inventoryJPanel.add(searchBookJTextField, gbc_searchJTextField);
		searchBookJTextField.setColumns(10);
		
		availableJCheckBox = new JCheckBox("nur verfügbare");
		availableJCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAvailable = !showAvailable;
				applyBookFilter();
			}
		});
		GridBagConstraints gbc_AvailableJCheckBox = new GridBagConstraints();
		gbc_AvailableJCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_AvailableJCheckBox.gridx = 2;
		gbc_AvailableJCheckBox.gridy = 0;
		inventoryJPanel.add(availableJCheckBox, gbc_AvailableJCheckBox);
		GridBagConstraints gbc_showSelectedJButton = new GridBagConstraints();
		gbc_showSelectedJButton.insets = new Insets(0, 0, 5, 5);
		gbc_showSelectedJButton.gridx = 3;
		gbc_showSelectedJButton.gridy = 0;
		inventoryJPanel.add(showSelectedBookJButton, gbc_showSelectedJButton);
		
		JButton newBookJButton = new JButton("Neues Buch hinzufügen");
		newBookJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openBookDetail(null);
			}
		});
		GridBagConstraints gbc_newBookJButton = new GridBagConstraints();
		gbc_newBookJButton.insets = new Insets(0, 0, 5, 0);
		gbc_newBookJButton.gridx = 4;
		gbc_newBookJButton.gridy = 0;
		inventoryJPanel.add(newBookJButton, gbc_newBookJButton);


		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		inventoryJPanel.add(scrollPane, gbc_scrollPane);
		bookJTable = new JTable();
		bookJTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)  {
					if(bookJTable.getSelectedRow() != -1)  {
						 if (e.getClickCount() == 2) {
							 openBookDetail(library.getBooks().get(bookJTable.convertRowIndexToModel(bookJTable.getSelectedRow())));							 
						 }
				     }
				}
			}
		});
		scrollPane.setViewportView(bookJTable);
		bookJTable.setModel(bookTableModel);
		bookJTable.getTableHeader().setReorderingAllowed(false);
		bookJTable.getSelectedRowCount();
		bookJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				selectedBookJLabel.setText("Ausgewählt: " + bookJTable.getSelectedRowCount());
				if (bookJTable.getSelectedRowCount() > 0) {
					showSelectedBookJButton.setEnabled(true);
				} else {
					showSelectedBookJButton.setEnabled(false);
				}
			}
		});
		bookTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				refreshBookLabelCount();				
			}			
		});
		
		bookJTable.getColumnModel().getColumn(0).setMaxWidth(100);
		bookJTable.getColumnModel().getColumn(0).setMinWidth(100);
		
		statisticJPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		bookCountJLabel = new JLabel("Anzahl Bücher: " + library.getBooks().size());
		statisticJPanel.add(bookCountJLabel);
		
		copiesCountJLabel = new JLabel("Anzahl Exemplare: " + library.getCopies().size());
		statisticJPanel.add(copiesCountJLabel);
		JLayeredPane loanLayeredPane = new JLayeredPane();
		tabbedPane.addTab("Ausleihen", null, loanLayeredPane, null);
		loanLayeredPane.setLayout(new BoxLayout(loanLayeredPane, BoxLayout.Y_AXIS));
		

		
		JLabel titleJLabel = new JLabel("Swinging Library");
		titleJLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titleJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(titleJLabel, BorderLayout.NORTH);
		
		bookSorter = new TableRowSorter<BookTableModel>(bookTableModel);
		bookJTable.setRowSorter(bookSorter);
		
		availableFilter = new RowFilter<Object, Object>() {
	        public boolean include(Entry entry) {
	        	if (showAvailable){
	        		return true;
	        	}
	        	return (((String) entry.getValue(0)).matches("[0-9]+"));
	        }
	    };
	    
	    bookSorter.setRowFilter(availableFilter);
	    
		JPanel loanstatisticJPanel = new JPanel();
		loanstatisticJPanel.setBorder(new TitledBorder(null, "Ausleihe Statistiken", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		loanstatisticJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		loanLayeredPane.add(loanstatisticJPanel);
		loanstatisticJPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		countLoanJLabel = new JLabel("Anzahl Ausleihen: " + library.getLoans().size());
		loanstatisticJPanel.add(countLoanJLabel);
		
		currentLoanJLabel = new JLabel("Aktuell Ausgeliehen: " + library.getLentOutBooks().size());
		loanstatisticJPanel.add(currentLoanJLabel);
		
		overdueLoanJLabel = new JLabel("Überfällige Ausleihen: " + library.getOverdueLoans().size());
		loanstatisticJPanel.add(overdueLoanJLabel);
		
		JPanel collectedloanJPanel = new JPanel();
		collectedloanJPanel.setBorder(new TitledBorder(null, "Erfasste Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		loanLayeredPane.add(collectedloanJPanel);
		GridBagLayout gbl_collectedloanJPanel = new GridBagLayout();
		gbl_collectedloanJPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_collectedloanJPanel.rowHeights = new int[]{0, 0, 0};
		gbl_collectedloanJPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_collectedloanJPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		collectedloanJPanel.setLayout(gbl_collectedloanJPanel);
		
		final JLabel selectedLoanJLabel = new JLabel("Ausgewählt: 0");
		selectedLoanJLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_selectedLoanJLabel = new GridBagConstraints();
		gbc_selectedLoanJLabel.anchor = GridBagConstraints.WEST;
		gbc_selectedLoanJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectedLoanJLabel.gridx = 0;
		gbc_selectedLoanJLabel.gridy = 0;
		collectedloanJPanel.add(selectedLoanJLabel, gbc_selectedLoanJLabel);
		
		searchLoanJTextField = new JTextField();
		GridBagConstraints gbc_searchLoanJTextField = new GridBagConstraints();
		gbc_searchLoanJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchLoanJTextField.insets = new Insets(0, 0, 5, 5);
		gbc_searchLoanJTextField.gridx = 1;
		gbc_searchLoanJTextField.gridy = 0;
		collectedloanJPanel.add(searchLoanJTextField, gbc_searchLoanJTextField);
		searchLoanJTextField.setColumns(10);
		searchLoanJTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				applyLoanFilter();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				applyLoanFilter();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}			
		});

		JCheckBox overdueJCheckBox = new JCheckBox("Nur Überfällige");
		GridBagConstraints gbc_overdueJCheckBox = new GridBagConstraints();
		gbc_overdueJCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_overdueJCheckBox.gridx = 2;
		gbc_overdueJCheckBox.gridy = 0;
		collectedloanJPanel.add(overdueJCheckBox, gbc_overdueJCheckBox);
		overdueJCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showOverdue = !showOverdue;
				applyLoanFilter();
			}
		});
		
		showSelectedLoanJButton = new JButton("Selektierte Ausleihe anzeigen");
		showSelectedLoanJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		showSelectedLoanJButton.setEnabled(false);
		showSelectedLoanJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i : loanJTable.getSelectedRows()) {
					openLoanDetail(library.getLentOutLoans().get(loanJTable.convertRowIndexToModel(i)));					
				}
			}
		});
		
		GridBagConstraints gbc_showSelectedLoanJButton = new GridBagConstraints();
		gbc_showSelectedLoanJButton.insets = new Insets(0, 0, 5, 5);
		gbc_showSelectedLoanJButton.gridx = 3;
		gbc_showSelectedLoanJButton.gridy = 0;
		collectedloanJPanel.add(showSelectedLoanJButton, gbc_showSelectedLoanJButton);
		
		JButton newLoanJButton = new JButton("Neue Ausleihe erfassen");
		GridBagConstraints gbc_newLoanJButton = new GridBagConstraints();
		gbc_newLoanJButton.insets = new Insets(0, 0, 5, 0);
		gbc_newLoanJButton.gridx = 4;
		gbc_newLoanJButton.gridy = 0;
		collectedloanJPanel.add(newLoanJButton, gbc_newLoanJButton);
		newLoanJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLoanDetail(null);
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 5;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		collectedloanJPanel.add(scrollPane_1, gbc_scrollPane_1);
		
		loanJTable = new JTable();
		loanJTable.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(loanJTable);
		loanTableModel = new LoanTableModel(library);
		loanJTable.setModel(loanTableModel);
		loanJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				selectedLoanJLabel.setText("Ausgewählt: " + loanJTable.getSelectedRowCount());
				if (loanJTable.getSelectedRowCount() > 0) {
					showSelectedLoanJButton.setEnabled(true);
				} else {
					showSelectedLoanJButton.setEnabled(false);
				}
			}
		});
		loanTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				refreshLoanLabelCount();				
			}			
		});
		loanJTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)  {
					if(loanJTable.getSelectedRow() != -1)  {
						 if (e.getClickCount() == 2) {
							 openLoanDetail(library.getLentOutLoans().get(loanJTable.convertRowIndexToModel(loanJTable.getSelectedRow())));
						 }
				     }
				}
			}
		});
		

		
		loanJTable.getColumnModel().getColumn(0).setMaxWidth(50);
		loanJTable.getColumnModel().getColumn(1).setMaxWidth(80);
		loanJTable.getColumnModel().getColumn(2).setMinWidth(500);
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment( JLabel.CENTER );
		loanJTable.getColumnModel().getColumn(1).setCellRenderer( leftRenderer );
			
		loanSorter = new TableRowSorter<LoanTableModel>(loanTableModel);
		loanJTable.setRowSorter(loanSorter);
		
		overdueFilter = new RowFilter<Object, Object>() {
	        public boolean include(Entry entry) {
	        	if (showOverdue){
	        		return true;
	        	}
	        	return (((String) entry.getValue(0)).matches("fällig"));
	        }
	    };	    
	    loanSorter.setRowFilter(overdueFilter);
	
	}
	
	private void refreshBookLabelCount() {
		bookCountJLabel.setText("Anzahl Bücher: " + library.getBooks().size());
		copiesCountJLabel.setText("Anzahl Exemplare: " + library.getCopies().size());
	}
	
	private void refreshLoanLabelCount() {
		countLoanJLabel.setText("Anzahl Ausleihen: " + library.getLoans().size());
		currentLoanJLabel.setText("Anzahl Ausgeliehen: " + library.getLentOutBooks().size());
		overdueLoanJLabel.setText("Überfällige Ausleihen: " + library.getOverdueLoans().size());
	}
	
	public void applyBookFilter() {
		List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>();
		filters.add(availableFilter);
		filters.add(RowFilter.regexFilter("(?i)" + searchBookJTextField.getText()));
		bookSorter.setRowFilter(RowFilter.andFilter(filters));
	}
	
	public void applyLoanFilter() {
		List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>();
		filters.add(overdueFilter);
		filters.add(RowFilter.regexFilter("(?i)" + searchLoanJTextField.getText()));
		loanSorter.setRowFilter(RowFilter.andFilter(filters));
	}
	
	private void openBookDetail(Book openBook) {
		if(!openedBooks.contains(openBook)) {
			new BookDetail(library, openBook, openedBooks);
			if(openBook != null) openedBooks.add(openBook);
		}
	}
	
	private void openLoanDetail(Loan loan) {
		Customer openCustomer = null;
		if(loan != null) openCustomer = loan.getCustomer();
		if(!openedCustomers.contains(openCustomer)) {
			new LoanDetail(library, loan, openedCustomers);
			if(loan != null) openedCustomers.add(openCustomer);
		}
	}
}
