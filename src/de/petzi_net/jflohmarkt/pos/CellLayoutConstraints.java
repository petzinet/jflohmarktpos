/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

/**
 * @author axel
 *
 */
public class CellLayoutConstraints {
	
	private final int x;
	private final int y;
	private final int spanX;
	private final int spanY;
	
	public CellLayoutConstraints(int x, int y) {
		this(x, y, 1, 1);
	}
	
	public CellLayoutConstraints(int x, int y, int spanX, int spanY) {
		this.x = x;
		this.y = y;
		this.spanX = spanX;
		this.spanY = spanY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpanX() {
		return spanX;
	}

	public int getSpanY() {
		return spanY;
	}
	
}
