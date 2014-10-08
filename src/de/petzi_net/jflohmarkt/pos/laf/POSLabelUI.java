/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * @author axel
 *
 */
public class POSLabelUI extends BasicLabelUI {
	
	private final static POSLabelUI INSTANCE = new POSLabelUI();
	
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}

}
