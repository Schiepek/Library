package domain;

import java.util.Observable;

public class Copy extends Observable {
	
	public enum Condition {NEW, GOOD, DAMAGED, WASTE, LOST }
	
	public static long nextInventoryNumber = 1;
	
	private final long inventoryNumber;
	private final Book book;
	private Condition condition;
	
	public Copy(Book title) {
		this.book = title;
		inventoryNumber = nextInventoryNumber++;
		condition = Condition.NEW;
	}

	public Book getTitle() {
		return book;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
		doNotify();
	}

	public long getInventoryNumber() {
		return inventoryNumber;
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}
}
