/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jfedor
 */
public interface BoardObject {
    
        
    public static HashMap<type,Image> images = new HashMap<type,Image>();
    public static HashMap<type,Image[]> coloredImages = new HashMap<type,Image[]>();

    public enum type {WHEAT,ORE,WOOD,BRICK,SHEEP,DEV,SETTLEMENT,ROAD,CITY,DICE,WHEATPORT,OREPORT,WOODPORT,BRICKPORT,SHEEPPORT};
   
    public ArrayList<type> cardtypes = new ArrayList<type>(Arrays.asList(type.WHEAT,type.ORE,type.WOOD,type.BRICK,type.SHEEP));
    public ArrayList<type> porttypes = new ArrayList<type>(Arrays.asList(type.WHEATPORT,type.OREPORT,type.WOODPORT,type.BRICKPORT,type.SHEEPPORT));

    public HashMap<type,type> type2port = new HashMap<type,type>() {
      { 
	  put(type.WHEAT, type.WHEATPORT);
	  put(type.ORE, type.OREPORT);
	  put(type.WOOD, type.WOODPORT);
	  put(type.BRICK, type.BRICKPORT);
	  put(type.SHEEP, type.SHEEPPORT);
      }};

    public int getW();
    public int getH();
    
    public int getX();
    public int getY();
    public void setX(int x);
    public void setY(int y);

    
    public BoardObject.type getType();

    public void paint(Graphics g);
    public void paint(Graphics g, int dx, int dy);

	public void setLoc(int l);

	public int getLoc();

}
