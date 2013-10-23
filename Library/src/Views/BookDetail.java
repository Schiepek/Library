package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JList;

import domain.Book;
import domain.Copy;
import domain.Library;

import javax.swing.JComboBox;

import viewModels.CopyListModel;
import domain.Shelf;

import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class BookDetail extends JFrame {

	private JPanel contentPane;
	private JTextField titleJTextField;
	private JTextField authorJTextField;
	private JTextField publisherJTextField;
	private JComboBox shelfJComboBox;
	private Library library;
	private Book currentBook;
	private JButton removeSelectedJButton;
	private JList copyList;
	private JLabel titleJLabel;
	private CopyListModel copyModel;
	private JButton saveJButton;
	private JButton addCopyJButton;
	private ImageIcon erImage = new ImageIcon("src/erroricon.png");
	private JLabel errorJLabel;

	public BookDetail(Library library , Book currentBook) {
		super();
		this.library = library;
		if (currentBook == null) { currentBook = initEmptyBook(); }
		this.currentBook = currentBook;
		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private Book initEmptyBook() {
		Book tempBook = new Book("");
		return tempBook;
	}

	
	private void initGUI() {
		this.setMinimumSize(new Dimension(600, 400));
				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel bookInformationJPanel = new JPanel();
		bookInformationJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		bookInformationJPanel.setBorder(new TitledBorder(null, "Buch Informationen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(bookInformationJPanel);
		GridBagLayout gbl_bookInformationJPanel = new GridBagLayout();
		gbl_bookInformationJPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_bookInformationJPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_bookInformationJPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_bookInformationJPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		bookInformationJPanel.setLayout(gbl_bookInformationJPanel);
		
		errorJLabel = new JLabel("Bitte füllen Sie alle Felder aus.");
		errorJLabel.setIcon(erImage);
		errorJLabel.setVisible(false);
		GridBagConstraints gbc_erorrJLabel = new GridBagConstraints();
		gbc_erorrJLabel.anchor = GridBagConstraints.EAST;
		gbc_erorrJLabel.insets = new Insets(0, 0, 0, 5);
		gbc_erorrJLabel.gridx = 1;
		gbc_erorrJLabel.gridy = 4;
		bookInformationJPanel.add(errorJLabel, gbc_erorrJLabel);
		
		titleJLabel = new JLabel("Titel");
		GridBagConstraints gbc_titleJLabel = new GridBagConstraints();
		gbc_titleJLabel.anchor = GridBagConstraints.WEST;
		gbc_titleJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_titleJLabel.gridx = 0;
		gbc_titleJLabel.gridy = 0;
		bookInformationJPanel.add(titleJLabel, gbc_titleJLabel);
		
		titleJTextField = new JTextField(currentBook.getName());
		GridBagConstraints gbc_titleJTextField = new GridBagConstraints();
		gbc_titleJTextField.gridwidth = 2;
		gbc_titleJTextField.insets = new Insets(0, 0, 5, 0);
		gbc_titleJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_titleJTextField.gridx = 1;
		gbc_titleJTextField.gridy = 0;
		bookInformationJPanel.add(titleJTextField, gbc_titleJTextField);
		titleJTextField.setColumns(10);
		
		JLabel authorJLabel = new JLabel("Autor");
		GridBagConstraints gbc_authorJLabel = new GridBagConstraints();
		gbc_authorJLabel.anchor = GridBagConstraints.WEST;
		gbc_authorJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_authorJLabel.gridx = 0;
		gbc_authorJLabel.gridy = 1;
		bookInformationJPanel.add(authorJLabel, gbc_authorJLabel);
		
		authorJTextField = new JTextField(currentBook.getAuthor());
		GridBagConstraints gbc_authorJTextField = new GridBagConstraints();
		gbc_authorJTextField.gridwidth = 2;
		gbc_authorJTextField.insets = new Insets(0, 0, 5, 0);
		gbc_authorJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_authorJTextField.gridx = 1;
		gbc_authorJTextField.gridy = 1;
		bookInformationJPanel.add(authorJTextField, gbc_authorJTextField);
		authorJTextField.setColumns(10);
		
		JLabel publisherJLabel = new JLabel("Verlag");
		GridBagConstraints gbc_publisherJLabel = new GridBagConstraints();
		gbc_publisherJLabel.anchor = GridBagConstraints.WEST;
		gbc_publisherJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_publisherJLabel.gridx = 0;
		gbc_publisherJLabel.gridy = 2;
		bookInformationJPanel.add(publisherJLabel, gbc_publisherJLabel);
		
		publisherJTextField = new JTextField(currentBook.getPublisher());
		GridBagConstraints gbc_publisherJTextField = new GridBagConstraints();
		gbc_publisherJTextField.gridwidth = 2;
		gbc_publisherJTextField.insets = new Insets(0, 0, 5, 0);
		gbc_publisherJTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_publisherJTextField.gridx = 1;
		gbc_publisherJTextField.gridy = 2;
		bookInformationJPanel.add(publisherJTextField, gbc_publisherJTextField);
		publisherJTextField.setColumns(10);
		
		JLabel shelfJLabel = new JLabel("Regal");
		GridBagConstraints gbc_shelfJLabel = new GridBagConstraints();
		gbc_shelfJLabel.anchor = GridBagConstraints.EAST;
		gbc_shelfJLabel.insets = new Insets(0, 0, 5, 5);
		gbc_shelfJLabel.gridx = 0;
		gbc_shelfJLabel.gridy = 3;
		bookInformationJPanel.add(shelfJLabel, gbc_shelfJLabel);
		
		shelfJComboBox = new JComboBox();
		shelfJComboBox.setModel(new DefaultComboBoxModel(Shelf.values()));
		shelfJComboBox.setSelectedItem(currentBook.getShelf());
		GridBagConstraints gbc_shelfcomboBox = new GridBagConstraints();
		gbc_shelfcomboBox.gridwidth = 2;
		gbc_shelfcomboBox.insets = new Insets(0, 0, 5, 0);
		gbc_shelfcomboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_shelfcomboBox.gridx = 1;
		gbc_shelfcomboBox.gridy = 3;
		bookInformationJPanel.add(shelfJComboBox, gbc_shelfcomboBox);
		
		saveJButton = new JButton("Änderungen speichern");
		if (currentBook.getName().isEmpty()) {
			saveJButton.setVisible(false);
		}
		saveJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BookDetail.this.currentBook.setName(titleJTextField.getText());
					BookDetail.this.currentBook.setAuthor(authorJTextField.getText());
					BookDetail.this.currentBook.setPublisher(publisherJTextField.getText());
					BookDetail.this.currentBook.setShelf((Shelf)shelfJComboBox.getSelectedItem());
					errorJLabel.setVisible(false);
				} catch (IllegalArgumentException ex) {
					errorJLabel.setText(ex.getMessage());
					errorJLabel.setVisible(true);
				}
			}
		});
		
		
		GridBagConstraints gbc_btnSpeichern = new GridBagConstraints();
		gbc_btnSpeichern.anchor = GridBagConstraints.EAST;
		gbc_btnSpeichern.gridx = 2;
		gbc_btnSpeichern.gridy = 4;
		bookInformationJPanel.add(saveJButton, gbc_btnSpeichern);
		
		JPanel exampleJPanel = new JPanel();
		exampleJPanel.setBorder(new TitledBorder(null, "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(exampleJPanel);
		GridBagLayout gbl_ExamplePanel = new GridBagLayout();
		gbl_ExamplePanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_ExamplePanel.rowHeights = new int[]{0, 0, 0};
		gbl_ExamplePanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_ExamplePanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		exampleJPanel.setLayout(gbl_ExamplePanel);
		
		JLabel quantityLabel = new JLabel("Anzahl: " + library.getCopiesOfBook(currentBook).size());
		GridBagConstraints gbc_quantityLabel = new GridBagConstraints();
		gbc_quantityLabel.anchor = GridBagConstraints.WEST;
		gbc_quantityLabel.insets = new Insets(0, 0, 5, 5);
		gbc_quantityLabel.gridx = 0;
		gbc_quantityLabel.gridy = 0;
		exampleJPanel.add(quantityLabel, gbc_quantityLabel);
		
		removeSelectedJButton = new JButton("Ausgewählte Entfernen");
		removeSelectedJButton.setEnabled(false);
		removeSelectedJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCopy();
				if (copyList.getSelectedIndex() == -1) {
					removeSelectedJButton.setEnabled(false);
				}
			}
		});
		GridBagConstraints gbc_removeselectedButton = new GridBagConstraints();
		gbc_removeselectedButton.insets = new Insets(0, 0, 5, 5);
		gbc_removeselectedButton.gridx = 1;
		gbc_removeselectedButton.gridy = 0;
		exampleJPanel.add(removeSelectedJButton, gbc_removeselectedButton);
		
		addCopyJButton = new JButton("Exemplar hinzufügen");
		addCopyJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentBook.getName().isEmpty()) {
					try {
						createNewBook();
						saveJButton.setVisible(true);
						errorJLabel.setVisible(false);
					} catch (IllegalArgumentException ex) {
						errorJLabel.setText(ex.getMessage());
						errorJLabel.setVisible(true);
					}
				} else {
					createNewCopy();
				}
			}
		});

		GridBagConstraints gbc_addCopyButton = new GridBagConstraints();
		gbc_addCopyButton.insets = new Insets(0, 0, 5, 0);
		gbc_addCopyButton.gridx = 2;
		gbc_addCopyButton.gridy = 0;
		exampleJPanel.add(addCopyJButton, gbc_addCopyButton);
		copyModel = new CopyListModel(library, currentBook);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		exampleJPanel.add(scrollPane, gbc_scrollPane);
		
		copyList = new JList();
		copyList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (copyList.getSelectedIndices().length > 0) {
					removeSelectedJButton.setEnabled(true);
				} else {
					removeSelectedJButton.setEnabled(false);
				}
			}
		});
		scrollPane.setViewportView(copyList);
		copyList.setModel(copyModel);
	}
	
	private void createNewBook() {
		Book addedBook = new Book(titleJTextField.getText());
		addedBook.setName(titleJTextField.getText());
		addedBook.setAuthor(authorJTextField.getText());
		addedBook.setPublisher(publisherJTextField.getText());
		addedBook.setShelf((Shelf)shelfJComboBox.getSelectedItem());
		currentBook = addedBook;
		library.addBook(currentBook);
		copyModel = new CopyListModel(library, currentBook);
		copyList.setModel(copyModel);
		createNewCopy();
	}
	
	private void createNewCopy() {
		library.createAndAddCopy(currentBook);
	}
	
	private void deleteCopy() {
		if (library.bookExists(currentBook)) {
			int[] selected = copyList.getSelectedIndices();
			ArrayList<Copy> selectedCopies = new ArrayList<>();
			for(int s : selected) {
				selectedCopies.add(library.getCopiesOfBook(currentBook).get(s));
			}
			library.removeCopies(selectedCopies);
			if(!library.bookExists(currentBook)) {
				this.dispose();
			}
			copyList.clearSelection();
		}
	}

}
