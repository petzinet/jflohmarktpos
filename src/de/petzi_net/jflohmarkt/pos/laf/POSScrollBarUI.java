/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * @author axel
 *
 */
public class POSScrollBarUI extends BasicScrollBarUI {
	
    public static ComponentUI createUI(JComponent c)    {
        return new POSScrollBarUI();
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g.translate(thumbBounds.x, thumbBounds.y);

        g.setColor(thumbColor);
        g.fillRoundRect(5, 5, w - 10, h - 10, 20, 20);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
    	return new POSArrowButton(orientation);
    }
    
    @Override
    protected JButton createIncreaseButton(int orientation) {
    	return new POSArrowButton(orientation);
    }
    
    @SuppressWarnings("serial")
	protected static class POSArrowButton extends BasicArrowButton {

		public POSArrowButton(int direction) {
			super(direction);
	    	setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    	setBackground(Color.WHITE);
	    	setForeground(Color.BLACK);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(75, 60);
		}
    	
    }

}
