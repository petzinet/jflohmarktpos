/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.xml.Dropoff;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class DropoffPanel extends DenominationPanel<Dropoff> {
	
	public DropoffPanel(ReceiptModel receiptModel, ReceiptStorage receiptStorage) {
		super("Einlage", Dropoff.class, receiptModel, receiptStorage);
	}

}
