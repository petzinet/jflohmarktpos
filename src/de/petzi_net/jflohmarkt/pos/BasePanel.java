/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class BasePanel extends JPanel {
	
	public enum Layout {
		HORIZONTAL,
		VERTICAL,
		BORDER,
		CARD
	}
	
	public BasePanel(LayoutManager layoutManager) {
		super(layoutManager);
		setOpaque(false);
	}
	
	public BasePanel(Layout layout) {
		super();
		setOpaque(false);
		switch (layout) {
		case HORIZONTAL:
			setLayout(new FlowLayout());
			break;
		case VERTICAL:
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			break;
		case BORDER:
			setLayout(new BorderLayout());
			break;
		case CARD:
			setLayout(new CardLayout());
			break;
		}
	}

}
