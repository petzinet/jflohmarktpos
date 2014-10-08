/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

/**
 * @author axel
 *
 */
public class POSToggleButtonUI extends BasicToggleButtonUI {
	
	private final static POSToggleButtonUI INSTANCE = new POSToggleButtonUI();
	
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}
	
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		b.setFocusable(false);
	}

}
