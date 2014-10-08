/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.laf;

import java.awt.Color;

import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class POSLookAndFeel extends MetalLookAndFeel {

	@Override
	public String getName() {
		return "POSLookAndFeel";
	}

	@Override
	public String getID() {
		return "POSLookAndFeel";
	}

	@Override
	public String getDescription() {
		return "POSLookAndFeel";
	}
	
	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		table.put("ButtonUI", POSButtonUI.class.getName());
		table.put("ToggleButtonUI", POSToggleButtonUI.class.getName());
		table.put("LabelUI", POSLabelUI.class.getName());
		table.put("PanelUI", POSPanelUI.class.getName());
		table.put("ScrollBarUI", POSScrollBarUI.class.getName());
		table.put("ScrollPaneUI", POSScrollPaneUI.class.getName());
		table.put("ViewportUI", POSViewportUI.class.getName());
	}
	
	@Override
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		
		Border noBorder = new BorderUIResource.EmptyBorderUIResource(0, 0, 0, 0);
		Border buttonBorder = new POSButtonUI.ButtonBorder(2, 6);
		Color white = new ColorUIResource(Color.WHITE);
		Color black = new ColorUIResource(Color.BLACK);
		Color gray = new ColorUIResource(Color.LIGHT_GRAY);
		
		table.put("Button.border", buttonBorder);
		table.put("Button.background", white);
		table.put("Button.foreground", black);
		table.put("ToggleButton.border", buttonBorder);
		table.put("Viewport.background", white);
		table.put("ScrollBar.border", noBorder);
		table.put("ScrollBar.background", white);
		table.put("ScrollBar.thumb", gray);
		table.put("ScrollBar.thumbHighlight", white);
		table.put("ScrollBar.thumbShadow", white);
		table.put("ScrollBar.thumbDarkShadow", white);
		table.put("ScrollBar.track", white);
		table.put("ScrollBar.trackHighlight", white);
		table.put("ScrollBar.width", 75);
		table.put("ScrollPane.border", noBorder);
		table.put("ScrollPane.background", white);
		table.put("ScrollPane.foreground", black);
		table.put("ScrollPane.viewportBorder", noBorder);
		table.put("Table.scrollPaneBorder", noBorder);
	}

}
