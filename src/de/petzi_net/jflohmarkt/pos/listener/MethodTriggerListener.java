/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.Component;

/**
 * @author axel
 *
 */
public class MethodTriggerListener implements TriggerListener {

	private final MethodListenerSupport support;
	
	public MethodTriggerListener(Object controller, String methodName) {
		this.support = new MethodListenerSupport(controller, methodName);
	}
	
	@Override
	public void trigger(Component component) {
		support.invoke(component);
	}
	
}
