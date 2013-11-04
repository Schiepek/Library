package viewModels;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import domain.Library;

public class CustomerComboBoxModel extends AbstractListModel implements ComboBoxModel {

	private Library library;
	private String customer = null;
	
	public CustomerComboBoxModel(Library l)  {
		library = l;
	}
	
	public Object getElementAt(int index)  {
		return library.getCustomers().get(index).getFullName();
	}
	
	public int getSize()  {
		return library.getCustomers().size();
	}
	
	public void setSelectedItem(Object o)  {
		customer = (String)o;
	}
	
	public Object getSelectedItem()  {
		return customer;
	}

}
