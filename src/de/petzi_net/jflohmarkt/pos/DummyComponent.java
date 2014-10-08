/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class DummyComponent extends JComponent {
	
	public DummyComponent() {
		setFocusable(true);
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				transferFocus();
			}
			
		});
	}

}
