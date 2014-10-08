/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.Component;

/**
 * @author axel
 *
 */
public class MethodEnterListener implements EnterListener {

	private final MethodListenerSupport support;
	
	public MethodEnterListener(Object controller, String methodName) {
		this.support = new MethodListenerSupport(controller, methodName);
	}
	
	@Override
	public void enter(Component component) {
		support.invoke(component);
	}
	
}
