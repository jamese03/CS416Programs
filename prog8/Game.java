/**
 * Game.java
 * 
 * Michael DuBreuil
 * Summer 2010
 * 
 * Modified by rdb Spring 2011
 * 
 * Edited by JB
 * Summer 2011
 */
import java.io.FileNotFoundException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel
{
   //----------------- class variables ------------------------------------
   //---- package variables defining metadata information about files
   //     and initial parameter settings; set by FlowQuad
   static int     width = 512;     // size of vector field in x direction
   static int     height = 512;    // size of vector field in y direction
   static int     frameDelay = 60;   // millisecs between frames
   static int     bulletFrameFire = 10; //the modulus for the the 
                                        //frame number for the bullet to fire
   static float bdx = 1; //bullet speed in the x direction
   static float bdy = 1; //for the y direction
   static float bulletAngle = 0;
   static int maxBullets = 50;
   static int numBullets = 0;
  
   
   public static float bulletSpeed = 10;

   
   //----------------- instance variables ---------------------------------
   
   private int  maxDepth = 4;
   private int  minSize  = 4;   // don't subdivide anything smaller than 4x4
   private int  minTargets = 5;
   
   public boolean showTree = true; // show the quadtree nodes
   
   public  QNode _root;                // root node of the quad tree
   public  LinkedList<Bullet> deadBullets;
   public  ArrayList<Bullet> interNodeBullets;
   
   private ArrayList<Target> targets;
   private ArrayList<QNode> _nodes;
   private Timer frameTimer;
   private int curFrame = 0;
   private boolean rebuildTree;
   private Scanner scanner;
   private int count = 0;
   private Timer _timer;  
 
   //--------------------------- constructor --------------------------->
    public Game( String targetsFile, int maxHits, int maxBullets ) 
   {
      super();
      this.setSize( new Dimension( width , height ));
      this.setBackground(Color.black);
      
      this.maxBullets = maxBullets;
      Target.maxHits = maxHits;
      setAngle( 0 );
      
      interNodeBullets = new ArrayList<Bullet>();
      deadBullets = new LinkedList<Bullet>();
      
      // You'll want to add code to generate the tree,
      // create graphical objects, and start animation.
       targets = new ArrayList<Target>();
       createTargets(targetsFile); // creating targets 
       buildTree(); // builds the tree

       _timer = new Timer(100, new ActionListener()
       {
         public void actionPerformed(ActionEvent ae) // timer for new frame to be called 
         {
           newFrame();
         }
       }
       );
       _timer.start();
    }

   private void createTargets(String targetsFile) 
   {
      // Each target is one line of the file.
      // The lines have the form:
      // x y width height hits

      // hits can be overridden by maxHits
       File file = new File(targetsFile);
     try
     {
       scanner = new Scanner(file);
     }
     catch( FileNotFoundException e)
     {
       System.err.println("ERROR: CANT READ THE FILE");
       return;
     }
     
     while(scanner.hasNextInt()) // to setup the targets.
     {
       int x = scanner.nextInt();
       int y = scanner.nextInt();
       int width = scanner.nextInt();
       int height = scanner.nextInt();
       int hits = scanner.nextInt();
       Target targ = new Target(x,y,width,height,hits);
       targets.add(targ);                      
     }  
   }
   
   //---------------------- buildTree() ----------------------------------
   /**
    * build the quad tree
    */
   public void buildTree() // helper function for buildtree recurison. 
   {
     Rectangle r = new Rectangle(0,0);
     r.setSize(width, height);
     _root = new QNode( r, this);
     buildTree(1, _root);
   }
   //------------------buidTree with depth and node parameters-----------------
     public void buildTree(int depth, QNode node) // recursive method of buildtree 
   { 
       int counter = 0;

       node.setDepth(depth); 
       
       for( int i = 0; i < targets.size(); i++)
       {
       if(targets.get(i).intersects(node.getRectangle()))
         
         counter++;
        // counter so min targets will work 
       }
       
       if( counter < minTargets) // so min targets is implemented correctly
         return;
       
     if(depth > maxDepth) // when recursion is done. 
       return;  
     
     if(depth <= maxDepth)
     {
       int width = (int)(node.getWidth()/2);
       int height = (int)(node.getHeight()/2); 
       Rectangle rect1 = new Rectangle();
       Rectangle rect2 = new Rectangle(); // making rectangles for the recursion 
       Rectangle rect3 = new Rectangle();
       Rectangle rect4 = new Rectangle();       
       rect1.setSize(width,height);
       rect1.setLocation((int)(node.getX()), (int)(node.getY()));     
       rect2.setSize(width,height);
       rect2.setLocation((int)(node.getX() + width),(int)(node.getY()));
       rect3.setSize(width,height);
       rect3.setLocation((int)(node.getX()),(int)(node.getY() + height));      
       rect4.setSize(width,height);
       rect4.setLocation((int)(node.getX() + width),(int)(node.getY() + height));// setting up rectangles 
      
       node.kids = new QNode[4];
       
       QNode one = new QNode(rect1,this); // each of the four nodes
       node.kids[0] = one;
       one.setDepth(depth+1);
       
       QNode two = new QNode(rect2,this);
       node.kids[1] = two;
       two.setDepth(depth+1);
       
       QNode three = new QNode(rect3,this);
       node.kids[2] = three;
       three.setDepth(depth+1);
       QNode four = new QNode(rect4,this);
       node.kids[3] = four;
       four.setDepth(depth+1);
      
       
       buildTree(depth+1, one); // recursivly calling them for build tree
       buildTree(depth+1, two);
       buildTree(depth+1, three);
       buildTree(depth+1, four); 

     }
     
   }
   //---------------------- restart() ----------------------------------
   /**
    * restart bullet movement; 
    */
   public void restart() // not sure what was neccessary for this method...
   {    
     // emptyNode( _root );
   //   interNodeBullets.clear();
   }
 
   //----------------- emptyNode( QNode ) ------------------------------------
   /**
    * empty the node and all its children of bullets, move to dead bullets
    */
   private void emptyNode( QNode node )
   {   
    
     // had no need for this method
   }
   
   //------------------------------ newFrame() -----------------------------
   /*
    * newFrame() moves all the bullets forward in time based on vector
    */
   public void newFrame() // new frame method called each time the timer ticks 
   {
     targUpdate(); // updates the targets each time new frame is called
  
     for( int i =0; i < interNodeBullets.size(); i++)
     {
      
       interNodeBullets.get(i).move();
       if( interNodeBullets.get(i).offScreen()) // if its off screen 
       {
        
         Bullet bullet = interNodeBullets.get(i);
         deadBullets.add(bullet);
         interNodeBullets.remove(bullet);
       }
       else{          
         addBullet(interNodeBullets.get(i)); 
       }
     }
     repaint();
     
     if(count++%bulletFrameFire == 0)
     {
       if(numBullets < maxBullets)
       {
         Bullet b = new Bullet(this, new Point2D.Float(0,285), bdx, bdy); 
         interNodeBullets.add(b);
         
         numBullets++; // incrememting counter
       }
       else {
         if(deadBullets.size() > 0) 
         {
           Bullet bull = deadBullets.removeFirst();
           bull.restart();
           bull.setAngle(bdx, bdy);
           interNodeBullets.add(bull);
         }         
       }
     }
   }
   
//--------------------------------target update method---------------------------
   public void targUpdate() // target update method 
   {
     for( int i = 0; i < interNodeBullets.size(); i++)
     {
       Bullet bull = interNodeBullets.get(i);
       bull.move();
       
       for(int j = 0; j < targets.size(); j++)
       {  
         Target targ = targets.get(j);
         if( bull.getRect().intersects(targ.getBounds()) || bull.contains(targ.getBounds()))
         {
           
           deadBullets.add(bull);
           interNodeBullets.remove(bull);
           
           if(!targ.hit()) // if hit is false do this 
           {
           targ.setFrame((float) - 1004, (float) 1004, (float) 20, (float) 20); 
           rebuildTree();
           
           }
           bull.setPosition(-2000, 3000); // sets postion off the screen
         }  
       }
       
     }
   }
   
   
   
   private void getCreateBullet(){ // I didnt need to use this method
   }
   //----------------------- paintComponent( Graphics ) -------------------
   /**
    * paint the field, the bullets and the tree 
    */
   protected void paintComponent( Graphics g ) 
   {
      super.paintComponent( g );
   }
   //----------------------- paint( Graphics ) ---------------------------
   /**
    * paint the field, the bullets and the tree 
    */
   public void paint( Graphics g ) 
   {
     // loops through to draw targets and the nodes
      super.paint( g );
      if ( showTree )
         drawNode( g, _root );
      
      for (Target temp : targets)  // draw the targets 
       temp.draw(g);

       drawBullets(g, _root);      //  draw bullets 
      
   }

   
   //-------------------- drawBullets( Graphics, QNode ) -------------------
   public void drawBullets( Graphics g, QNode node ) // draw bullets method 
   {
    
     if( node.kids[0] == null ) // first case to draw bullets 
     {
            
       for( int i =0; i < node.bullets.size(); i++)
       {
           
         node.bullets.get(i).draw(g, node.getColor());
       }
     }
     else
     {
       for( int i = 0; i < node.kids.length; i++ )
       {
         drawBullets( g, node.kids[i] ); // recurisve call of draw bullets
       }
     }
     
   }
   
   //----------------------- drawNode( Graphics, QNode ) ---------------------
   public void drawNode( Graphics g, QNode node ) 
   {
     if ( node == null )
       return;
     if ( node.kids[0] == null  ) // only gets into this function if there are no kids
     {
      
       g.setColor( node.setBrushColor(node.getDepth())); // sets the colors of them 
       Rectangle r = node.getRectangle();
       
       g.drawRect( r.x , r.y, r.width-2, r.height-2 ); // draws the rectangles
     }
     
     else // otherwise draw this 
     {
       for ( QNode n : node.kids ) 
         drawNode( g, n );
     }
     // recursive call on kids nodes
     
   }
//--------------------------getMinTargets-----------------------
   public int getMinTargets( ){
     return minTargets;
   }
//------------------------sets min targets ------------------------
   public void setMinTargets( int m ){
 minTargets = m;
   }
   
   //------------------- addBullet( Bullet ) --------------------
   /**
    * Adds a bullet to the tree. Starts with the root
    *       
    */
   public void addBullet( Bullet bullet ) 
   { 
         addBullet( _root, bullet ); // helper method for add bullets recursion 
   }
   //------------------- addBullet( QNode, Bullet ) --------------------
   /**
    * Adds a bullet to a node, but only actually adds it to a leaf of the tree
    * Note: this code assumes the bullet is contained in the node, so it
    *       only has to determine which of 4 children it is in.
    */
   public void addBullet( QNode node, Bullet bullet ) 
   {
     if( node.kids[0] == null)
     {
       if (!node.bullets.contains(bullet))
       {   
         node.bullets.add(bullet);
       }
     }
     
     else {
       for( int i = 0; i < node.kids.length; i++)
       {                 
         if (node.kids[i].contains(bullet)) // if the bullet is contained 
         {

           addBullet(node.kids[i], bullet); // call the recursion. 
         }
       }
     }
     
   }

   //----------------------- rebuildTreeWhenDone( ) -------------------------
   public void rebuildTreeWhenDone( ) 
   {
     rebuildTree();
   }
   
   //----------------------- rebuildTree( ) -------------------------
   public void rebuildTree( ) 
   {
      buildTree();
      repaint();
   }
   //---------------------- setMaxDepth( int ) -------------------
   /**
    * Set the maximum depth allowed for expanding the quad tree
    *  and rebuild the tree.
    */
   public void setMaxDepth( int max )
   {
      maxDepth =  max;
      rebuildTree();
   }

   public void setDY( float a ){
      bdy = a;
   }
   
   public void setDX( float s ){
      bdx = s;
   }
   
   public void setAngle( float theta ){
      float radian = theta / 180.0f * (float) Math.PI;
      setDX( (float) (  bulletSpeed * Math.cos( radian )));
      setDY( (float) ( -bulletSpeed * Math.sin( radian )));
     
   }
   
   public void setBFF( int b ){
      bulletFrameFire = b;
   }


   //--------------------- main --------------------------------------------
   /**
    * Convenience main to invoke app
    */
   public static void main( String[] args )
   {
      QuadShoot.main( args );
   }
}
