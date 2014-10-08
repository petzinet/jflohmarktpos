/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.petzi_net.jflohmarkt.pos.valuetype.BigDecimalValueType;
import de.petzi_net.jflohmarkt.pos.valuetype.IntegerValueType;
import de.petzi_net.jflohmarkt.pos.valuetype.StringValueType;
import de.petzi_net.jflohmarkt.pos.valuetype.ValueType;
import de.petzi_net.jflohmarkt.pos.xml.Receipt;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ReceiptModel extends AbstractTableModel {

	private final List<ReceiptLine> lines = new ArrayList<ReceiptLine>(128);
	
	private int currentEditRow = -1;
	
	public int getCurrentEditRow() {
		return currentEditRow;
	}
	
	@Override
	public int getRowCount() {
		return lines.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	public ValueType<?> getColumnValueType(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return null;
		case 1:
			return new StringValueType(10);
		case 2:
			return new IntegerValueType(5);
		case 3:
			return new BigDecimalValueType(7, 2, true);
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ReceiptLine line = lines.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return line.callback.isValid(line.receipt, line.line);
		case 1:
			return line.callback.getCol1(line.receipt, line.line);
		case 2:
			return line.callback.getCol2(line.receipt, line.line);
		case 3:
			return line.callback.getCol3(line.receipt, line.line);
		}
		return null;
	}
	
	public boolean isModifiable(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		return line.callback.isModifiable(line.receipt, line.line);
	}
	
	public boolean isNegative(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		return line.callback.isNegative(line.receipt, line.line);
	}
	
	public boolean isMarked(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		return line.callback.isMarked(line.receipt, line.line);
	}
	
	public boolean isValid(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		return line.callback.isValid(line.receipt, line.line);
	}
	
	public void edit(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		if (line.callback.edit(line.receipt, line.line)) {
			currentEditRow = rowIndex;
		}
		fireTableDataChanged();
	}
	
	public void quitEdit() {
		currentEditRow = -1;
		fireTableDataChanged();
	}
	
	public void triggerValid(int rowIndex) {
		ReceiptLine line = lines.get(rowIndex);
		line.callback.triggerValid(line.receipt, line.line);
		fireTableDataChanged();
	}
	
	public void addLine(Receipt receipt, int line, Callback callback) {
		while (lines.size() >= 100) {
			lines.remove(0);
		}
		lines.add(new ReceiptLine(receipt, line, callback));
		fireTableDataChanged();
	}
	
	private static class ReceiptLine {
		
		public Receipt receipt;
		public int line;
		public Callback callback;
		
		public ReceiptLine(Receipt receipt, int line, Callback callback) {
			super();
			this.receipt = receipt;
			this.line = line;
			this.callback = callback;
		}
		
	}
	
	public static interface Callback {
		
		public boolean isModifiable(Receipt receipt, int line);
		
		public String getCol1(Receipt receipt, int line);
		
		public Integer getCol2(Receipt receipt, int line);
		
		public BigDecimal getCol3(Receipt receipt, int line);
		
		public boolean isNegative(Receipt receipt, int line);
		
		public boolean isMarked(Receipt receipt, int line);
				
		public boolean isValid(Receipt receipt, int line);
		
		public boolean edit(Receipt receipt, int line);
		
		public void triggerValid(Receipt receipt, int line);
		
	}

}
