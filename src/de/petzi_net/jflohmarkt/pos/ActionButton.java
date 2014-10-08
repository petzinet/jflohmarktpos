/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

import de.petzi_net.jflohmarkt.pos.listener.MethodActionListener;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ActionButton extends JButton {
	
	public ActionButton(String text, Color background) {
		this(text, background, null, null);
	}
	
	public ActionButton(String text, Color background, Object controller, String methodName) {
		super(text);
		Dimension size = new Dimension(130, 80);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		setBackground(background);
		setFont(new Font("Dialog", Font.PLAIN, 24));
		if (controller != null && methodName != null) {
			addActionListener(new MethodActionListener(controller, methodName));
		}
	}

}
