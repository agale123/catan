package catanui;
import java.awt.event.*;
import javax.swing.*;

public class MyKeyListener implements KeyListener{
	private String _dest;
	public MyKeyListener(String t) {
		_dest = t;
	}
	public void keyReleased(KeyEvent k) {
		_dest = ((JTextField) k.getSource()).getText();
		System.out.println(_dest);
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
}