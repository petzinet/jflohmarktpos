/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.xml.Pickup;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class PickupPanel extends DenominationPanel<Pickup> {
	
	public PickupPanel(ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super("Leerung", Pickup.class, receiptModel, receiptStorage);
	}

}
