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

public class CopyTableModel extends AbstractTableModel implements Observer {

	private Library library;
	private Book currentBook;
	private String[] columns = { "" , "" , "" };

	public CopyTableModel(Library library, Book currentBook) {
		library.addObserver(this);
		this.library = library;
		this.currentBook = currentBook;
		setColumnHeaders();
	}
	
	public void setColumnHeaders(){
		columns[0] = "Id-Nr.";
		columns[1] = "Zustand";
		columns[2] = "Status";
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
		return library.getCopiesOfBook(currentBook).size();
	}
	
	@Override
	public Class getColumnClass (int columnIndex) {
		if (columnIndex == 0) { return Integer.class; }
		if (columnIndex <= 2 && columnIndex >= 1) { return String.class; }
		return Object.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return library.getCopiesOfBook(currentBook).get(rowIndex).getInventoryNumber();
		case 1:
			return library.getCopiesOfBook(currentBook).get(rowIndex).getCondition();
		case 2:
			return getLoanInformation(library.getCopiesOfBook(currentBook).get(rowIndex));
		default:
			return null;
		}
	}
	
	private String getLoanInformation(Copy currentCopy) {
		Loan loan = library.getLoan(currentCopy);
		if(!library.isCopyLent(currentCopy) || loan == null) { return " verfuegbar"; }
		else {
			if (!loan.isOverdue()) { return "Ausgeliehen bis " + getDateString(loan.getLatestReturnDate()) + getDaysInformationString(loan); }
			else { return "Überzogen seit " + getDateString(loan.getLatestReturnDate()) + getDaysInformationString(loan); }
		}
	}
	
	private String getDateString(GregorianCalendar date) {
		if (date != null) {
			DateFormat f = SimpleDateFormat.getDateInstance();
			return f.format(date.getTime());
		}
		return "00.00.00";
	}
	
	private String getDaysInformationString(Loan loan) {
		if(!loan.isOverdue()) {
			switch (loan.getDaysTillReturn()) {
				case 0: return " (heute)";
				case 1: return " (noch 1 Tag)";
				default: return " (noch " + loan.getDaysTillReturn() + " Tage)";
			}
		}
		else {
			switch (loan.getDaysOverdue()) {
				case 0: return " (seit heute)";
				case 1: return " (seit 1 Tag)";
				default: return " (seit " + loan.getDaysOverdue() + " Tagen)";
			}		
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		fireTableDataChanged();
		
	}

	
}