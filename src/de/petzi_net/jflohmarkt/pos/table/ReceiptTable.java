/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import de.petzi_net.jflohmarkt.pos.valuetype.ValueType;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ReceiptTable extends JTable {

	public ReceiptTable(final ReceiptModel receiptModel) {
		super();
		setFocusable(false);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		setTableHeader(null);
		setFont(new Font("Monospaced", Font.PLAIN, 40));
		setForeground(Color.BLACK);
		setBackground(new Color(0, 0, 0, 0));
		setShowGrid(false);
		setRowHeight(getFontMetrics(getFont()).getHeight() + 2);
		setModel(receiptModel);
		
		{
			TableColumn tableColumn = getColumnModel().getColumn(0);
			tableColumn.setCellRenderer(new ReceiptValidCellRenderer());
			int width = 32 + 2;
			tableColumn.setMinWidth(width);
			tableColumn.setMaxWidth(width);
			tableColumn.setPreferredWidth(width);
			tableColumn.setWidth(width);
		}
		for (int col = 1; col < receiptModel.getColumnCount(); col++) {
			ValueType<?> valueType = receiptModel.getColumnValueType(col);
			TableColumn tableColumn = getColumnModel().getColumn(col);
			tableColumn.setCellRenderer(ReceiptValueCellRenderer.createRenderer(valueType));
			int width = valueType.getMaxLength() * getFontMetrics(getFont()).charWidth('W') + 2;
			tableColumn.setMinWidth(width);
			tableColumn.setMaxWidth(width);
			tableColumn.setPreferredWidth(width);
			tableColumn.setWidth(width);
		}
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = convertRowIndexToModel(rowAtPoint(e.getPoint()));
				int column = convertColumnIndexToModel(columnAtPoint(e.getPoint()));
				if (receiptModel.isModifiable(row)) {
					if (column == 0) {
						receiptModel.triggerValid(row);
					} else {
						if (receiptModel.isValid(row)) {
							receiptModel.edit(row);
						}
					}
				}
			}
			
		});
		
		receiptModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (receiptModel.getCurrentEditRow() < 0) {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							scrollRectToVisible(getCellRect(receiptModel.getRowCount(), 0, false));
						}
						
					});
				}
			}
			
		});
	}

}
