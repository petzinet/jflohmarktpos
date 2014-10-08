/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * @author axel
 *
 */
public class ExtendedKeyboardFocusManager extends DefaultKeyboardFocusManager {
	
	private int lastKeyCode = KeyEvent.VK_UNDEFINED;
	
	public int getLastKeyCode() {
		return lastKeyCode;
	}
	
	public void clearLastKeyCode() {
		lastKeyCode = KeyEvent.VK_UNDEFINED;
	}
	
	@Override
	public void processKeyEvent(Component focusedComponent, KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			lastKeyCode = e.getKeyCode();
		}
		super.processKeyEvent(focusedComponent, e);
	}
	
	@Override
	public boolean dispatchEvent(AWTEvent e) {
		try {
			return super.dispatchEvent(e);
		} finally {
			if (e instanceof FocusEvent) {
				clearLastKeyCode();
			}
		}
	}

}
