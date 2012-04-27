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
    private int _w = 20;
    private int _h = 20;
    
    public Color c = Color.yellow;
    public BoardObject.type mytype = BoardObject.type.SETTLEMENT;

    public Settlement(int x, int y) {
        _x = x;
        _y = y;
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

    @Override
    public void setY(int y) {
        _y = y;
    }
    public BoardObject.type getType() {return mytype;}
    @Override
    public void paint(Graphics g) {
        
        g.setColor(c);
        g.fillRect(_x, _y, _w, _h);
        
    }
    
    @Override
    public void paint(Graphics g, int dx, int dy) {
        g.setColor(c);
        g.fillRect(_x+dx, _y+dy, _w, _h);
        
    }
}
