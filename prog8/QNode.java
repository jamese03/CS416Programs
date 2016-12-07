/**
 * QNode - a node in a quadtree
 * 
 * Edited by JB
 * Summer 2011
 * 
 * Edited by KJ
 * Spring 2012
 */
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class QNode 
{
   // --------------- instance variables --------------------------
   private static final Color[] colors = { Color.WHITE, Color.GREEN,
      Color.BLUE, Color.LIGHT_GRAY, Color.CYAN, Color.MAGENTA,
      Color.ORANGE, Color.PINK, Color.YELLOW};
   
   private     Rectangle      bounds = null;
   private     Rectangle      container; 
   public      ArrayList<Bullet> bullets;
   public      Color           color;
   private ArrayList<Target> targets;
   private Game game;
   static private final int    usedColors = colors.length;
   static private       int    nextColor = 0;
   QNode[] kids;
   private int depth;
   // ---------------- constructors --------------------------------
   private static int counter = 0;
   public QNode( Rectangle rect, Game g ) 
   {
      // initialize class variables
      this.bounds = rect;
      color = colors[ nextColor % usedColors ];
      targets = new ArrayList<Target>();
      kids = new QNode[4];
      bullets = new ArrayList<Bullet>();
      
   }
   //---------------- accessors -----------------------------------
   public double getX() 
   {
      return bounds.getX();
   }
   public double getY() 
   {
      return bounds.getY();
   }
   public double getWidth() 
   {
      return bounds.getWidth();
   }
   public double getHeight() 
   {
      return bounds.getHeight();
   }
   // -------------- toString() -----------------------------------
   /*
    * Provide information about what objects the node contains.
    */
   public String toString() 
   {
      String ret = "";
      return ret;
   }
   //----------------- contains( Bullet ) -----------------------
   public static int edgeCount = 0;
   public boolean contains( Bullet bullet ) 
   {
      // check if the container contains bullet. 
      // remember to check if the bullet is on edge.
       
      return bounds.contains(bullet.getRect());
      
   }
   //------------------- getRectangle() -----------------------------------
   public Rectangle getRectangle()
   {
      return bounds;
   }
   
   // ------------------ calcCollisions() ------------------------------   
   public void calcCollisions(){
      // check the bullet list and target lists for collisions. 
      Iterator<Bullet> iter = bullets.iterator();
      while ( iter.hasNext() ){
         Bullet p = iter.next();
         Iterator<Target> iterT = targets.iterator();
         while ( iterT.hasNext() ){
            Target t = iterT.next();
            if ( t.contains( p.x, p.y ) ){
              // ...
            }
         }
      }
   }

   // -------------- resetColors() -----------------------------------
   public static void resetColors(){
       nextColor = 0;
   }
   
   // -------------- incrementColor() -----------------------------------
   public static void incrementColor(){
       nextColor++;
   }
   
   // -------------- decrementColor() -----------------------------------
   public static void decrementColor(){
       nextColor--;
   }   
   
   //------get depth method------------------------------------------------------
   public int getDepth()
   {
   return depth;
   }
   public Color getColor()
   {
   return colors[depth];
   }
   //----------sets the color for the depth given------------------------------
   public Color setBrushColor(int d)
   {
   return colors[d];
   }
//----------------------set depth method----------------------------------------------   
     public void setDepth(int d)
   {
     depth = d;
   }
   
}
