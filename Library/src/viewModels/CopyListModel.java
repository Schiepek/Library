package viewModels;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class CopyListModel extends AbstractListModel implements Observer {
	
	private Library library;
	private Book currentBook;
	private List<Copy> copyList;
	
	public CopyListModel(Library library , Book book) {
		library.addObserver(this);
		this.library = library;
		this.currentBook = book;
	}	
	
	private String getLoanInformation(Copy currentCopy) {
		Loan loan = library.getLoan(currentCopy);
		if(!library.isCopyLent(currentCopy) || loan == null) { return " verfuegbar"; }
		else {
			if (!loan.isOverdue()) { return " Ausgeliehen bis " + getDateString(loan.getReturnDate()) + " (noch " + loan.getDaysOfLoanDuration() + " Tage)"; }
			else { return " Ausleihe fällig seit " + loan.getDaysOverdue() + " Tagen"; }
		}
	}
	
	private String getDateString(GregorianCalendar date) {
		return date.get(date.DATE) + "." + date.get(date.MONTH) + "." + date.get(date.YEAR);
	}
	
	@Override
	public Object getElementAt(int index) {
		if (library.getCopiesOfBook(currentBook).size()<=index){
			return null;
		}
		return library.getCopiesOfBook(currentBook).get(index).getInventoryNumber() + getLoanInformation(library.getCopiesOfBook(currentBook).get(index));
	}

	@Override
	public int getSize() {
		return library.getCopiesOfBook(currentBook).size();
	}

	@Override
	public void update(Observable o, Object arg) {
		fireContentsChanged(this, 0 , library.getCopiesOfBook(currentBook).size());
	}
	
	public List<Copy> getCopyList() {
		return copyList;
	}



}