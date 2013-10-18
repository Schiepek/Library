package domain;

import java.util.Observable;

public class Book extends Observable {
	
	private String title, author, publisher;
	private Shelf shelf;
	
	public Book(String name) {
		this.title = name;
		doNotify();
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		if (name.isEmpty() || name == "") {
			throw new IllegalArgumentException("Bitte Titel des Buches eingeben");
		}
		this.title = name;
		doNotify();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String autor) {
		if (autor.isEmpty() || autor == "") {
			throw new IllegalArgumentException("Bitte Autor des Buches eingeben");
		}
		this.author = autor;
		doNotify();
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		if (publisher.isEmpty() || publisher == "") {
			throw new IllegalArgumentException("Bitte Verlag des Buches eingeben");
		}
		this.publisher = publisher;
		doNotify();
	}
	
	public Shelf getShelf() {
		return shelf;
	}
	
	public void setShelf(Shelf shelf) {
		if (shelf == null) {
			throw new IllegalArgumentException("Bitte Regal des Buches eingeben");
		}
		this.shelf = shelf;
		doNotify();
	}
	
	@Override
	public String toString() {
		return title + ", " + author + ", " + publisher;
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}
}
