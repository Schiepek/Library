package Views;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.Copy.Condition;
import viewModels.CustomerComboBoxModel;
import viewModels.LoanDetailTableModel;

@SuppressWarnings("serial")
public class LoanDetail extends JFrame {

	private JPanel contentPane;
	private Loan currentLoan;
	private Library library;
	private Customer customer;
	private JTextField copyIDJTextField;
	private LoanDetailTableModel loanModel;
	private JTable loanTable;
	private JLabel titleValueJLabel;
	private JLabel statusValueJLabel;
	private JLabel authorValueJLabel;
	private JComboBox customerJComboBox;
	private CustomerComboBoxModel customerBoxModel;
	private JButton loanJButton;
	private JPanel loanJPanel;
	private JLabel numberOfLoanJLabel;
	private ImageIcon warningImage = new ImageIcon("images/warning.png");
	private ImageIcon applyImage = new ImageIcon("images/apply.png");
	private JButton returnJButton;
	private ArrayList<Customer> openedCustomers;


	public LoanDetail(Library library, Loan currentLoan, ArrayList<Customer> openedCustomers) {
		super();
		if (currentLoan == null) { currentLoan = initEmptyLoan(null); setTitle("Neue Ausleihe erfassen"); }
		else { setTitle(currentLoan.getCustomer().getFullName()); }
		this.currentLoan = currentLoan;
		this.library = library;
		this.customer = currentLoan.getCustomer();
		this.openedCustomers = openedCustomers;		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private Loan initEmptyLoan(Customer c) {
		Loan tempLoan = new Loan(null,null);
		return tempLoan;
	}
	
	private void initGUI() {
		this.setMinimumSize(new Dimension(600, 400));
		this.addWindowListener(new java.awt.event.WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) { disposeLoanDetail(); }
		});

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        	
	        getRootPane().getActionMap().put("Cancel", new AbstractAction(){
	            public void actionPerformed(ActionEvent e)
	            {
	            	Window w = LoanDetail.this;
	            	w.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
	            }
	        }
        );

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        	
	        getRootPane().getActionMap().put("Enter", new AbstractAction(){
	            public void actionPerformed(ActionEvent e)
	            {
	            	if (loanJButton.isEnabled()) {
	            		for(ActionListener a:  loanJButton.getActionListeners()) {
		            	    a.actionPerformed(e);
		            	}
	            	}
	            	
	            }
	        }
        );
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel customerJPanel = new JPanel();
		customerJPanel.setBorder(new TitledBorder(null, "Kundenauswahl", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(customerJPanel);
		GridBagLayout gbl_customerJPanel = new GridBagLayout();
		gbl_customerJPanel.columnWidths = new int[]{0, 0, 0};
		gbl_customerJPanel.rowHeights = new int[]{0, 0};
		gbl_customerJPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_customerJPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		customerJPanel.setLayout(gbl_customerJPanel);
		
		JLabel customerJLabel = new JLabel("Kunde");
		GridBagConstraints gbc_customerJLabel = new GridBagConstraints();
		gbc_customerJLabel.anchor = GridBagConstraints.EAST;
		gbc_customerJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_customerJLabel.gridx = 0;
		gbc_customerJLabel.gridy = 0;
		customerJPanel.add(customerJLabel, gbc_customerJLabel);
		
		
		customerBoxModel = new CustomerComboBoxModel(library);
		customerJComboBox = new JComboBox(customerBoxModel);
		if (customer == null) {
			customerJComboBox.setSelectedItem("");
		} else {
			customerJComboBox.setSelectedItem(customer.getFullName());
		}
		
		customerJComboBox.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Customer oldCustomer = customer;
				customer = library.getCustomers().get(customerJComboBox.getSelectedIndex());
				if (library.getActiveCustomerLoans(customer).size() > 0) {
					currentLoan = library.getActiveCustomerLoans(customer).get(0);
				} else {
					currentLoan = initEmptyLoan(customer);
				}
				changeOpenedCustomers(oldCustomer,customer);
				updateGUI(customer);
			}
		});
		GridBagConstraints gbc_customerJComboBox = new GridBagConstraints();
		gbc_customerJComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_customerJComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_customerJComboBox.gridx = 1;
		gbc_customerJComboBox.gridy = 0;
		customerJPanel.add(customerJComboBox, gbc_customerJComboBox);
		
		JPanel copyJPanel = new JPanel();
		copyJPanel.setBorder(new TitledBorder(null, "Neues Exemplar ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(copyJPanel);
		GridBagLayout gbl_copyJPanel = new GridBagLayout();
		gbl_copyJPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_copyJPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_copyJPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_copyJPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		copyJPanel.setLayout(gbl_copyJPanel);
		
		JLabel copyIDJLabel = new JLabel("Exemplar-ID");
		GridBagConstraints gbc_copyIDJLabel = new GridBagConstraints();
		gbc_copyIDJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_copyIDJLabel.anchor = GridBagConstraints.WEST;
		gbc_copyIDJLabel.gridx = 0;
		gbc_copyIDJLabel.gridy = 0;
		copyJPanel.add(copyIDJLabel, gbc_copyIDJLabel);
		
		copyIDJTextField = new JTextField();
		GridBagConstraints gbc_copyiDJTextField = new GridBagConstraints();
		gbc_copyiDJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_copyiDJTextField.insets = new Insets(0, 0, 5, 5);
		gbc_copyiDJTextField.gridx = 1;
		gbc_copyiDJTextField.gridy = 0;
		copyJPanel.add(copyIDJTextField, gbc_copyiDJTextField);
		copyIDJTextField.setColumns(10);
		copyIDJTextField.getDocument().addDocumentListener(new DocumentListener()  {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateGUI(currentLoan.getCustomer());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateGUI(currentLoan.getCustomer());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {	
				updateGUI(currentLoan.getCustomer());
			}
		});
		
		loanJButton = new JButton("Exemplar ausleihen");
		loanJButton.setEnabled(false);
		loanJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Copy copy = library.getCopyByID(Integer.parseInt(copyIDJTextField.getText()));
				customer = library.getCustomers().get(customerJComboBox.getSelectedIndex());
				Loan addedLoan = library.createAndAddLoan(customer, copy);
				currentLoan = addedLoan;
				loanModel = new LoanDetailTableModel(library, currentLoan);
				loanTable.setModel(loanModel);
				copyIDJTextField.setText("");
				updateGUI(customer);

			}
		});
		GridBagConstraints gbc_loanJButton = new GridBagConstraints();
		gbc_loanJButton.insets = new Insets(0, 0, 5, 0);
		gbc_loanJButton.gridx = 2;
		gbc_loanJButton.gridy = 0;
		copyJPanel.add(loanJButton, gbc_loanJButton);
		
		JLabel statusJLabel = new JLabel("Status");
		GridBagConstraints gbc_statusJLabel = new GridBagConstraints();
		gbc_statusJLabel.anchor = GridBagConstraints.WEST;
		gbc_statusJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_statusJLabel.gridx = 0;
		gbc_statusJLabel.gridy = 1;
		copyJPanel.add(statusJLabel, gbc_statusJLabel);
		
		statusValueJLabel = new JLabel("-");
		GridBagConstraints gbc_statusValueJLabel = new GridBagConstraints();
		gbc_statusValueJLabel.anchor = GridBagConstraints.WEST;
		gbc_statusValueJLabel.gridwidth = 2;
		gbc_statusValueJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_statusValueJLabel.gridx = 1;
		gbc_statusValueJLabel.gridy = 1;
		copyJPanel.add(statusValueJLabel, gbc_statusValueJLabel);
		
		JLabel titleJLabel = new JLabel("Titel");
		GridBagConstraints gbc_titleJLabel = new GridBagConstraints();
		gbc_titleJLabel.anchor = GridBagConstraints.WEST;
		gbc_titleJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_titleJLabel.gridx = 0;
		gbc_titleJLabel.gridy = 2;
		copyJPanel.add(titleJLabel, gbc_titleJLabel);
		
		titleValueJLabel = new JLabel("-");
		GridBagConstraints gbc_titleValueJLabel = new GridBagConstraints();
		gbc_titleValueJLabel.anchor = GridBagConstraints.WEST;
		gbc_titleValueJLabel.gridwidth = 2;
		gbc_titleValueJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_titleValueJLabel.gridx = 1;
		gbc_titleValueJLabel.gridy = 2;
		copyJPanel.add(titleValueJLabel, gbc_titleValueJLabel);
		
		JLabel authorJLabel = new JLabel("Autor");
		GridBagConstraints gbc_authorJLabel = new GridBagConstraints();
		gbc_authorJLabel.anchor = GridBagConstraints.WEST;
		gbc_authorJLabel.insets = new Insets(0, 0, 0, 5);
		gbc_authorJLabel.gridx = 0;
		gbc_authorJLabel.gridy = 3;
		copyJPanel.add(authorJLabel, gbc_authorJLabel);
		
		authorValueJLabel = new JLabel("-");
		GridBagConstraints gbc_authorValueJLabel = new GridBagConstraints();
		gbc_authorValueJLabel.anchor = GridBagConstraints.WEST;
		gbc_authorValueJLabel.gridwidth = 2;
		gbc_authorValueJLabel.insets = new Insets(0, 0, 0, 5);
		gbc_authorValueJLabel.gridx = 1;
		gbc_authorValueJLabel.gridy = 3;
		copyJPanel.add(authorValueJLabel, gbc_authorValueJLabel);
		
		loanJPanel = new JPanel();
		if (customer == null) {
			loanJPanel.setBorder(new TitledBorder(null, "Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		} else {
			loanJPanel.setBorder(new TitledBorder(null, "Ausleihen von " + currentLoan.getCustomer().getFullName(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		}
		
		contentPane.add(loanJPanel);
		GridBagLayout gbl_loanJPanel = new GridBagLayout();
		gbl_loanJPanel.columnWidths = new int[]{0, 0, 0};
		gbl_loanJPanel.rowHeights = new int[]{0, 0, 0};
		gbl_loanJPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_loanJPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		loanJPanel.setLayout(gbl_loanJPanel);

		
		numberOfLoanJLabel = new JLabel("Anzahl Ausleihen: "  + library.getActiveCustomerLoans(currentLoan.getCustomer()).size());
		GridBagConstraints gbc_numberOfLoanJLabel = new GridBagConstraints();
		gbc_numberOfLoanJLabel.anchor = GridBagConstraints.WEST;
		gbc_numberOfLoanJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_numberOfLoanJLabel.gridx = 0;
		gbc_numberOfLoanJLabel.gridy = 0;
		loanJPanel.add(numberOfLoanJLabel, gbc_numberOfLoanJLabel);
		
		returnJButton = new JButton("Zurückgeben");
		returnJButton.setEnabled(false);
		returnJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnLoan();
			}
		});
		GridBagConstraints gbc_returnJButton = new GridBagConstraints();
		gbc_returnJButton.insets = new Insets(0, 0, 5, 0);
		gbc_returnJButton.gridx = 1;
		gbc_returnJButton.gridy = 0;
		loanJPanel.add(returnJButton, gbc_returnJButton);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		loanJPanel.add(scrollPane, gbc_scrollPane);
		
		loanModel = new LoanDetailTableModel(library, currentLoan);
		loanTable = new JTable();
		scrollPane.setViewportView(loanTable);
		loanTable.setModel(loanModel);
		loanTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (loanTable.getSelectedRowCount() > 0) {
					returnJButton.setEnabled(true);
				} else {
					returnJButton.setEnabled(false);
				}
			}
		});
		getRootPane().setDefaultButton(loanJButton);
		updateGUI(currentLoan.getCustomer());
		
	}

	private void updateGUI(Customer c)  {
		try  {
			if (c == null && (customerJComboBox.getSelectedIndex() == -1)) {
				statusValueJLabel.setText("Bitte einen Kunden auswählen.");
				statusValueJLabel.setIcon(warningImage);
				return;
			} else if (c == null) {
				c = library.getCustomers().get(customerJComboBox.getSelectedIndex());
			}
			setTitle(c.getFullName());
			loanModel = new LoanDetailTableModel(library, currentLoan);
			loanTable.setModel(loanModel);
			loanTable.getColumnModel().getColumn(0).setPreferredWidth(50);
			loanTable.getColumnModel().getColumn(1).setPreferredWidth(180);
			loanTable.getColumnModel().getColumn(2).setPreferredWidth(80);
			loanTable.getColumnModel().getColumn(3).setPreferredWidth(300);
			loanTable.getColumnModel().getColumn(0).setMinWidth(50);
			loanTable.getColumnModel().getColumn(1).setMinWidth(180);
			loanTable.getColumnModel().getColumn(2).setMinWidth(80);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.LEFT );
			loanTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
			loanTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
			titleValueJLabel.setText("-");
			authorValueJLabel.setText("-");
			numberOfLoanJLabel.setText("Anzahl Ausleihen: "  + library.getActiveCustomerLoans(c).size());
			loanJPanel.setBorder(new TitledBorder(null, "Ausleihen von " + c.getFullName(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			
			if(copyIDJTextField.getText().isEmpty())  {
				List<Loan> customerLoans = library.getActiveCustomerLoans(c);
				
				if(customerLoans.size() >= 3)  {
					statusValueJLabel.setText("Der Kunde hat bereits 3 Bücher ausgeliehen.");
				}
				else  {
					boolean overdue = false;
					for(Loan l : customerLoans)  {
						if(l.isOverdue())  {
							overdue = true;
						}
					}
				
					if(overdue)  {
						statusValueJLabel.setText("Der Kunde hat eine überfällige Ausleihe.");
					} else {
						statusValueJLabel.setText("Bitte geben Sie die gewünschte Exemplar-ID ein.");
					}
				}
				
				
			}
			else  {
				Copy copy = library.getCopyByID(Integer.parseInt(copyIDJTextField.getText()));
				if(copy == null)  {
					statusValueJLabel.setText("Kein Exemplar mit der gewünschten ID gefunden.");
				}
				else  {
					titleValueJLabel.setText(copy.getTitle().getName());
					authorValueJLabel.setText(copy.getTitle().getAuthor());
					
					List<Loan> customerLoans = library.getActiveCustomerLoans(c);
					
					if(customerLoans.size() >= 3)  {
						statusValueJLabel.setText("Der Kunde hat bereits 3 Bücher ausgeliehen.");
					}
					else  {
						boolean overdue = false;
						for(Loan l : customerLoans)  {
							if(l.isOverdue())  {
								overdue = true;
							}
						}
					
						if(overdue)  {
							statusValueJLabel.setText("Der Kunde hat eine überfällige Ausleihe.");
						}
						else if (copy.getCondition().equals(Condition.LOST)) {
							statusValueJLabel.setText("Dieses Exemplar ist leider verloren gegangen.");
						}
						else if(library.isCopyLent(copy))  {
							if (library.getLoan(copy).isOverdue()) {
								statusValueJLabel.setText("Dieses Exemplar ist überfällig seit " + getDateString(library.getLoan(copy).getLatestReturnDate()));
							} else {
								statusValueJLabel.setText("Dieses Exemplar ist ausgeliehen bis " + getDateString(library.getLoan(copy).getLatestReturnDate()));
							}
						}
						else  {
								statusValueJLabel.setText("Verfügbar");
								statusValueJLabel.setIcon(applyImage);
								loanJButton.setEnabled(true);
								return;
						}
						
					}	
				}
			}
		}
		catch(Exception e)  {
			statusValueJLabel.setText("Diese Exemplar-ID existiert nicht.");
		}
		loanJButton.setEnabled(false);
		statusValueJLabel.setIcon(warningImage);
	}
	
	private String getDateString(GregorianCalendar date) {
		if (date != null) {
			DateFormat f = SimpleDateFormat.getDateInstance();
			return f.format(date.getTime());
		}
		return "00.00.00";
	}
	
	private void returnLoan(){
		int[] selected = loanTable.getSelectedRows();
		ArrayList<Loan> selectedLoans = new ArrayList<Loan>();
		for(int s : selected) selectedLoans.add(library.getActiveCustomerLoans(currentLoan.getCustomer()).get(s));
		for(Loan returnLoan : selectedLoans){
			if (returnLoan.isOverdue())	JOptionPane.showMessageDialog(null, "Für " + customer.getFullName() + " fällt eine Mahngebühr von CHF 3.- an", "Ausleihe überfällig!", 2);
			returnLoan.returnCopy();
		}
		loanTable.clearSelection();
	}
	
	private void disposeLoanDetail() {
		openedCustomers.remove(customer);
		this.dispose();
	}
	
	private void changeOpenedCustomers(Customer oldCustomer, Customer newCustomer) {
		if(oldCustomer==null) openedCustomers.remove(oldCustomer);
		openedCustomers.add(newCustomer);
	}

}
