/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author axel
 *
 */
public class MethodListenerSupport {

	private final Object controller;
	private final Method method;
	
	public MethodListenerSupport(Object controller, String methodName) {
		this.controller = controller;
		if (controller != null && methodName != null) {
			Method m;
			try {
				m = controller.getClass().getMethod(methodName, new Class<?>[]{Component.class});
			} catch (NoSuchMethodException e1) {
				try {
					m = controller.getClass().getMethod(methodName, new Class<?>[0]);
				} catch (NoSuchMethodException e2) {
					m = null;
				}
			}
			this.method = m;
		} else {
			this.method = null;
		}
	}
	
	public void invoke(Component component) {
		if (method != null) {
			try {
				Class<?>[] paramTypes = method.getParameterTypes();
				if (paramTypes.length == 0) {
					method.invoke(controller);
				} else {
					if (paramTypes[0] == Component.class) {
						method.invoke(controller, component);
					}
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
