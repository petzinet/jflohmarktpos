/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.petzi_net.jflohmarkt.pos.ControlTextField;
import de.petzi_net.jflohmarkt.pos.valuetype.ValueType;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ReceiptValueCellRenderer<T> extends ControlTextField<T> implements TableCellRenderer {

	public static <V> ReceiptValueCellRenderer<V> createRenderer(ValueType<V> valueType) {
		return new ReceiptValueCellRenderer<V>(valueType);
	}
	
	public ReceiptValueCellRenderer(ValueType<T> valueType) {
		super(valueType);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		boolean negative = ((ReceiptModel) table.getModel()).isNegative(row);
		boolean marked = ((ReceiptModel) table.getModel()).isMarked(row);
		boolean valid = ((ReceiptModel) table.getModel()).isValid(row);
		boolean edit = ((ReceiptModel) table.getModel()).getCurrentEditRow() == row;
		setFont(marked ? table.getFont().deriveFont(Font.BOLD) : table.getFont());
		if (valid) {
			if (negative) {
				setForeground(Color.RED);
			} else {
				setForeground(Color.BLUE);
			}
		} else {
			setForeground(Color.GRAY);
		}
		if (edit) {
			setBackground(new Color(0, 0, 0, 64));
			setOpaque(true);
		} else {
			setOpaque(false);
		}
		setValue(value);
		return this;
	}

}
