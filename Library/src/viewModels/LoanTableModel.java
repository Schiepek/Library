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

public class LoanTableModel extends AbstractTableModel implements Observer {

	private String[] columns = { "" , "" , "" , "" , "" };
	private Library library;
	
	public LoanTableModel(Library library) {
		this.library = library;
		library.addObserver(this);
		setColumnHeaders();
	}
	
	public void setColumnHeaders(){
		columns[0] = "Status";
		columns[1] = "Exemplar-ID";
		columns[2] = "Titel";
		columns[3] = "Ausgeliehen Bis";
		columns[4] = "Ausgeliehen An";
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
		return library.getLentOutLoans().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Loan loan = library.getLentOutLoans().get(rowIndex);
		switch (columnIndex) {
		case 0:
			if (!loan.isOverdue()) { return "ok"; }
			else { return "fällig"; }
		case 1:
			return loan.getCopy().getInventoryNumber();
		case 2:
			return loan.getCopy().getTitle().getName();
		case 3:
			return getLoanInformation(loan);
		case 4:
			return loan.getCustomer().getSurname() + " " +  loan.getCustomer().getName();
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
		if (columnIndex == 1) { return Integer.class; }
		if (columnIndex <= 4 && columnIndex >= 0) { return String.class; }
	return Object.class;
	}
	
	private String getLoanInformation(Loan loan) {
			return getDateString(loan.getLatestReturnDate()) + getDaysInformationString(loan);
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

}