package Views;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class LoanTableCellRenderer extends DefaultTableCellRenderer {

	private static ImageIcon[] icons;
	
	LoanTableCellRenderer () {
		icons = new ImageIcon[2];
		try {
			icons[0] = new ImageIcon(ImageIO.read(new File("images/available.png")));
			icons[1] = new ImageIcon(ImageIO.read(new File("images/not_available.png")));
		} catch (IOException e) {
			
		}
	}
	
	@Override
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(((String)value).equals("ok"))  {
	    	setIcon(icons[0]);
	    }
	    else  {
	    	setIcon(icons[1]);
	    }
		Component cellRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    
	    return cellRenderer;
	}

}
