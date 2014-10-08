/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class KeyButton extends JButton {
	
	public KeyButton(String text, final Key... keys) {
		super(text);
		Dimension size = new Dimension(80, 60);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		setFont(new Font("Dialog", Font.BOLD, 32));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Robot robot = new Robot();
					for (Key key : keys) {
						key.process(robot);
					}
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
			
		});
	}

}
