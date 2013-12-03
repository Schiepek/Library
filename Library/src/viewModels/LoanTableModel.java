package viewModels;

import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import java.lang.String;

import domain.Library;
import domain.Loan;

@SuppressWarnings("serial")
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
			return loan;
		case 4:
			return loan.getCustomer().getSurname() + " " +  loan.getCustomer().getName();
		default:
			return null;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int loanPos = library.getRemovedLoanIndex();
		if (loanPos>=0){
			fireTableRowsDeleted(loanPos, loanPos);
		}else{
			loanPos = library.getAddedLoanIndex();
			if (loanPos>=0){
				fireTableRowsInserted(loanPos, loanPos);
			}else{
				fireTableDataChanged();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass (int columnIndex) {
		if (columnIndex == 1) { return Integer.class; }
		if (columnIndex == 3) { return GregorianCalendar.class; };
		if (columnIndex <= 4 && columnIndex >= 0) { return String.class; }
	return Object.class;
	}

}
