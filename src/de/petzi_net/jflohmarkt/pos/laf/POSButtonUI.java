/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author axel
 *
 */
public class POSButtonUI extends BasicButtonUI {
	
	private final static POSButtonUI INSTANCE = new POSButtonUI();
	
	public static ComponentUI createUI(JComponent c) {
		return INSTANCE;
	}
	
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		b.setFocusable(false);
	}
	
	@SuppressWarnings("serial")
	static class ButtonBorder extends LineBorder implements UIResource {
		
		private final int thicknessPressed;
		private final int thicknessSelected;
		
		public ButtonBorder(int thicknessPressed, int thicknessSelected) {
			super(Color.WHITE, 0);
			this.thicknessPressed = thicknessPressed;
			this.thicknessSelected = thicknessSelected;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			AbstractButton b = (AbstractButton) c;
			if (b.getModel().isPressed()) {
				lineColor = b.getForeground();
				thickness = thicknessPressed;
			} else {
				if (b.getModel().isSelected()) {
					lineColor = b.getForeground();
					thickness = thicknessSelected;
				} else {
					lineColor = b.getBackground();
					thickness = 0;
				}
			}
			super.paintBorder(c, g, x, y, width, height);
		}
		
	}

}
