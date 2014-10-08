/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.Component;

/**
 * @author axel
 *
 */
public class MethodExitListener implements ExitListener {

	private final MethodListenerSupport support;
	
	public MethodExitListener(Object controller, String methodName) {
		this.support = new MethodListenerSupport(controller, methodName);
	}
	
	@Override
	public void exit(Component component) {
		support.invoke(component);
	}
	
}
