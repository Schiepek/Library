package viewModels;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import domain.Loan;

@SuppressWarnings("serial")
public class DateRenderer extends DefaultTableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		value = getLoanInformation((Loan)value);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
