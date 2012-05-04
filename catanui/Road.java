/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author jfedor
 */
public class Road implements BoardObject {

    private int _x;
    private int _y;
    private int _x2;
    private int _y2;
    public int _length = 70;
    private int _h = 20;

    public int[] mycoord = new int[2];
    private int _loc = 0;
    public Color c = Color.magenta;
    public BoardObject.type mytype = BoardObject.type.ROAD;

    public boolean oneDown = false;
    
    public Road(int x1, int y1) {
        _x = x1;
        _y = y1;
        _x2 = _x+_length;
        _y2 = _y;
    }
    
    @Override
    public int getW() {
        return _length;
    }

    @Override
    public int getH() {
        return _h;
    }
    
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }
    @Override
    public void setX(int x) {
        _x = x;
	setX2(_x+_length);
    }

    @Override
    public void setY(int y) {
        _y = y;
	setY2(_y);
    }

    public void setX2(int x) {
        _x2 = x;
    }


    public void setY2(int y) {
        _y2 = y;
    }

    public void setColor(int num) {
	switch(num) {
		case 0: c = Color.blue; break;
		case 1: c = Color.red; break;
		case 2: c = Color.green; break;
		case 3: c = Color.cyan; break;
		case 4: c = Color.yellow; break;
		case 5: c = Color.orange; break;
	}
    }

    public BoardObject.type getType() {return mytype;}

	public void setLoc(int l) {
		_loc = l;
	}

	public int getLoc() {
		return _loc;
	}


    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        
        g.setColor(c);
        g.setStroke(new BasicStroke(6));
        g.drawLine(_x, _y, _x2, _y2);
        g.setStroke(new BasicStroke(1));
        
    }
    
    @Override
    public void paint(Graphics g1, int dx, int dy) {
        Graphics2D g = (Graphics2D)g1;

        g.setColor(c);
        g.setStroke(new BasicStroke(6));
        g.drawLine(_x+dx, _y+dy, _x2+dx, _y2+dy);
        g.setStroke(new BasicStroke(1));
        
    }
}
