/** 
 * FractalEllipse.java:
 * A shape that draws a self-similar pattern based on certain parameters
 * We begin with a circle and then add smaller circles on its circumference.
 *
 * @author  of the skeleton: rdb
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class FractalEllipse extends Ellipse2D.Float 
{
   //---------------- class variables ------------------------------
   static  int     maxDepth    = 1;
   static  double  offset      = 0;
   static  double  sizeRatio   = 0.5;
   static  double  hwRatio     = 1;
   static  int     width       = 200;
   static  int     numChildren = 4;
   private int _d = 0;
   static  boolean fill        = true;
   private FractalEllipse [] children;
  
   //---------------- instance variables ------------------------------
   
   
   //------------------------ constructors ----------------------------
   /**
    *   constructor 
    */
   public FractalEllipse( int depth, Point p, int w, int h )
   {
      setFrame( p.x, p.y, w, h );

      //***************************************************************
      //  Need to create the children unless depth has reached the 
      //    max specified by the maxDepth static variable
      //***************************************************************
      
      //////////////////////////////////////////////////////////////////
      // Need to determine the size and location of all the children
      //    of this object and then invoke their constructors.
      //
      // All calculation needs to be done in double; conversion to the
      //    int (using Math.round() -- should only be performed at the
      //    very end.
      //////////////////////////////////////////////////////////////////
      
      _d = depth;
      double arc = (2*Math.PI)/numChildren;
      double _cir = ( 2 * Math.PI) / numChildren;
      
      double angle = 0;
      int w1 = (int)Math.round((double)getWidth()*sizeRatio);  // gets width 
      int h1 = (int)Math.round((double)getHeight()*sizeRatio); // gets height
      
      if(depth >= maxDepth) // do you can't go farther then max Depth
        return;
     
      children = new FractalEllipse[numChildren];
      
      for( int i =0; i < numChildren; i++)
      {
      
        Point centerP = getCenter(angle);
        children[i] = new FractalEllipse(depth + 1, new Point(centerP.x - w1/2, centerP.y - h1/2), w1, h1);
        angle += _cir;
        
      }
      

      
      
   }
   //--------------------- draw( Graphics2D ) -----------------------
   /**
    * method called by FractalGUI.paintComponent
    */   
   public void draw( Graphics2D context ) 
   {
      Color saveColor = context.getColor();
      Color myColor = Color.MAGENTA;
      Color cArray [] = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.YELLOW};
      
 
      
      if( _d > 1)
        myColor = cArray[_d-2];
      
      
      context.setColor( myColor );
      // draw myself
      if ( fill )        
         context.fill( this );
      else
         context.draw( this );
      
      
      
      
      
      ////////////////////////////////////////////////////////////////
      // Need to recurse to children draw methods -- only if the children
      //   exist of course.
      // Need to use a different color for each level of recursion
      /////////////////////////////////////////////////////////////////
      
      if ( children == null) 
        return;
      
      for( int j = 0; j < numChildren; j++)
      {
        children[j].draw(context);
      
      }
      context.setColor(saveColor);
      
   
   }
   
//-----------------------get Center method of children ------------------------
   public Point getCenter(double angle)
   {
     /*Stephen from the PAC helped me with the details behind this math, I was struggling to get the center
      * and he helped me fine tune the math.
      * */   
     
    double _x = 0; 
    double _y =0;
    angle += offset*Math.PI/numChildren;
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    _x = getWidth()*( 1 + offset)*cos/2 + ((getWidth()/2) + getBounds().getX());
    _y = getHeight()* -( 1 + offset)*sin/2 + ((getHeight()/2) + getBounds().getY());
    return new Point( (int)Math.round(_x), (int)Math.round(_y));
    
   }
   
   
   
   //==================== main ========================================
   /**
    * A convenience main to invoke app
    */
   public static void main( String[] args )
   {
      FractalApp.main( args );
   }
}
