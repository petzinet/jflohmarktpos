/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.xml.Inventory;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class InventoryPanel extends DenominationPanel<Inventory> {
	
	public InventoryPanel(ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super("Bestand", Inventory.class, receiptModel, receiptStorage);
	}

}
