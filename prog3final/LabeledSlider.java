/** 
 * LabeledSlider.java:
 * 
 * A utility class that combines a label and slider and simplifies
 * the code needed to modify a value. 
 * 
 * 02/08/11 rdb -Revised implementation to use BoxLayout. Allows 
 *               better labeling for vertical sliders, and to allow the
 *               label and JSlider components to be different sizes.
 *              -removed the internal ParameterListener class;
 *               users just use addListener
 * 0919/10 rdb Added getJSlider() method so rest of JSlider methods
 *              are accessible.
 *
 * Created Spring 2009
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.*;
import java.awt.Dimension;

public class LabeledSlider extends JPanel 
{
   //-------------------- instance variables ----------------------------
   JSlider _slider;   // references to the 2 components
   JLabel  _label; 
   
   //------------------------ constructors ------------------------------
   /**
    * int argument constructor defines a "value" slider
    *       (as opposed to a % slider).
    */
   public LabeledSlider ( String name, int min, int max, int val, int direction ) 
   {
      super();

      setAlignmentX( 0.5f );
      //setAlignmentY( 0.5f );
      
      // Create the label and slider and initialize their parameters.
      _label = new JLabel( name, SwingConstants.LEFT );
      _slider = new JSlider( direction, min, max, val );
      _slider.setBorder( new LineBorder( Color.BLACK, 2 ));
      
      if ( direction == JSlider.VERTICAL )
         setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ));
      else
         setLayout( new BoxLayout( this, BoxLayout.X_AXIS ));
      
      add( _label );
      add( _slider );
     
      //------- set up some ticks ---------------------------
      _slider.setPaintTicks( true );
      _slider.setPaintLabels( true );
      
      // a little heuristic to guess at reasonable tick marks
      int majorTicks;
      int range = max - min;
      int numScores = range / 20 + 1;
      int numCs = range / 100 + 1;
      if ( numCs >= 3 )
         majorTicks = 100;
      else if ( numScores >= 2 )
         majorTicks = 20;
      else
         majorTicks = 1;
      int minorTicks = majorTicks / 10;
      if ( minorTicks == 0 )
         minorTicks = 1;
      
      _slider.setMajorTickSpacing( majorTicks );
      _slider.setMinorTickSpacing( minorTicks );
      this.add( _slider );
   }
 
   /**
    * Constructor for default Horizontal layout
    */
   public LabeledSlider ( String name, int min, int max, int val ) 
   {
      this( name, min, max, val, JSlider.HORIZONTAL );
   }

   //--------------------- getJSlider() -----------------------------------
   /**
    * return the underlying JSlider for more parameter modification options
    */
   public JSlider getJSlider()
   {
      return _slider;
   }
   //---------------- addChangeListener( ChangeListener ) -------------------
   /** 
    * OR, the app can call this method to specify the change listener.
    */
   public void addChangeListener( ChangeListener listener )
   {
      _slider.addChangeListener( listener );
   }
}
