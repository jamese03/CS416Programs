/* Slingshot 
 * James English jpj68
 * 10/30
 * 
 * */


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.*;
import java.awt.Dimension;


public class Shooter extends JLine
{
//-------------instance variables---------------------------------------
private int width;
private int x, y, _x1, _y1;

//--------------constructor---------------------------------------------
  public Shooter( int x, int y, int x1, int y1, int thickness )
  {
    super();
    setPoints( x, y, x1, y1);
    this.x = x;
    this.y = y;
    _x1 = x1; 
    _y1 = y1;

    setThickness(thickness);
    width = x1 - x;
  }
//---------------- setts the angle for the shot------------------------------
 
  /*Jake from the pac helped me set up the math for this method
   * */
  
  public void setShotAngle(double degrees)    
  {
    double rad = Math.toRadians(degrees);
    double dx = width*Math.cos(rad);
    double dy = -width*Math.sin(rad);
    setPoints((int)x, (int)y, (int)(x + dx), (int)(y + dy));
    _x1 = x + (int)dx;
    _y1 = y + (int)dy;
    
  }


//---------------------return x2-----------------------------
  public int getX2()
  {
    return _x1;
  
  }
  
  public int getY2()
  {
   return  _y1;
  }
  
//---------------main----------------------------------------------------
  public static void main( String args[ ] )
   {
    
      
   }

}