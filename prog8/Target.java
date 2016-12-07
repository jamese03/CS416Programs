/**
 *
 * Created by JB
 * Summer 2011
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Target extends Ellipse2D.Float
{
   public static int maxHits;
   private int hits;
   private Color curColor;
   public Target( int x, int y, int w, int h, int hits ){
      super( x, y, w, h );
      this.hits = hits;
      calculateColor();
   }
   
   public boolean hit(){
      if ( hits > 0 ){
         hits--;
         calculateColor();
         return true;
      }
      return false;
   }
   
   public int getHits(){
      return hits;
   }
   
   private void calculateColor(){
      int temp = (int)(180 * ((float)hits)/maxHits) + 75;
      curColor = new Color( 0, temp, temp );
   }
   
   public void draw( Graphics g ){
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(curColor);
      g2.draw(this);
      g2.fill(this);
   }
}
