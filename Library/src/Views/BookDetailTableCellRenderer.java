package Views;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import domain.Copy.Condition;


//class BookDetailTableCellRenderer extends JComboBox implements TableCellRenderer {

	
//}

public class BookDetailTableCellRenderer extends JComboBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public BookDetailTableCellRenderer() {
        super(Condition.values());
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            //setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setSelectedItem(value);
        return this;
    }
}

//@SuppressWarnings("serial")
//public class BookDetailTableCellEditor extends DefaultCellEditor {
//    public BookDetailTableCellEditor() {
//        super(new JComboBox(Condition.values()));
//    }
//}