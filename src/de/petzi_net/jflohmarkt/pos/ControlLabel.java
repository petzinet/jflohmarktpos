/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class ControlLabel extends JLabel {
	
	public ControlLabel(String text) {
		super(text);
		setFont(new Font("Dialog", Font.ITALIC, 24));
		setForeground(Color.BLACK);
		setAlignmentX(1.0f);
	}

}
