/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.xml.datatype.XMLGregorianCalendar;

import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.table.ReceiptModel.Callback;
import de.petzi_net.jflohmarkt.pos.xml.Receipt;
import de.petzi_net.jflohmarkt.pos.xml.State;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public abstract class ModulePanel<T extends Receipt> extends BasePanel {
	
	protected final static Callback SEPARATOR_CALLBACK = new ConstantValuesCallback("----------", null, null, false, false, false);
	protected final static Callback CANCEL_CALLBACK = new ConstantValuesCallback("ABBRUCH", null, null, false, true, true);
	
	protected final BasePanel controlPanel;
	
	private final Class<T> receiptType;
	private final ReceiptModel receiptModel;
	private final ReceiptStorage receiptStorage;
	private final Callback headerCallback;
	
	private T receipt = null;
	
	public ModulePanel(String title, Class<T> receiptType, ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super(new BorderLayout(0, 0));
		this.receiptType = receiptType;
		this.receiptModel = receiptModel;
		this.receiptStorage = receiptStorage;
		this.headerCallback = new HeaderCallback(title);
		controlPanel = new BasePanel(new FlowLayout());
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		add(controlPanel, BorderLayout.CENTER);
		add(new NumPadPanel(), BorderLayout.SOUTH);
	}
	
	protected XMLGregorianCalendar getCurrentTime() {
		return receiptStorage.getCurrentTime();
	}
	
	public abstract void cancel();
	
	protected T getReceipt() {
		return receipt;
	}
	
	protected T ensureReceipt() {
		if (receipt == null) {
			receipt = receiptStorage.createReceipt(receiptType);
			printLine(0, headerCallback);
		}
		return receipt;
	}
	
	protected void updateReceipt() {
		if (receipt != null) {
			receiptStorage.storeReceipt(receipt);
		}
	}
	
	protected void cancelReceipt() {
		if (receipt != null) {
			receipt.setState(State.ABORTED);
			receiptStorage.storeReceipt(receipt);
			printLine(0, CANCEL_CALLBACK);
			printLine(0, SEPARATOR_CALLBACK);
		}
		receipt = null;
	}
	
	protected void commitReceipt() {
		if (receipt != null) {
			receipt.setState(State.FINISHED);
			receiptStorage.storeReceipt(receipt);
			printLine(0, SEPARATOR_CALLBACK);
		}
		receipt = null;
	}
	
	protected void printLine(int line, Callback callback) {
		if (receipt == null) {
			throw new IllegalStateException("no current receipt");
		}
		receiptModel.addLine(receipt, line, callback);
	}
	
	protected void quitEdit() {
		receiptModel.quitEdit();
	}
	
	protected abstract class BaseCallback implements Callback {

		@Override
		public boolean isModifiable(Receipt receipt, int line) {
			return ModulePanel.this.receipt == receipt;
		}

		@Override
		public boolean edit(Receipt receipt, int line) {
			return false;
		}

		@Override
		public void triggerValid(Receipt receipt, int line) {
		}
		
	}
	
	protected static class ConstantValuesCallback implements Callback {

		private final String col1;
		private final Integer col2;
		private final BigDecimal col3;
		private final boolean negative;
		private final boolean marked;
		private final boolean valid;
		
		public ConstantValuesCallback(String col1, Integer col2, BigDecimal col3, boolean negative, boolean marked, boolean valid) {
			this.col1 = col1;
			this.col2 = col2;
			this.col3 = col3;
			this.negative = negative;
			this.marked = marked;
			this.valid = valid;
		}

		@Override
		public boolean isModifiable(Receipt receipt, int line) {
			return false;
		}
		
		@Override
		public String getCol1(Receipt receipt, int line) {
			return col1;
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return col2;
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return col3;
		}

		@Override
		public boolean isNegative(Receipt receipt, int line) {
			return negative;
		}

		@Override
		public boolean isMarked(Receipt receipt, int line) {
			return marked;
		}

		@Override
		public boolean isValid(Receipt receipt, int line) {
			return valid;
		}

		@Override
		public boolean edit(Receipt receipt, int line) {
			return false;
		}

		@Override
		public void triggerValid(Receipt receipt, int line) {
		}
		
	}
	
	private class HeaderCallback extends BaseCallback {

		private final String title;
		
		public HeaderCallback(String title) {
			this.title = title;
		}
		
		@Override
		public boolean isModifiable(Receipt receipt, int line) {
			return false;
		}
		
		@Override
		public String getCol1(Receipt receipt, int line) {
			return title;
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return receipt.getReceipt();
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return null;
		}

		@Override
		public boolean isNegative(Receipt receipt, int line) {
			return false;
		}

		@Override
		public boolean isMarked(Receipt receipt, int line) {
			return true;
		}

		@Override
		public boolean isValid(Receipt receipt, int line) {
			return true;
		}
		
	}

}
