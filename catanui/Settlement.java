/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author jfedor
 */
public class Settlement implements BoardObject {

    private int _x;
    private int _y;
    private int _w = 50;
    private int _h = 50;
    
    private int _color;
    private int _loc = 0;
    public BoardObject.type mytype = BoardObject.type.SETTLEMENT;

    public Settlement(int x, int y, int color) {
        _x = x;
        _y = y;
	_color = color;
    }
    
    @Override
    public int getW() {
        return _w;
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
    }

	public void setLoc(int l) {
		_loc = l;
	}

	public int getLoc() {
		return _loc;
	}

    @Override
    public void setY(int y) {
        _y = y;
    }
    public BoardObject.type getType() {return mytype;}
    @Override
    public void paint(Graphics g) {
        
        g.drawImage(coloredImages.get(mytype)[_color], (_x)-3, (_y)-3, _w, _h,  null);
        
    }
    
    @Override
    public void paint(Graphics g, int dx, int dy) {

        g.drawImage(coloredImages.get(mytype)[_color], (_x+dx)-3, (_y+dy)-3, _w, _h,  null);
        
    }
}
