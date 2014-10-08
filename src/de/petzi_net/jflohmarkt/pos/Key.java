/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.Robot;

/**
 * @author axel
 *
 */
public class Key {
	
	private final int keyCode;
	private final Key[] keys;
	
	public Key(int keyCode, Key... keys) {
		this.keyCode = keyCode;
		this.keys = keys.clone();
	}
	
	public void process(Robot robot) {
		robot.keyPress(keyCode);
		for (Key key : keys) {
			key.process(robot);
		}
		robot.keyRelease(keyCode);
	}

}
