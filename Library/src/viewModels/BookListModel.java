package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.Library;

public class BookListModel extends AbstractListModel implements Observer {
	
	private Library library;
	
	public BookListModel(Library library) {
		this.library = library;
		library.addObserver(this);
	}	
	
	@Override
	public Object getElementAt(int index) {
		if (library.getBooks().size()<=index){
			return null;
		}
		return library.getBooks().get(index);
	}

	@Override
	public int getSize() {
		return library.getBooks().size();
	}

	@Override
	public void update(Observable o, Object arg) {
		fireContentsChanged(this, 0 , library.getBooks().size());
	}

}
