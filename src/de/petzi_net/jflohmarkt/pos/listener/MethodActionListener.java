/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;

import de.petzi_net.jflohmarkt.pos.ActionButton;

/**
 * @author axel
 *
 */
public class MethodActionListener implements ActionListener {

	private final Object controller;
	private final Method method;
	
	public MethodActionListener(Object controller, String methodName) {
		this.controller = controller;
		if (controller != null && methodName != null) {
			Method m;
			try {
				m = controller.getClass().getMethod(methodName, new Class<?>[]{ActionEvent.class});
			} catch (NoSuchMethodException e1) {
				try {
					m = controller.getClass().getMethod(methodName, new Class<?>[]{AbstractButton.class});
				} catch (NoSuchMethodException e2) {
					try {
						m = controller.getClass().getMethod(methodName, new Class<?>[0]);
					} catch (NoSuchMethodException e3) {
						m = null;
					}
				}
			}
			this.method = m;
		} else {
			this.method = null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (method != null) {
			try {
				Class<?>[] paramTypes = method.getParameterTypes();
				if (paramTypes.length == 0) {
					method.invoke(controller);
				} else {
					if (paramTypes[0] == ActionButton.class) {
						method.invoke(controller, evt.getSource());
					} else {
						if (paramTypes[0] == ActionEvent.class) {
							method.invoke(controller, evt);
						}
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
