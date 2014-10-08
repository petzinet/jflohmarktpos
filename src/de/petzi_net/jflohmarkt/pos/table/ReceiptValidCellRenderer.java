/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ReceiptValidCellRenderer extends JLabel implements TableCellRenderer {

	private final static Icon DELETE_ICON = new ImageIcon(ReceiptValidCellRenderer.class.getClassLoader().getResource("de/petzi_net/jflohmarkt/pos/editdelete.png"));
	private final static Icon REDO_ICON = new ImageIcon(ReceiptValidCellRenderer.class.getClassLoader().getResource("de/petzi_net/jflohmarkt/pos/redo.png"));
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		setText(null);
		if (((ReceiptModel) table.getModel()).isModifiable(row)) {
			if (value instanceof Boolean && ((Boolean) value)) {
				setIcon(DELETE_ICON);
			} else {
				setIcon(REDO_ICON);
			}
		} else {
			setIcon(null);
		}
		return this;
	}

}
