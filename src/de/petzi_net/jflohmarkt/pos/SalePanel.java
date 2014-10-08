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
import de.petzi_net.jflohmarkt.pos.valuetype.ChecksumValueType;
import de.petzi_net.jflohmarkt.pos.xml.ItemLine;
import de.petzi_net.jflohmarkt.pos.xml.PaymentLine;
import de.petzi_net.jflohmarkt.pos.xml.Receipt;
import de.petzi_net.jflohmarkt.pos.xml.Sale;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class SalePanel extends ModulePanel<Sale> {
	
	private static final int SELLER_NUMBER_LENGTH = 4;

	private final BasePanel saleCard;
	private final ControlTextField<Integer> seller;
	private final ControlTextField<BigDecimal> price;
	private final BasePanel paymentCard;
	private final ControlTextField<BigDecimal> payment;
	private final ActionButton startPayment;
	private final ActionButton cancelSale;
	private final ActionButton commitSale;
	private final Callback itemLineCallback = new ItemLineCallback();
	private final Callback paymentLineCallback = new PaymentLineCallback();
	private final Callback overpaymentCallback = new OverpaymentCallback();
	
	private ItemLine editItemLine = null;
	private PaymentLine editPaymentLine = null;
	
	public SalePanel(ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super("VK-Bon", Sale.class, receiptModel, receiptStorage);
		BasePanel cardPanel = new BasePanel(BasePanel.Layout.CARD);
		
		saleCard = new BasePanel(BasePanel.Layout.BORDER);
		BasePanel saleActions = new BasePanel(BasePanel.Layout.HORIZONTAL);
		cancelSale = new ActionButton("Abbruch", Color.RED, this, "cancel");
		saleActions.add(cancelSale);
		startPayment = new ActionButton("Zahlung", Color.CYAN, this, "switchPayment");
		saleActions.add(startPayment);
		saleCard.add(saleActions, BorderLayout.NORTH);
		BasePanel saleInput = new BasePanel(BasePanel.Layout.VERTICAL);
		saleInput.add(new ControlLabel("Verkäufer"));
		seller = new ControlTextField<Integer>(new ChecksumValueType(SELLER_NUMBER_LENGTH));
		seller.addEnterListener(new MethodEnterListener(this, "prepareSale"));
		saleInput.add(seller);
		saleInput.add(new ControlLabel("Preis"));
		price = new ControlTextField<BigDecimal>(new BigDecimalValueType(6, 2, true), this, "triggerPrice");
		price.addEnterListener(new MethodEnterListener(this, "prepareSale"));
		saleInput.add(price);
		saleInput.add(new ControlLabel("Retour"));
		ActionButton retour = new ActionButton("+/-", Color.ORANGE, this, "retour");
		retour.setAlignmentX(1.0f);
		saleInput.add(retour);
		saleCard.add(saleInput, BorderLayout.CENTER);
		cardPanel.add(saleCard, "sale");
		
		paymentCard = new BasePanel(BasePanel.Layout.BORDER);
		BasePanel paymentActions = new BasePanel(BasePanel.Layout.HORIZONTAL);
		paymentActions.add(new ActionButton("Zurück", Color.YELLOW, this, "switchSale"));
		commitSale = new ActionButton("Abschluss", Color.GREEN, this, "commit");
		paymentActions.add(commitSale);
		paymentCard.add(paymentActions, BorderLayout.NORTH);
		BasePanel paymentInput = new BasePanel(BasePanel.Layout.VERTICAL);
		paymentInput.add(new ControlLabel("Zahlbetrag"));
		payment = new ControlTextField<BigDecimal>(new BigDecimalValueType(6, 2, false), this, "triggerPayment");
		payment.addEnterListener(new MethodEnterListener(this, "preparePayment"));
		paymentInput.add(payment);
		paymentInput.add(new DummyComponent());
		paymentCard.add(paymentInput, BorderLayout.CENTER);
		cardPanel.add(paymentCard, "payment");
		
		controlPanel.add(cardPanel);
	}
	
	private void cleanup(boolean quitEdit) {
		if (quitEdit) {
			quitEdit();
		}
		seller.setValue(null);
		price.setValue(null);
		payment.setValue(null);
		editItemLine = null;
		editPaymentLine = null;
	}
	
	private static BigDecimal calcOutstanding(Sale sale) {
		BigDecimal outstanding = BigDecimal.ZERO;
		if (sale != null) {
			for (ItemLine line : sale.getItem()) {
				if (line.isValid()) {
					outstanding = outstanding.add(line.getPrice());
				}
			}
			for (PaymentLine line : sale.getPayment()) {
				if (line.isValid()) {
					outstanding = outstanding.subtract(line.getAmount());
				}
			}
		}
		return outstanding;
	}
	
	public void prepareSale() {
		if (getReceipt() == null) {
			cancelSale.setEnabled(false);
			startPayment.setEnabled(false);
		} else {
			cancelSale.setEnabled(true);
			startPayment.setEnabled(true);
		}
	}
	
	public void retour() {
		String text = price.getText();
		if (text != null) {
			if (text.length() > 0 && text.charAt(0) == '-') {
				price.setText(text.substring(1));
			} else {
				price.setText('-' + text);
			}
		}
	}
	
	public void triggerPrice() {
		Integer sellerValue = (Integer) seller.getValue();
		BigDecimal priceValue = (BigDecimal) price.getValue();
		if (sellerValue != null && priceValue != null) {
			if (editItemLine != null) {
				editItemLine.setSeller(sellerValue);
				editItemLine.setPrice(priceValue);
				editItemLine = null;
			} else {
				Sale sale = ensureReceipt();
				int idx = sale.getItem().size();
				ItemLine line = new ItemLine();
				line.setValid(true);
				line.setTimestamp(getCurrentTime());
				line.setSeller(sellerValue);
				line.setPrice(priceValue);
				sale.getItem().add(line);
				printLine(idx, itemLineCallback);
			}
			updateReceipt();
			cleanup(true);
		} else {
			if (sellerValue == null && priceValue == null && editItemLine == null && getReceipt() != null) {
				switchPayment();
			}
		}
	}
	
	public void preparePayment() {
		BigDecimal outstanding = calcOutstanding(getReceipt());
		if (outstanding.compareTo(BigDecimal.ZERO) > 0) {
			if (editPaymentLine == null) {
				payment.setValue(outstanding);
				payment.selectAll();
			}
			commitSale.setEnabled(false);
		} else {
			payment.setValue(null);
			commitSale.setEnabled(true);
		}
	}
	
	public void triggerPayment() {
		BigDecimal paymentValue = (BigDecimal) payment.getValue();
		if (paymentValue != null) {
			if (editPaymentLine != null) {
				editPaymentLine.setAmount(paymentValue);
				editPaymentLine = null;
			} else {
				Sale sale = ensureReceipt();
				int idx = sale.getPayment().size();
				PaymentLine paymentLine = new PaymentLine();
				paymentLine.setValid(true);
				paymentLine.setTimestamp(getCurrentTime());
				paymentLine.setAmount(paymentValue);
				sale.getPayment().add(paymentLine);
				printLine(idx, paymentLineCallback);
			}
			updateReceipt();
			cleanup(true);
		} else {
			if (editItemLine == null && getReceipt() != null) {
				BigDecimal outstanding = calcOutstanding(getReceipt());
				if (outstanding.compareTo(BigDecimal.ZERO) <= 0) {
					commit();
				}
			}
		}
	}
	
	public void switchPayment() {
		switchPayment(null);
	}
	
	private void switchPayment(PaymentLine paymentLine) {
		saleCard.setVisible(false);
		cleanup(paymentLine == null);
		editPaymentLine = paymentLine;
		if (editPaymentLine != null) {
			payment.setValue(editPaymentLine.getAmount());
			payment.selectAll();
		}
		paymentCard.setVisible(true);
		preparePayment();
		payment.requestFocusInWindow();
	}
	
	public void switchSale() {
		switchSale(null);
	}
	
	private void switchSale(ItemLine itemLine) {
		paymentCard.setVisible(false);
		cleanup(itemLine == null);
		editItemLine = itemLine;
		if (editItemLine != null) {
			seller.setValue(editItemLine.getSeller());
			price.setValue(editItemLine.getPrice());
			seller.selectAll();
		}
		saleCard.setVisible(true);
		prepareSale();
		seller.requestFocusInWindow();
	}
	
	public void commit() {
		if (getReceipt() != null) {
			printLine(0, overpaymentCallback);
			commitReceipt();
		}
		switchSale(null);
	}
	
	@Override
	public void cancel() {
		cancelReceipt();
		switchSale(null);
	}
	
	private class ItemLineCallback extends BaseCallback {

		@Override
		public String getCol1(Receipt receipt, int line) {
			return "Verkäufer";
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return ((Sale) receipt).getItem().get(line).getSeller();
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return ((Sale) receipt).getItem().get(line).getPrice();
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
			return ((Sale) receipt).getItem().get(line).isValid();
		}
		
		@Override
		public boolean edit(Receipt receipt, int line) {
			ItemLine itemLine = ((Sale) receipt).getItem().get(line);
			switchSale(itemLine);
			return itemLine != null;
		}
		
		@Override
		public void triggerValid(Receipt receipt, int line) {
			ItemLine itemLine = ((Sale) receipt).getItem().get(line);
			itemLine.setValid(!itemLine.isValid());
			updateReceipt();
			preparePayment();
		}
		
	}
	
	private class PaymentLineCallback extends BaseCallback {

		@Override
		public String getCol1(Receipt receipt, int line) {
			return "Gegeben";
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return null;
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return ((Sale) receipt).getPayment().get(line).getAmount();
		}

		@Override
		public boolean isNegative(Receipt receipt, int line) {
			return true;
		}

		@Override
		public boolean isMarked(Receipt receipt, int line) {
			return false;
		}

		@Override
		public boolean isValid(Receipt receipt, int line) {
			return ((Sale) receipt).getPayment().get(line).isValid();
		}
		
		@Override
		public boolean edit(Receipt receipt, int line) {
			PaymentLine paymentLine = ((Sale) receipt).getPayment().get(line);
			switchPayment(paymentLine);
			return paymentLine != null;
		}
		
		@Override
		public void triggerValid(Receipt receipt, int line) {
			PaymentLine paymentLine = ((Sale) receipt).getPayment().get(line);
			paymentLine.setValid(!paymentLine.isValid());
			updateReceipt();
			preparePayment();
		}
		
	}
	
	private class OverpaymentCallback extends BaseCallback {

		@Override
		public boolean isModifiable(Receipt receipt, int line) {
			return false;
		}
		
		@Override
		public String getCol1(Receipt receipt, int line) {
			return "Zurück";
		}

		@Override
		public Integer getCol2(Receipt receipt, int line) {
			return null;
		}

		@Override
		public BigDecimal getCol3(Receipt receipt, int line) {
			return calcOutstanding((Sale) receipt).negate();
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
