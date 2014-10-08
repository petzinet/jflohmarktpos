/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import javax.swing.JComponent;

/**
 * @author axel
 *
 */
public class CellLayout implements LayoutManager2 {

	private final static String PROPERTY_CONSTRAINTS = CellLayout.class.getName() + ".constraints";
	
	private final int cellWidth;
	private final int cellHeight;
	private final int hgap;
	private final int vgap;
	
	public CellLayout(int cellWidth, int cellHeight) {
		this(cellWidth, cellHeight, 0, 0);
	}
	
	public CellLayout(int cellWidth, int cellHeight, int hgap, int vgap) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, null);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (comp instanceof JComponent) {
			((JComponent) comp).putClientProperty(PROPERTY_CONSTRAINTS, null);
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension dim = getSizes(parent);
		if (dim.width > 0 && dim.height > 0) {
			return new Dimension(dim.width * cellWidth + (dim.width - 1) * hgap, dim.height * cellHeight + (dim.height - 1) * vgap);
		} else {
			return new Dimension(0, 0);
		}
	}
	
	private CellLayoutConstraints getConstraints(Component comp) {
		if (comp instanceof JComponent) {
			Object propertyValue = ((JComponent) comp).getClientProperty(PROPERTY_CONSTRAINTS);
			if (propertyValue instanceof CellLayoutConstraints) {
				return (CellLayoutConstraints) propertyValue;
			}
		}
		return null;
	}
	
	private Dimension getSizes(Container parent) {
		int maxX = 0;
		int maxY = 0;
		for (Component comp : parent.getComponents()) {
			CellLayoutConstraints constraints = getConstraints(comp);
			if (constraints != null) {
				int x = constraints.getX() + constraints.getSpanX();
				if (x > maxX) {
					maxX = x;
				}
				int y = constraints.getY() + constraints.getSpanY();
				if (y > maxY) {
					maxY = y;
				}
			}
		}
		return new Dimension(maxX, maxY);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public void layoutContainer(Container parent) {
		for (Component comp : parent.getComponents()) {
			CellLayoutConstraints constraints = getConstraints(comp);
			if (constraints != null) {
				int x = constraints.getX() * (cellWidth + hgap);
				int y = constraints.getY() * (cellHeight + vgap);
				int w = constraints.getSpanX() * cellWidth + (constraints.getSpanX() - 1) * hgap;
				int h = constraints.getSpanY() * cellHeight + (constraints.getSpanY() - 1) * vgap;
				comp.setBounds(x, y, w, h);
			} else {
				comp.setBounds(0, 0, 0, 0);
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (comp instanceof JComponent && constraints instanceof CellLayoutConstraints) {
			((JComponent) comp).putClientProperty(PROPERTY_CONSTRAINTS, constraints);
		}
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return preferredLayoutSize(target);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}
