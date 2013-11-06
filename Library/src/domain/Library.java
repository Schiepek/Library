package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Library extends Observable implements Observer{

	private List<Copy> copies;
	private List<Customer> customers;
	private List<Loan> loans;
	private List<Book> books;
	
	private int editedBookIndex;
	private int addBookIndex;
	private int removeBookIndex;
	private int addLoanIndex;
	private int removeLoanIndex;


	public Library() {
		copies = new ArrayList<Copy>();
		customers = new ArrayList<Customer>();
		loans = new ArrayList<Loan>();
		books = new ArrayList<Book>();
	}

	public Loan createAndAddLoan(Customer customer, Copy copy) {
		if (!isCopyLent(copy)) {
			Loan l = new Loan(customer, copy);
			l.addObserver(this);
			loans.add(l);			
			addLoanIndex=getLentOutLoans().indexOf(l);
			removeLoanIndex=-1;
			addBookIndex=-1;
			editedBookIndex=-1;
			removeBookIndex=-1;
			doNotify();
			return l;
		} else {
			return null;
		}
	}

	public Customer createAndAddCustomer(String name, String surname) {
		Customer c = new Customer(name, surname);
		customers.add(c);
		doNotify();
		return c;
	}

	public Book createAndAddBook(String name) {
		Book b = new Book(name);
		b.addObserver(this);
		books.add(b);
		
		addBookIndex=books.indexOf(b);
		editedBookIndex=-1;
		removeBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		
		doNotify();
		return b;
	}
	
	public void addBook(Book book) {
		book.addObserver(this);
		books.add(book);
		
		addBookIndex=books.indexOf(book);
		editedBookIndex=-1;
		removeBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		
		doNotify();
	}

	public Copy createAndAddCopy(Book title) {
		Copy c = new Copy(title);
		copies.add(c);
		addBookIndex=-1;
		editedBookIndex=-1;
		removeBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		doNotify();
		return c;
	}

	public Book findByBookTitle(String title) {
		for (Book b : books) {
			if (b.getName().equals(title)) {
				return b;
			}
		}
		return null;
	}

	public boolean isCopyLent(Copy copy) {
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return true;
			}
		}
		return false;
	}

	public List<Copy> getCopiesOfBook(Book book) {
		List<Copy> res = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (c.getTitle().equals(book)) {
				res.add(c);
			}
		}

		return res;
	}

	public List<Loan> getLentCopiesOfBook(Book book) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCopy().getTitle().equals(book) && l.isLent()) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}

	public List<Loan> getCustomerLoans(Customer customer) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCustomer().equals(customer)) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}
	
	public List<Loan> getActiveCustomerLoans(Customer customer) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCustomer().equals(customer) && l.isLent()) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}
	
	public List<Loan> getOverdueLoans() {
		List<Loan> overdueLoans = new ArrayList<Loan>();
		for ( Loan l : getLoans() ) {
			if (l.isOverdue())
				overdueLoans.add(l);
		}
		return overdueLoans;
	}
	
	public List<Copy> getAvailableCopies(){
		return getCopies(false);
	}
	
	public List<Copy> getLentOutBooks(){
		return getCopies(true);
	}
	
	public List<Loan> getLentOutLoans(){
		List<Loan> lentLoans = new ArrayList<Loan>();
		for (Loan loan : loans) {
			if(loan.isLent()) { lentLoans.add(loan); }
		}
		return lentLoans;
	}

	private List<Copy> getCopies(boolean isLent) {
		List<Copy> retCopies = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (isLent == isCopyLent(c)) {
				retCopies.add(c);
			}
		}
		return retCopies;
	}
	
	public Loan getLoan(Copy copy) {
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return l;
			}
		}
		return null;
	}
	
	public void removeCopy(Copy copy) {
		copies.remove(copy);
		if (getCopiesOfBook(copy.getTitle()).isEmpty()) {
			removeBook(copy.getTitle());
		}
		addBookIndex=-1;
		editedBookIndex=-1;
		removeBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		doNotify();
	}
	
	public void removeCopies(List<Copy> copies) {
		for(Copy copy : copies) {
			removeCopy(copy);
		}
		addBookIndex=-1;
		editedBookIndex=-1;
		removeBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		doNotify();
	}
	
	public void removeBook(Book book) {
		book.deleteObserver(this);	
		removeBookIndex=books.indexOf(book);
		editedBookIndex=-1;
		addBookIndex=-1;
		removeLoanIndex=-1;
		addLoanIndex=-1;
		books.remove(book);
		
		doNotify();
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public List<Book> getBooks() {
		return books;
	}

	public List<Customer> getCustomers() {
		Collections.sort(customers);
		return customers;
	}
	
	public int getEditedBookPos() {
		return editedBookIndex;
	}

	public int getInsertedBookIndex() {
		return addBookIndex;
	}

	public int getRemovedBookIndex() {
		return removeBookIndex;
	}
	
	public int getAddedLoanIndex() {
		return addLoanIndex;
	}
	
	public int getRemovedLoanIndex() {
		return removeLoanIndex;
	}
	
	public boolean bookExists(Book book) {
		for(Book tempBook : getBooks()) {
			if (tempBook.equals(book)) { return true; }
		}
		return false;
	}
	
	public Copy getCopyByID(int inventoryNumber)  {
		for(Copy c : copies)  {
			if(c.getInventoryNumber() == inventoryNumber)  {
				return c;
			}	
		}
		return null;
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable obs, Object arg1) {
		if(obs instanceof Book) {
			editedBookIndex=books.indexOf(obs);
			addBookIndex=-1;
			removeBookIndex=-1;
			removeLoanIndex=-1;
			addLoanIndex=-1;
			setChanged();
			notifyObservers(obs);
		}
		if(obs instanceof Loan) {
			editedBookIndex=books.indexOf(((Loan)obs).getCopy().getTitle());
			addBookIndex=-1;
			removeBookIndex=-1;
			removeLoanIndex=-1;
			addLoanIndex=-1;
			setChanged();
			notifyObservers(obs);
		}
	}

}
