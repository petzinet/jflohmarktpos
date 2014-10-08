/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFormattedTextField;

import de.petzi_net.jflohmarkt.pos.ExtendedKeyboardFocusManager;

/**
 * @author axel
 *
 */
public class FocusStateAdapter implements FocusListener {

	private final Set<EnterListener> enterListeners = new HashSet<EnterListener>();
	private final Set<ExitListener> exitListeners = new HashSet<ExitListener>();
	private final Set<TriggerListener> triggerListeners = new HashSet<TriggerListener>();
	
	public void addEnterListener(EnterListener enterListener) {
		enterListeners.add(enterListener);
	}
	
	public void removeEnterListener(EnterListener enterListener) {
		enterListeners.remove(enterListener);
	}
	
	public void addExitListener(ExitListener exitListener) {
		exitListeners.add(exitListener);
	}
	
	public void removeExitListener(ExitListener exitListener) {
		exitListeners.remove(exitListener);
	}
	
	public void addTriggerListener(TriggerListener triggerListener) {
		triggerListeners.add(triggerListener);
	}
	
	public void removeTriggerListener(TriggerListener triggerListener) {
		triggerListeners.remove(triggerListener);
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		if (!e.isTemporary()) {
			try {
				if (e.getComponent() instanceof JFormattedTextField) {
					((JFormattedTextField) e.getComponent()).commitEdit();
				}
				for (ExitListener exitListener : exitListeners) {
					exitListener.exit(e.getComponent());
				}
				if (((ExtendedKeyboardFocusManager) KeyboardFocusManager.getCurrentKeyboardFocusManager()).getLastKeyCode() == KeyEvent.VK_ENTER) {
					for (TriggerListener triggerListener : triggerListeners) {
						triggerListener.trigger(e.getComponent());
					}
				}
			} catch (ParseException ex) {
				ex.printStackTrace();
				e.getComponent().requestFocusInWindow();
			}
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		if (!e.isTemporary()) {
			for (EnterListener enterListener : enterListeners) {
				enterListener.enter(e.getComponent());
			}
		}
	}

}
