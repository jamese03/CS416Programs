/**
 * Chapter 7: GUI.java
 * Creates the panel to be placed inside the SwingApp window.
 *
 * 1/30/08: rdb
 *    Renamed (old name was BallApp)
 * 01/28/09: rdb
 *    Revised to use null Layout
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

public class GUI extends JPanel implements Animated 
{
   //------------ package static variables for command line args -------
   static int     dxInitialMove = 4;
   static int     dyInitialMove = 4;
   
   //------------------------- instance variables -----------------------
 
   private FrameTimer _timer;
   private final int INTERVAL = 100;
   private JPanel panel;
   public Shooter shooter;
   private JLabel tb;
   private int _s= Targets.targetSpeed, _a=0;
   private ArrayList<Pellet> bullets;
   private ColoredShotTarget _colorTargets;
   private LabeledSlider angleSlider;
   private LabeledSlider speedSlider;
   private Boolean lastShot = false;
   private int numberPellets = Targets.numPellets;
   //------------------------- constructor ----------------------------
   public GUI( int w, int h ) 
   {
      super();
      setLayout( new BorderLayout() );    // make sure no layout manager tries to                           
      panel = new JPanel();
      panel.setSize( w, h );
      panel.setLayout( null );
      add( panel );

      buildDisplay(  );        // build the initial display for the app   
      this.setBackground( Color.white );    
     _colorTargets = new ColoredShotTarget(350, 0, panel);
      bullets = new ArrayList<Pellet>();
      _timer = new FrameTimer( INTERVAL, this );
      _timer.start();

   }


   //-----------------------  buildDisplay() --------------------------
   /**
    * encapsulate code that builds display components for startup.
    */
   private void buildDisplay()
   {    
     angleSlider = new LabeledSlider("Angle", -45, 45, 0, JSlider.VERTICAL );
     speedSlider = new LabeledSlider("Speed", 0, 30, 10, JSlider.VERTICAL);    
     angleSlider.addChangeListener( new sliderListener() );  
     speedSlider.addChangeListener( new speedListener() );
     add(angleSlider, BorderLayout.WEST);
     add(speedSlider, BorderLayout.EAST);
     
     
     
     String labels[] = { "Fire", "Reset" };
     JPanel northPanel = new JPanel();
     for ( int i = 0; i < labels.length; i++ )
     {
       JButton button = new JButton( labels[ i ] );
       button. addActionListener( new MyButtonListener( i ));
       northPanel.add( button );
     }
     add( northPanel, BorderLayout.NORTH );  
     shooter = new Shooter( 0, 300, 50, 300, 10 );
     panel.add(shooter, BorderLayout.WEST);   
     
      tb = new JLabel();
      tb.setText("Pellets Left" + numberPellets);
      northPanel.add(tb);
     
   }
   
   /*slider Listener class to get access to the value of the slider 
    * so the set Angle method can work
    * Stephen helped me alot with this part 
    * 
    * */  
   public class sliderListener implements ChangeListener
   {
     public void stateChanged(ChangeEvent e)
     {
       JSlider j = (JSlider)e.getSource();
       shooter.setShotAngle( j.getValue() );
       _a = j.getValue();
       repaint();
     }
   }
