/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * @author axel
 *
 */
public class POSPanelUI extends BasicPanelUI {
	
	private final static POSPanelUI INSTANCE = new POSPanelUI();
	
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}
	
	@Override
	protected void installDefaults(JPanel p) {
		super.installDefaults(p);
        LookAndFeel.installProperty(p, "opaque", Boolean.FALSE);
	}

}
