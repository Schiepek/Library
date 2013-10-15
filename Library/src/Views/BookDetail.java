package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BookDetail extends JFrame {

	private JPanel contentPane;
	private JTextField Titletextfield;
	private JTextField Auhortextfield;
	private JTextField Pubishertextfield;
	private JComboBox shelfcomboBox;
	private Library library;
	private Book currentBook;
	JList copyList;
	CopyListModel copyModel;

	/**
	 * Launch the application.
	 */
	private void initBookDetail() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookDetail frame = new BookDetail(library,currentBook);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BookDetail(final Library library , Book currentBook) {
		this.library = library;
		this.currentBook = currentBook;
		if (currentBook == null) { currentBook = initEmptyBook(); }
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel BookInformationPanel = new JPanel();
		BookInformationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		BookInformationPanel.setBorder(new TitledBorder(null, "Buch Informationen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(BookInformationPanel);
		GridBagLayout gbl_BookInformationPanel = new GridBagLayout();
		gbl_BookInformationPanel.columnWidths = new int[]{0, 0, 0};
		gbl_BookInformationPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_BookInformationPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_BookInformationPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		BookInformationPanel.setLayout(gbl_BookInformationPanel);
		
		JLabel TitleLabel = new JLabel("Titel");
		GridBagConstraints gbc_TitleLabel = new GridBagConstraints();
		gbc_TitleLabel.anchor = GridBagConstraints.WEST;
		gbc_TitleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_TitleLabel.gridx = 0;
		gbc_TitleLabel.gridy = 0;
		BookInformationPanel.add(TitleLabel, gbc_TitleLabel);
		
		Titletextfield = new JTextField(currentBook.getName());
		GridBagConstraints gbc_Titletextfield = new GridBagConstraints();
		gbc_Titletextfield.insets = new Insets(0, 0, 5, 0);
		gbc_Titletextfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_Titletextfield.gridx = 1;
		gbc_Titletextfield.gridy = 0;
		BookInformationPanel.add(Titletextfield, gbc_Titletextfield);
		Titletextfield.setColumns(10);
		
		JLabel AuthorLabel = new JLabel("Autor");
		GridBagConstraints gbc_AuthorLabel = new GridBagConstraints();
		gbc_AuthorLabel.anchor = GridBagConstraints.WEST;
		gbc_AuthorLabel.insets = new Insets(0, 0, 5, 5);
		gbc_AuthorLabel.gridx = 0;
		gbc_AuthorLabel.gridy = 1;
		BookInformationPanel.add(AuthorLabel, gbc_AuthorLabel);
		
		Auhortextfield = new JTextField(currentBook.getAuthor());
		GridBagConstraints gbc_Auhortextfield = new GridBagConstraints();
		gbc_Auhortextfield.insets = new Insets(0, 0, 5, 0);
		gbc_Auhortextfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_Auhortextfield.gridx = 1;
		gbc_Auhortextfield.gridy = 1;
		BookInformationPanel.add(Auhortextfield, gbc_Auhortextfield);
		Auhortextfield.setColumns(10);
		
		JLabel Publisherlabel = new JLabel("Verlag");
		GridBagConstraints gbc_Publisherlabel = new GridBagConstraints();
		gbc_Publisherlabel.anchor = GridBagConstraints.WEST;
		gbc_Publisherlabel.insets = new Insets(0, 0, 5, 5);
		gbc_Publisherlabel.gridx = 0;
		gbc_Publisherlabel.gridy = 2;
		BookInformationPanel.add(Publisherlabel, gbc_Publisherlabel);
		
		Pubishertextfield = new JTextField(currentBook.getPublisher());
		GridBagConstraints gbc_Pubishertextfield = new GridBagConstraints();
		gbc_Pubishertextfield.insets = new Insets(0, 0, 5, 0);
		gbc_Pubishertextfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_Pubishertextfield.gridx = 1;
		gbc_Pubishertextfield.gridy = 2;
		BookInformationPanel.add(Pubishertextfield, gbc_Pubishertextfield);
		Pubishertextfield.setColumns(10);
		
		JLabel Shelflabel = new JLabel("Regal");
		GridBagConstraints gbc_Shelflabel = new GridBagConstraints();
		gbc_Shelflabel.anchor = GridBagConstraints.EAST;
		gbc_Shelflabel.insets = new Insets(0, 0, 0, 5);
		gbc_Shelflabel.gridx = 0;
		gbc_Shelflabel.gridy = 3;
		BookInformationPanel.add(Shelflabel, gbc_Shelflabel);
		
		shelfcomboBox = new JComboBox();
		shelfcomboBox.setModel(new DefaultComboBoxModel(Shelf.values()));
		shelfcomboBox.setSelectedItem(currentBook.getShelf());
		GridBagConstraints gbc_shelfcomboBox = new GridBagConstraints();
		gbc_shelfcomboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_shelfcomboBox.gridx = 1;
		gbc_shelfcomboBox.gridy = 3;
		BookInformationPanel.add(shelfcomboBox, gbc_shelfcomboBox);
		
		JPanel ExamplePanel = new JPanel();
		ExamplePanel.setBorder(new TitledBorder(null, "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(ExamplePanel);
		GridBagLayout gbl_ExamplePanel = new GridBagLayout();
		gbl_ExamplePanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_ExamplePanel.rowHeights = new int[]{0, 0, 0};
		gbl_ExamplePanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_ExamplePanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		ExamplePanel.setLayout(gbl_ExamplePanel);
		
		JLabel quantityLabel = new JLabel("Anzahl: " + library.getCopiesOfBook(currentBook).size());
		GridBagConstraints gbc_quantityLabel = new GridBagConstraints();
		gbc_quantityLabel.anchor = GridBagConstraints.WEST;
		gbc_quantityLabel.insets = new Insets(0, 0, 5, 5);
		gbc_quantityLabel.gridx = 0;
		gbc_quantityLabel.gridy = 0;
		ExamplePanel.add(quantityLabel, gbc_quantityLabel);
		
		JButton removeselectedButton = new JButton("Ausgew\u00E4hlte Entfernen");
		removeselectedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCopy();
			}
		});
		GridBagConstraints gbc_removeselectedButton = new GridBagConstraints();
		gbc_removeselectedButton.insets = new Insets(0, 0, 5, 5);
		gbc_removeselectedButton.gridx = 1;
		gbc_removeselectedButton.gridy = 0;
		ExamplePanel.add(removeselectedButton, gbc_removeselectedButton);
		
		JButton addCopyButton = new JButton("Exemplar hinzuf\u00FCgen");
		addCopyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (library.findByBookTitle(Titletextfield.getText()) == null) {createNewBook();}
				else { createNewCopy(); }
			}
		});

		GridBagConstraints gbc_addCopyButton = new GridBagConstraints();
		gbc_addCopyButton.insets = new Insets(0, 0, 5, 0);
		gbc_addCopyButton.gridx = 2;
		gbc_addCopyButton.gridy = 0;
		ExamplePanel.add(addCopyButton, gbc_addCopyButton);
		copyModel = new CopyListModel(library, currentBook);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		ExamplePanel.add(scrollPane, gbc_scrollPane);
		
		copyList = new JList();
		scrollPane.setViewportView(copyList);
		copyList.setModel(copyModel);
	}
	
	private Book initEmptyBook() {
		Book tempBook = new Book("leer");
		tempBook.setAuthor("leer");
		tempBook.setPublisher("leer");
		tempBook.setShelf(Shelf.A1);
		return tempBook;
	}
	
	private void createNewBook() {
		Book addedBook = library.createAndAddBook(Titletextfield.getText());
		addedBook.setAuthor(Auhortextfield.getText());
		addedBook.setPublisher(Pubishertextfield.getText());
		addedBook.setShelf((Shelf)shelfcomboBox.getSelectedItem());
		currentBook = addedBook;
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
			if(!library.bookExists(currentBook)) this.dispose();
		}
	}
}
