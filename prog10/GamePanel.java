/**
 * Chapter 7: GamePanel.java
 * Sanders and van Dam
 * Creates the panel to be placed inside the Lag3 window.
 * Used( with modifications ) in all programs later in this book.
 * Version 3 of 3
 *
 * 1/30/08: rdb
 *    Renamed (old name was BallApp) and added JFrame parameter to constructor
 *    Pass JFrame to BouncingBall
 * 11/24/10: More changes to adapt to the Bouncing Threads assignment
 * 12/03/10: Significant additional GUI functions for Bouncing Threads
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel 
                         implements Animated, MouseListener
{
   //------------------------ class variables ------------------------
   public static int regionRows = 4;
   public static int regionCols = 8;
   public static int startObjects = 10;
   public static int numObjects = 0;
   
   //------------------------ instance variables ---------------------
   private final int FRAME_INTERVAL = 100;  // 100 msec per frame

   private Container     _parent;
   private RegionManager regionMgr;
   private JPanel        drawPanel;
   private LabeledSlider sleepSlider = null;
   private JLabel        objectCountLabel;
   private Random        rng;
   private FrameTimer    timer;
   private JButton       onoff;    // toggles animation on and off
   
   private int           swapTries = 0;
   private int           swapSuccesses = 0;
   private Region        lastOwner = null;
   private GameObject    lastFatality = null;
   
   //--------- animation/gui variables
   private ArrayList<Animated>  _movers;
   
   //--------- magic constants
   private int     panelW = 1000;
   private int     panelH = 785;
   private int     moreObjCount = 40;   // # objects added on each button press
   
   //--------------------- GamePanel -----------------------------
   public GamePanel( Container parent ) 
   {
      super();
      setLayout( new BorderLayout() );
      _parent = parent;
      
      addMouseListener( this );
      
      addDrawPanel();
      addWidgets();
      
      regionMgr = RegionManager.create( panelW, panelH, regionRows, regionCols );
      // create an ArrayList containing all objects that need updating
      //  on each frame.
      _movers   = new ArrayList<Animated>();
      
      // create and start up the FrameTimer
      timer = new FrameTimer( FRAME_INTERVAL, this );
      rng = new Random( 0 );
      
      initGame();
      this.setPreferredSize( new Dimension( panelW, panelH + 80 ));
   }
   //------------------------ initGame() -------------------------------
   /**
    * initialize (or re-initialize) the game
    */
   private void initGame()
   {
      timer.stop();
      drawBoundaries();      
          
      moreObjects( startObjects );
      onoff.setText( "Start" );
   }
   //------------------------ stopGame() -------------------------------
   /**
    * stop the durrent game; send terminate message to manager, 
    * who will then terminate the threads.
    */
   private void stopGame()
   {
      timer.stop();
      regionMgr.terminate();
   }
   //------------------------ restart() -------------------------------
   /**
    * restart the game
    */
   private void restart()
   {
      regionMgr.restart();
      _movers.clear();
      numObjects = 0;
      
      drawPanel.removeAll();
      initGame();
      repaint();
   }

   //---------------------------- addWidgets-------------------------
   /**
    * Add buttons and slider to the GUI
    */
   private void addWidgets()
   {
      JPanel guiPanel = new JPanel();
      addSleepSlider( guiPanel );
      addButtons( guiPanel );
      this.add( guiPanel, BorderLayout.SOUTH );
   }
   //---------------------------- addSleepSlider -------------------------
   /**
    * Add slider to control the wait time for the Regions
    */
   private void addSleepSlider( JPanel parent )
   {
      sleepSlider = new LabeledSlider( "Sleep", 0, 300, 100 )
      {
         public void stateChanged( ChangeEvent ev )
         {
            JSlider slider = (JSlider) ev.getSource();
            int val = slider.getValue();
            RegionManager.sleepDelay =  val;   
         }
      };
      JSlider slider = sleepSlider.getJSlider();
      slider.setMajorTickSpacing( 50 );
      //slider.setMinorTickSpacing( 1 );
      slider.setSize( 400, 40 );

      parent.add( sleepSlider );
   }
   //
   //---------------------------- addButtons -------------------------
   /**
    * Add buttons for toggling between synch/nosynch and to add more
    * objects.
    */
   private void addButtons( JPanel parent )
   {
      //+++++++++++++++++++++++++ More Button ++++++++++++++++++++++++++++
      //----- More: add 40 more objects
      JButton more = new JButton( "More" );
      ActionListener al = new ActionListener()
      {  
         public void actionPerformed( ActionEvent ev )
         {  
            moreObjects( moreObjCount );
         }
      };
      more.addActionListener( al );
      parent.add( more );
      
      //+++++++++++++++++++++++++ Sync Button ++++++++++++++++++++++++++++
      //------- Sync: toggle synchronization
      JToggleButton synch = new JToggleButton( "Sync" );
      al = new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {  
            RegionManager.synchronize = !RegionManager.synchronize;
            System.out.println( "Synchronization: " + RegionManager.synchronize );
         }
      };
      if ( RegionManager.synchronize )
         synch.setEnabled( true );
      synch.addActionListener( al );
      parent.add( synch );  
      
      //++++++++++++++++++++++ Start/Stop Button ++++++++++++++++++++++++++++
      //------- On/Off: toggle animation on/off
      onoff = new JButton( "Start" );
      al = new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {  
            if ( timer.isRunning() )
            {
               timer.stop();
               onoff.setText( "Start" );
            }
            else
            {
               timer.start();
               onoff.setText( "Stop" );
            }
         }
      };
      onoff.addActionListener( al );
      parent.add( onoff );  
      
      //+++++++++++++++++++++++++ Restart Button ++++++++++++++++++++++++++++
      //------- Restart button
      JButton restart = new JButton( "Restart" );
      al = new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {  
            restart();
         }
      };
      restart.addActionListener( al );
      parent.add( restart );    
      
      //+++++++++++++++++++++++++ Terminate Button +++++++++++++++++++++++++
      //------- Terminate button
      JButton terminate = new JButton( "Terminate Threads" );
      al = new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {  
            stopGame();
         }
      };
      terminate.addActionListener( al );
      parent.add( terminate );        
      
      //+++++++++++++++++++++++++ ObjectCount Label +++++++++++++++++++++++++
      //------- Terminate button
      objectCountLabel = new JLabel( "# Objects: " + numObjects  );
      parent.add( objectCountLabel );        
   }
   //---------------------------- addDrawPanel -------------------------
   /**
    * Add panel to show drawing
    */
   private void addDrawPanel()
   {
      drawPanel = new JPanel();
      drawPanel.setLayout( null );
      drawPanel.setSize( panelW, panelH );
      drawPanel.setBackground( Color.white );
      this.add( drawPanel, BorderLayout.CENTER );
   }
   //---------------------------- drawBoundaries -------------------------
   /**
    * draw lines to show region boundaries
    */
   private void drawBoundaries()
   {
      int regionW = panelW / regionCols;
      int regionH = panelH / regionRows;
      for ( int r = 1; r < regionRows; r++ )
      {
         int y = r * regionH;
         JLine hLine = new JLine( 0, y, panelW, y );
         hLine.setColor( Color.BLACK );
         drawPanel.add( hLine );
      }
      for ( int c = 1; c < regionCols; c++ )
      {
         int x = c * regionW;
         JLine vLine = new JLine( x, 0, x, panelH );
         vLine.setColor( Color.BLACK );
         drawPanel.add( vLine );
      }
   }
 
   //++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++
   private boolean      _animated; // not really used for the panel
   //---------------------- newFrame() ----------------------------------
   public boolean isAnimated()
   {
      return _animated;
   }
   //---------------------- setAnimated( boolean ) --------------------
   public void setAnimated( boolean onOff )
   {
      _animated = onOff;
   }
   //---------------------- newFrame() -------------------------------
   public void newFrame() 
   {
      for ( Animated anim: _movers )
      {
         if ( anim.isAnimated() )
            anim.newFrame();
      }
      
      // every n-th frame, randomly remove an object from the set and
      // add it right back in
      /*****
      int N = 1;
      int loser = rng.nextInt( N * _movers.size() );
      if ( loser < _movers.size() )
      {
         GameObject fatality = (GameObject)(_movers.get( loser ));
         Region owner = regionMgr.getRegion( fatality );
         if ( owner != null )
         {
            swapTries++;
            if ( owner.remove( fatality ) )
            {
               if ( lastOwner != null )
                  lastOwner.add( lastFatality );
               lastOwner = owner;
               lastFatality = fatality;
                  
               swapSuccesses++;
            }
            else
               System.err.println( "Couldn't remove from region" );
         }
         if ( swapTries % 100 == 0 )
            System.out.println( "Swaps: " + swapTries + " " + swapSuccesses );
         //moreObjects( 1 );
      }
      /*****************/
      
      this.repaint();
   }
   //---------------------------- moreObjects -------------------------
   /**
    * add more objects to the display
    */
   private void moreObjects( int count )
   {
      for ( int i = 0; i < count; i++ )
      {
         int x = 20 + rng.nextInt( panelW - 40 );
         int y = 20 + rng.nextInt( panelH - 40 );
         makeObject( x, y );
      } 
      objectCountLabel.setText( "# Objects: " + numObjects );
   }
      
   //-------------------- makeObject ------------------------------
   /**
    * make a new GameObject at the specified location; use the 
    * x and y position low bits to determine parameters.
    */
   private void makeObject( int x, int y )
   {
      int size = 10 + ( x + y ) % 20;
      int dx = x % 10 + 3; 
      int dy = y % 10 + 3; 
      if ( dx % 2 == 0 )
         dx = -dx;
      if ( dy % 2 == 0 )
         dy = -dy;
      JAreaShape shape = null;
      Color col = Color.YELLOW;
      if ( size % 2 == 0 )
         shape = new JEllipse( col );
      else
         shape = new JRectangle( col );
      
      GameObject b = new GameObject( drawPanel, shape );
      numObjects++;
      b.setLocation( x, y );
      b.setSize( size, size );
      b.setMove( dx, dy );
      _movers.add( b );
      drawPanel.add( b );
      regionMgr.add( b );
   }
 
   //++++++++++++++++++ MouseListener methods +++++++++++++++++++++++++++
   // You need to implement mousePressed 
   // the others must be there but will remain "empty".
   //
   //------------------- mousePressed( MouseEvent ) ----------------------
   /**
    * When completed, this method will take one of 2 actions:
    *   if the mouse event location is inside one or more draggable objects,
    *      it will initiate dragging of those objects
    *   else
    *      it will generate a new BouncingBall object at the mouse location
    */
   public void mousePressed( MouseEvent me )
   {  
      ///////////////////////////////////////////////////////////////
      // 
      // 1. First, add code to create a new BouncingBall at the mouse position. 
      //    The step increments for the ball's motion should be
      //            xLoc % 21 - 10, yLoc % 21 - 10
      //    The size (both width and height) should be determined by the
      //    formula:
      //            10 + ( xLoc + yLoc ) % 20
      //    The color should be set to YELLOW.
      //////////////////////////////////////////////////////////////////
      Point loc = me.getPoint();
      makeObject( loc.x, loc.y );
      RegionManager.checkThreads();
      RegionManager.report = !RegionManager.report;
   }
   //------------- unused MouseListener methods -----------------
   public void mouseClicked( MouseEvent me ) {}
   public void mouseDragged( MouseEvent me ) {}
   public void mouseEntered( MouseEvent me ) {}
   public void mouseExited( MouseEvent me ) {}
   public void mouseMoved( MouseEvent me ) {}
   public void mouseReleased( MouseEvent me ){}
   //+++++++++++++++++++++ end MouseListener methods ++++++++++++++++++++++
   
   //++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++
   public static void main( String[] args )
   {
      ThreadsApp.main( args );
   }
}
