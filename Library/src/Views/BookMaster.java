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
import domain.Library;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class BookMaster extends JFrame{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Library library;
	//private BookTableModel bookTableModel;
	private JLabel bookCountJLabel;
	private JLabel copiesCountJLabel;
	private JButton showSelectedJButton;
	private JTable bookJTable;
	private JCheckBox availableJCheckBox;
	private JTextField searchJTextField;
	private TableRowSorter<BookTableModel> sorter;
	private RowFilter<Object, Object> filter;
	private boolean showAvailable = true;

	public BookMaster(Library library) {
		super();
		setTitle("Swinging Library");
		this.library = library;
		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initGUI() {
		
		this.setMinimumSize(new Dimension(800, 500));
		BookTableModel bookTableModel= new BookTableModel(library);
		
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
				for(int i : bookJTable.getSelectedRows()) {
					new BookDetail(library, library.getBooks().get(i));
				}
			}
		});
		
		searchJTextField = new JTextField();
		searchJTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				applyFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				applyFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
			
		});
		GridBagConstraints gbc_searchJTextField = new GridBagConstraints();
		gbc_searchJTextField.insets = new Insets(0, 0, 5, 5);
		gbc_searchJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchJTextField.gridx = 1;
		gbc_searchJTextField.gridy = 0;
		inventoryJPanel.add(searchJTextField, gbc_searchJTextField);
		searchJTextField.setColumns(10);
		
		availableJCheckBox = new JCheckBox("nur verfügbare");
		availableJCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAvailable = !showAvailable;
				applyFilter();
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
							 new BookDetail(library, library.getBooks().get(bookJTable.getSelectedRow()));
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
				selectedJLabel.setText("Ausgewaehlt: " + bookJTable.getSelectedRowCount());
				if (bookJTable.getSelectedRowCount() > 0) {
					showSelectedJButton.setEnabled(true);
				} else {
					showSelectedJButton.setEnabled(false);
				}
			}
		});
		bookTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				refreshLabelCount();				
			}			
		});
		
		bookJTable.getColumnModel().getColumn(0).setMaxWidth(100);		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		bookJTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );

		
		statisticJPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		bookCountJLabel = new JLabel("Anzahl Bücher: " + library.getBooks().size());
		statisticJPanel.add(bookCountJLabel);
		
		copiesCountJLabel = new JLabel("Anzahl Exemplare: " + library.getCopies().size());
		statisticJPanel.add(copiesCountJLabel);
		JLayeredPane loanLayeredPane = new JLayeredPane();
		tabbedPane.addTab("Ausleihen", null, loanLayeredPane, null);
		
		JLabel titleJLabel = new JLabel("Swinging Library");
		titleJLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titleJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(titleJLabel, BorderLayout.NORTH);
		
		
		
		sorter = new TableRowSorter<BookTableModel>(bookTableModel);
		bookJTable.setRowSorter(sorter);
		
		filter = new RowFilter<Object, Object>() {
	        public boolean include(Entry entry) {
	        	if (showAvailable){
	        		return true;
	        	}
	        	return (((String) entry.getValue(0)).matches("[0-9]+"));
	        }
	    };
	    
	    sorter.setRowFilter(filter);
		

	}
	
	private void refreshLabelCount() {
		bookCountJLabel.setText("Anzahl Bücher: " + library.getBooks().size());
		copiesCountJLabel.setText("Anzahl Exemplare: " + library.getCopies().size());
	}
	
	public void applyFilter() {
		List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>();
		filters.add(filter);
		filters.add(RowFilter.regexFilter("(?i)" + searchJTextField.getText()));
		sorter.setRowFilter(RowFilter.andFilter(filters));
	}
}
