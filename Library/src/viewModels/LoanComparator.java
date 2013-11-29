package viewModels;

import java.util.Comparator;

import domain.Loan;

public class LoanComparator implements Comparator<Loan> {

	@Override
	public int compare(Loan l1, Loan l2) {
		return l1.getLatestReturnDate().compareTo(l2.getLatestReturnDate());
	}

}
