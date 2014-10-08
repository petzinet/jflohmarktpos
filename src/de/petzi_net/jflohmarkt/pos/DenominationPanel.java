/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;

import de.petzi_net.jflohmarkt.pos.listener.MethodEnterListener;
import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.table.ReceiptModel.Callback;
import de.petzi_net.jflohmarkt.pos.valuetype.BigDecimalValueType;
import de.petzi_net.jflohmarkt.pos.valuetype.IntegerValueType;
import de.petzi_net.jflohmarkt.pos.xml.DenominationLine;
import de.petzi_net.jflohmarkt.pos.xml.DenominationReceipt;
import de.petzi_net.jflohmarkt.pos.xml.Receipt;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class DenominationPanel<T extends DenominationReceipt> extends ModulePanel<T> {
	
	private final ControlTextField<Integer> quantity;
	private final ControlTextField<BigDecimal> value;
	private final ActionButton commit;
	private final ActionButton cancel;
	private final Callback lineCallback = new LineCallback();
	private final Callback sumCallback = new SumCallback();
	
	private DenominationLine editLine = null;
	
	public DenominationPanel(String title, Class<T> receiptType, ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super(title, receiptType, receiptModel, receiptStorage);
		
		BasePanel mainPanel = new BasePanel(BasePanel.Layout.BORDER);
		BasePanel actions = new BasePanel(BasePanel.Layout.HORIZONTAL);
		cancel = new ActionButton("Abbruch", Color.RED, this, "cancel");
		actions.add(cancel);
		commit = new ActionButton("Abschluss", Color.GREEN, this, "commit");
		actions.add(commit);
		mainPanel.add(actions, BorderLayout.NORTH);
		BasePanel input = new BasePanel(BasePanel.Layout.VERTICAL);
		input.add(new ControlLabel("Anzahl"));
		quantity = new ControlTextField<Integer>(new IntegerValueType(4));
		quantity.addEnterListener(new MethodEnterListener(this, "prepare"));
		input.add(quantity);
		input.add(new ControlLabel("Wert"));
		value = new ControlTextField<BigDecimal>(new BigDecimalValueType(6, 2, false), this, "triggerValue");
		value.addEnterListener(new MethodEnterListener(this, "prepare"));
		input.add(value);
		mainPanel.add(input, BorderLayout.CENTER);
		controlPanel.add(mainPanel);
	}
	
	private void cleanup(boolean quitEdit) {
		if (quitEdit) {
			quitEdit();
		}
		quantity.setValue(null);
		value.setValue(null);
		editLine = null;
	}
	
	public void prepare() {
		if (getReceipt() == null) {
			cancel.setEnabled(false);
			commit.setEnabled(false);
		} else {
			cancel.setEnabled(true);
			commit.setEnabled(true);
		}
	}
	
	public void triggerValue() {
		Integer quantityValue = (Integer) quantity.getValue();
		BigDecimal valueValue = (BigDecimal) value.getValue();
		if (quantityValue != null && valueValue != null) {
			if (editLine != null) {
				editLine.setQuantity(quantityValue);
				editLine.setValue(valueValue);
				editLine = null;
			} else {
				T receipt = ensureReceipt();
				int idx = receipt.getDenomination().size();
				DenominationLine line = new DenominationLine();
				line.setValid(true);
				line.setTimestamp(getCurrentTime());
				line.setQuantity(quantityValue);
				line.setValue(valueValue);
				receipt.getDenomination().add(line);
				printLine(idx, lineCallback);
			}
			updateReceipt();
			cleanup(true);
		} else {
			if (quantityValue == null && valueValue == null && editLine == null && getReceipt() != null) {
				commit();
			}
		}
	}
	
	public void commit() {
		if (getReceipt() != null) {
			printLine(0, sumCallback);
			commitReceipt();
		}
		cleanup(true);
		editLine = null;
		prepare();
		quantity.requestFocusInWindow();
	}
	
	@Override
	public void cancel() {
		cancelReceipt();
		cleanup(true);
		editLine = null;
		prepare();
		quantity.requestFocusInWindow();
	}
	
	private class LineCallback extends BaseCallback {

		@Override
		public String getCol1(Receipt receipt, int line) {
			return "Anzahl";
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return ((DenominationReceipt) receipt).getDenomination().get(line).getQuantity();
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return ((DenominationReceipt) receipt).getDenomination().get(line).getValue();
		}

		@Override
		public boolean isNegative(Receipt receipt, int line) {
			return false;
		}

		@Override
		public boolean isMarked(Receipt receipt, int line) {
			return false;
		}

		@Override
		public boolean isValid(Receipt receipt, int line) {
			return ((DenominationReceipt) receipt).getDenomination().get(line).isValid();
		}
		
		@Override
		public boolean edit(Receipt receipt, int line) {
			DenominationLine denominationLine = ((DenominationReceipt) receipt).getDenomination().get(line);
			cleanup(denominationLine == null);
			editLine = denominationLine;
			if (editLine != null) {
				quantity.setValue(editLine.getQuantity());
				value.setValue(editLine.getValue());
				quantity.selectAll();
			}
			prepare();
			return denominationLine != null;
		}
		
		@Override
		public void triggerValid(Receipt receipt, int line) {
			DenominationLine denominationLine = ((DenominationReceipt) receipt).getDenomination().get(line);
			denominationLine.setValid(!denominationLine.isValid());
			updateReceipt();
			prepare();
		}
		
	}
	
	private class SumCallback extends BaseCallback {

		@Override
		public boolean isModifiable(Receipt receipt, int line) {
			return false;
		}
		
		@Override
		public String getCol1(Receipt receipt, int line) {
			return "Summe";
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return null;
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			BigDecimal sum = BigDecimal.ZERO;
			DenominationReceipt dr = (DenominationReceipt) receipt;
			for (DenominationLine dl : dr.getDenomination()) {
				if (dl.isValid()) {
					sum = sum.add(dl.getValue().multiply(BigDecimal.valueOf(dl.getQuantity())));
				}
			}
			return sum;
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
