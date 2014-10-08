/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.event.KeyEvent;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class NumPadPanel extends BasePanel {
	
	public NumPadPanel() {
		super(new CellLayout(75, 60, 4, 4));
		add(new KeyButton("7", new Key(KeyEvent.VK_7)), new CellLayoutConstraints(0, 0));
		add(new KeyButton("8", new Key(KeyEvent.VK_8)), new CellLayoutConstraints(1, 0));
		add(new KeyButton("9", new Key(KeyEvent.VK_9)), new CellLayoutConstraints(2, 0));
		add(new KeyButton("C", new Key(KeyEvent.VK_CONTROL, new Key(KeyEvent.VK_A)), new Key(KeyEvent.VK_DELETE)), new CellLayoutConstraints(3, 0));
		add(new KeyButton("4", new Key(KeyEvent.VK_4)), new CellLayoutConstraints(0, 1));
		add(new KeyButton("5", new Key(KeyEvent.VK_5)), new CellLayoutConstraints(1, 1));
		add(new KeyButton("6", new Key(KeyEvent.VK_6)), new CellLayoutConstraints(2, 1));
		add(new KeyButton("<-", new Key(KeyEvent.VK_END), new Key(KeyEvent.VK_BACK_SPACE)), new CellLayoutConstraints(3, 1));
		add(new KeyButton("1", new Key(KeyEvent.VK_1)), new CellLayoutConstraints(0, 2));
		add(new KeyButton("2", new Key(KeyEvent.VK_2)), new CellLayoutConstraints(1, 2));
		add(new KeyButton("3", new Key(KeyEvent.VK_3)), new CellLayoutConstraints(2, 2));
		add(new KeyButton("OK", new Key(KeyEvent.VK_ENTER)), new CellLayoutConstraints(3, 2, 1, 2));
		add(new KeyButton("0", new Key(KeyEvent.VK_0)), new CellLayoutConstraints(0, 3, 2, 1));
		add(new KeyButton(",", new Key(KeyEvent.VK_COMMA)), new CellLayoutConstraints(2, 3));
	}

}
