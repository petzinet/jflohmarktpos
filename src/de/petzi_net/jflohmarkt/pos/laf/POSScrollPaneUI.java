/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 * @author axel
 *
 */
public class POSScrollPaneUI extends BasicScrollPaneUI {
	
    public static ComponentUI createUI(JComponent x) {
        return new POSScrollPaneUI();
    }

}