//-----------------------same as the other listener to get speed-----------------------
   public class speedListener implements ChangeListener
   {
     public void stateChanged(ChangeEvent e)
     {
       JSlider j = (JSlider)e.getSource();
       _s = j.getValue();
       repaint();
     }
   }
   
   
     
   //++++++++++++++++++++++ MyButtonListener ++++++++++++++++++++++++++
   // public inner class for button event handler:
   public class MyButtonListener implements ActionListener
   {
      int _btnId;  // which button is associated with this ActionListener
      public MyButtonListener( int btnId )
      {
         _btnId = btnId;   // save the id for the actionPerfomed invocation
      }
      public void actionPerformed( ActionEvent ev )
      {
         System.out.println( "Button " + _btnId + " event." );
         //////////////////////////////////////////////////////////////
         // add code here to test _btnId and do what needs to be
         //   done for each button. 
         // As an inner class, code here has access to all instance
         //   variables and methods of the outer class.
         //////////////////////////////////////////////////////////////
         
         switch (_btnId)
         {
           case 0:{
          _timer.start();
        //  System.out.println("Fire" + "Bullets Left: " + bullets.size())    
          if (numberPellets > 0)
          {
            numberPellets--;
            tb.setText("Pellets Left" + numberPellets);    
            Pellet projectile = new Pellet(shooter.getX2(), shooter.getY2()-3, _s, _a);
            bullets.add(projectile);    
            panel.add(projectile);
                    
          }
          break;
           }
           case 1:{
             
             System.out.println("RESET");
             newGame(); // resets the game 
             break;
           }
         }
      }
   }

   //++++++++++++++++++++++++ Animated interface methods +++++++++++++++++++++
   private boolean _animated = true;  // instance variable
   //---------------------- isAnimated() ----------------------------------
   public boolean isAnimated()
   {
      return _animated;
   }
   //---------------------- setAnimated( boolean ) --------------------
   public void setAnimated( boolean onOff )
   {
      _animated = onOff;
   }
   //-------------------------- newFrame() --------------------------------
   /**
    * New Frame method, checks winning conditions and losing conditions 
    * 
    * 
    */
   public void newFrame() 
   {
     _colorTargets.moving(); // moves targets
     for ( int i = 0; i < bullets.size(); i++)
     {
       bullets.get(i).move(); // moves bullets    
       for ( int j = 0; j < _colorTargets.getArrayList().size(); j++)
       {
         boolean check = checkHit(_colorTargets.getArrayList().get(j), bullets.get(i));
         if (check)
         {
           bullets.get(i).setLocation(10000,10000); // sets the location of the bullets to off the screen
           break; // to get out of the loop
         }
         if (bullets.get(i).getXLocation() > 400)
           bullets.get(i).setVisible(false);      
       }  
       
       if (bullets != null && bullets.size() > 0 &&  
           (bullets.get(i).getXLocation() > 400 || bullets.get(i).getXLocation() < 0 ))
       {
         bullets.remove(i); 
         i++;
         
       }
     }   
     if ( _colorTargets.getArrayList().size() == 0) // winning condition
     {
       repaint();
       _timer.stop();
       JOptionPane.showMessageDialog(null, "You win! With: " + numberPellets + " Bullets Remaining"  );
       newGame();
       
     }
     
     if ( bullets.size() == 0 && numberPellets == 0) // losing condition
     {
       _timer.stop();
       JOptionPane.showMessageDialog(null, "You  Lose! With: " +  _colorTargets.getArrayList().size() 
                                       + " Targest remaining");
       newGame();      
     }
     
     this.repaint();
   }   
   
//--------------------------check hit of the targets----------------------
   public boolean checkHit(JRectangle r, Pellet p)
   {  
     /*Was helped in PAC for this method
      * */
     
     if (r == null || p == null )
       return false;
     
     if ( r.getBounds().intersects(p.getBounds()))
     {
       r.setVisible(false);
       p.setVisible(false);

       _colorTargets.delete(r);
       
       return true;
     }   
         
     return false;      
   }
  
//----------------------newGame method for resetting the game------------------
   public void newGame()
   {   
     for ( int i = 0; i < _colorTargets.getArrayList().size(); i++)
     {
       _colorTargets.getArrayList().get(i).setVisible(false);      
     }
     
     for ( int j = 0; j < bullets.size(); j++)
     {
       bullets.get(j).setLocation(600, 600); // so the bullets dont hit a target after they have hid
       bullets.get(j).setVisible(false);
     }
     _colorTargets.clearList();
     _colorTargets = new ColoredShotTarget( 350, 0, panel);    
     bullets = new ArrayList<Pellet>();
     numberPellets = Targets.numPellets;
     tb.setText("Pellets Left" + numberPellets);    
     
     angleSlider.getJSlider().setValue(0);
     speedSlider.getJSlider().setValue(10);
     _timer.start();   
   }
   
   //+++++++++++++++++++ application starter ++++++++++++++++++++
   //-------------------------- main ----------------------------
   public static void main( String[] args )
   {
          Targets app = new Targets( "Targets", args );
   }  
}