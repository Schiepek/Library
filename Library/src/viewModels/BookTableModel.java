package viewModels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import java.lang.String;

import domain.Copy;
import domain.Library;
import domain.Book;
import domain.Loan;

public class BookTableModel extends AbstractTableModel implements Observer {

	private String[] columns = { "" , "" , "" , "" };
	private Library library;
	
	public BookTableModel(Library library) {
		this.library = library;
		library.addObserver(this);
		setColumnHeaders();
	}
	
	public void setColumnHeaders(){
		columns[0] = "Verfügbar";
		columns[1] = "Titel";
		columns[2] = "Autor";
		columns[3] = "Verlag";
		fireTableStructureChanged();
	}
	
	public void propagateUpdate(int pos) {
		fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}	
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return library.getBooks().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = library.getBooks().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return getLoanInformation(library.getCopiesOfBook(book));
		case 1:
			return book.getName();
		case 2:
			return book.getAuthor();
		case 3:
			return book.getPublisher();
		default:
			return null;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int bookPos = library.getEditedBookPos();
		if (bookPos>=0){
			fireTableRowsUpdated(bookPos, bookPos);
		}else{
			bookPos = library.getRemovedBookIndex();
			if (bookPos>=0){
				fireTableRowsDeleted(bookPos, bookPos);
			}else{
				bookPos = library.getInsertedBookIndex();
				if (bookPos>=0){
					fireTableDataChanged();
					//fireTableRowsInserted(bookPos, bookPos); //hier ist noch ein Bug
				}else{
					fireTableDataChanged();
				}
			}
		}
	}
	
	@Override
	public Class getColumnClass (int columnIndex) {
		if (columnIndex <= 3 && columnIndex >= 0) { return String.class; }
	return Object.class;
	}
	
	private String getLoanInformation(List<Copy> copylist) {
		int available = 0;
		GregorianCalendar nextDate = new GregorianCalendar(00,00,00);
		for (Copy copy : copylist) {
			Loan loan = library.getLoan(copy);
			if(!library.isCopyLent(copy) || loan == null) { available++; }
			else if(!library.getLoan(copy).isOverdue()) { 
				if(nextDate.before(loan.getLatestReturnDate())) { nextDate = loan.getLatestReturnDate(); }
			}
			else { return "seit " + getDateString(loan.getLatestReturnDate()); }
		}
		if(available != 0) return Integer.toString(available);
		return "ab " + getDateString(nextDate);
	}
	
	private String getDateString(GregorianCalendar date) {
		if (date != null) {
			DateFormat f = SimpleDateFormat.getDateInstance();
			return f.format(date.getTime());
		}
		return "00.00.00";
	}

}
