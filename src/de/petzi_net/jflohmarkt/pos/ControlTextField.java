/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;

import de.petzi_net.jflohmarkt.pos.listener.EnterListener;
import de.petzi_net.jflohmarkt.pos.listener.ExitListener;
import de.petzi_net.jflohmarkt.pos.listener.FocusStateAdapter;
import de.petzi_net.jflohmarkt.pos.listener.MethodTriggerListener;
import de.petzi_net.jflohmarkt.pos.listener.TriggerListener;
import de.petzi_net.jflohmarkt.pos.valuetype.ValueType;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ControlTextField<T> extends JFormattedTextField {
	
	private final FocusStateAdapter focusStateAdapter = new FocusStateAdapter() {

		@Override
		public void focusLost(FocusEvent e) {
			setBackground(Color.WHITE);
			super.focusLost(e);
		}

		@Override
		public void focusGained(FocusEvent e) {
			setBackground(Color.YELLOW);
			super.focusGained(e);
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					selectAll();
				}
				
			});
		}
		
	};
	
	public ControlTextField(ValueType<T> valueType) {
		this(valueType, null, null);
	}
	
	public ControlTextField(ValueType<T> valueType, Object controller, String methodName) {
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setFont(new Font("Monospaced", Font.BOLD, 64));
		FontMetrics fm = getFontMetrics(getFont());
		Dimension size = new Dimension(valueType.getMaxLength() * fm.charWidth('W') + 2, fm.getHeight() + 2);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		setAlignmentX(1.0f);
		setHorizontalAlignment(valueType.getHorizontalAlignment());
		setFormatterFactory(valueType);
		setInputVerifier(valueType.getInputVerifier());
		addFocusListener(focusStateAdapter);
		if (controller != null && methodName != null) {
			addTriggerListener(new MethodTriggerListener(controller, methodName));
		}
	}
	
	public void addEnterListener(EnterListener enterListener) {
		focusStateAdapter.addEnterListener(enterListener);
	}
	
	public void removeEnterListener(EnterListener enterListener) {
		focusStateAdapter.removeEnterListener(enterListener);
	}
	
	public void addExitListener(ExitListener exitListener) {
		focusStateAdapter.addExitListener(exitListener);
	}
	
	public void removeExitListener(ExitListener exitListener) {
		focusStateAdapter.removeExitListener(exitListener);
	}
	
	public void addTriggerListener(TriggerListener triggerListener) {
		focusStateAdapter.addTriggerListener(triggerListener);
	}
	
	public void removeTriggerListener(TriggerListener triggerListener) {
		focusStateAdapter.removeTriggerListener(triggerListener);
	}

}
