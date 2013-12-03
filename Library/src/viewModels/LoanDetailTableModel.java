package viewModels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import domain.Library;
import domain.Loan;

@SuppressWarnings("serial")
public class LoanDetailTableModel extends AbstractTableModel implements Observer {

	private String[] columns = { "" , "" , "" , "" };
	private Library library;
	private Loan loan;
	
	public LoanDetailTableModel(Library library, Loan loan) {
		this.library = library;
		this.loan = loan;
		library.addObserver(this);
		setColumnHeaders();
	}
	
	public void setColumnHeaders(){
		columns[0] = "Status";
		columns[1] = "Ausgeliehen Bis";
		columns[2] = "Exemplar-ID";
		columns[3] = "Titel";
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
		return library.getActiveCustomerLoans(loan.getCustomer()).size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Loan l = library.getActiveCustomerLoans(loan.getCustomer()).get(rowIndex);
		switch (columnIndex) {
		case 0:
			if (!l.isOverdue()) { return "ok"; }
			else { return "fällig"; }
		case 1:
			return getLoanInformation(l);
		case 2:
			return l.getCopy().getInventoryNumber();
		case 3:
			return l.getCopy().getTitle().getName();
		default:
			return null;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		fireTableDataChanged();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass (int columnIndex) {
		if (columnIndex == 1) { return Integer.class; }
		if (columnIndex <= 4 && columnIndex >= 0) { return String.class; }
	return Object.class;
	}
	
	private String getLoanInformation(Loan l) {
			return getDateString(l.getLatestReturnDate()) + getDaysInformationString(l);
	}
	
	private String getDateString(GregorianCalendar date) {
		if (date != null) {
			DateFormat f = SimpleDateFormat.getDateInstance();
			return f.format(date.getTime());
		}
		return "00.00.00";
	}
	
	private String getDaysInformationString(Loan l) {
		if(!l.isOverdue()) {
			switch (l.getDaysTillReturn()) {
				case 0: return " (heute)";
				case 1: return " (noch 1 Tag)";
				default: return " (noch " + l.getDaysTillReturn() + " Tage)";
			}
		}
		else {
			switch (l.getDaysOverdue()) {
				case 0: return " (seit heute)";
				case 1: return " (seit 1 Tag)";
				default: return " (seit " + l.getDaysOverdue() + " Tagen)";
			}		
		}
	}
}
