package viewModels;

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
		fireTableDataChanged();	
	}
	
	@Override
	public Class getColumnClass (int columnIndex) {
		if (columnIndex <= 3 && columnIndex >= 0) { return String.class; }
	return Object.class;
	}
	
	private String getLoanInformation(List<Copy> copylist) {
		int available = 0;
		GregorianCalendar nextDate = new GregorianCalendar(99999,1,1);
		for (Copy copy : copylist) {
			Loan loan = library.getLoan(copy);
			if(!library.isCopyLent(copy) || loan == null) { available++; }
			else if(!library.getLoan(copy).isOverdue()) { 
				if(nextDate.after(loan.getReturnDate())) { nextDate = loan.getReturnDate(); }
			}
			else { return "überzogen"; }
		}
		if(available != 0) return Integer.toString(available);
		return "ab " + getDateString(nextDate);
		
		
//		Loan loan = library.getLoan(currentCopy);
//		if(!library.isCopyLent(currentCopy) || loan == null) { return " verfuegbar"; }
//		else {
//			if (!loan.isOverdue()) { return " Ausgeliehen bis " + getDateString(loan.getReturnDate()) + " (noch " + loan.getDaysOfLoanDuration() + " Tage)"; }
//			else { return " Ausleihe fällig seit " + loan.getDaysOverdue() + " Tagen"; }
//		}
	}
	
	private String getDateString(GregorianCalendar date) {
		return date.get(date.DATE) + "." + date.get(date.MONTH) + "." + date.get(date.YEAR);
	}

}
