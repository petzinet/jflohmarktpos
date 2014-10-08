/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 * @author axel
 *
 */
public class POSViewportUI extends BasicViewportUI {
	
	private final static POSViewportUI INSTANCE = new POSViewportUI();
	
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}
	
	@Override
	protected void installDefaults(JComponent c) {
		super.installDefaults(c);
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
	}

}
