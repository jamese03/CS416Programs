/**
 * FractalGUI -- based on the GUI class for the canonical 416 application.
 *               This version presents the GUI for the rectangle fractal 
 *               application program
 * 
 * rdb
 * 
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FractalGUI extends JPanel 
{
   //---------------- class variables ------------------------------
   
   //--------------- instance variables ----------------------------
   
   private FractalEllipse _shape;

   private  JPanel      _drawPanel;
   private  JSlider     _sizeSlider;
   private  JSlider     _offsetSlider;
   private  JSlider     _hwRatioSlider;
   private  JSlider     _widthSlider; 
   
   //------------- constants
   private  double   defaultOffset    = FractalEllipse.offset;
   private  double   defaultSizeRatio = FractalEllipse.sizeRatio;
   private  double   default_hwRatio  = FractalEllipse.hwRatio; 
   private  int      defaultWidth     = FractalEllipse.width;
   private  int      maxMaxDepth      = 6;
   
   //------------------ constructor ---------------------------------
   /**
    * Construct the GUI components and the first fractal rectangle
    */
   public FractalGUI( int w, int h )     
   {
      this.setLayout( new BorderLayout() );
      buildGUI();
      
      _drawPanel = new JPanel()
      { public void paintComponent( Graphics brush )
         {  
           super.paintComponent( brush );
           _shape.draw( (Graphics2D) brush );
         }
      };
      _drawPanel.setLayout( null );
      _drawPanel.setSize( w + 50, h - 185 ); // guess at size to start with
      //_drawPanel.setBackground( Color.BLACK );
      this.add( _drawPanel, BorderLayout.CENTER );

      makeScene();
   }
   //--------------------- makeScene() -----------------------------
   /**
    * generate the fractal shape based on current parameters
    *    1) the task of creating the children AND their children
    *       and so on (the constructor)
    *    2) the task of determining the locations and sizes of 
    *       everybody (propagate)
    */
   private void makeScene()
   { 
      int w = FractalEllipse.width;
      int h = (int) ( w * FractalEllipse.hwRatio );
      
      int x = ( (int)_drawPanel.getWidth() - w ) / 2;
      int y = ( (int)_drawPanel.getHeight() - h ) / 2;
      
      _shape = new FractalEllipse( 1, new Point( x, y ), w, h );
      
      this.repaint();
   }
   //--------------------- buildGUI() ---------------------------------------
   /**
    * create all the GUI components
    */
   private void buildGUI()
   {
      JPanel sliders = new JPanel();
      sliders.setBorder( new LineBorder( Color.BLACK, 2 ));
      sliders.setLayout( new GridLayout( 0, 2 ));
      
      buildSliders( sliders );
      add( sliders, BorderLayout.SOUTH );
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder( new LineBorder( Color.BLACK, 2 ));
      buildButtons( buttonPanel );
            
      add( buttonPanel, BorderLayout.NORTH );
   }
   
   //--------------------- buildSliders( Container ) ------------------------
   /**
    * create the sliders in the Container passed as a parameter
    */
   private void buildSliders( Container sliders )
   {   
      LabeledSlider slider;
      //------------- Size Slider  ------------------------------------
      int initVal;
      initVal = (int)Math.round( FractalEllipse.sizeRatio * 100 );
      slider = new LabeledSlider( "Child size ratio", 0, 100, initVal );
      slider.addChangeListener( new SizeListener() );
      sliders.add( slider );
      _sizeSlider = slider.getJSlider();
 
      //------------- Offset Slider  --------------------------------
      initVal = (int)Math.round( FractalEllipse.offset * 100 );
      slider = new LabeledSlider( "Child offset %", -100, 100, initVal );
      slider.addChangeListener( new OffsetListener() );
      sliders.add( slider );
      _offsetSlider = slider.getJSlider();
 
      //------------- Height/Width Ratio Slider  ------------------------
      initVal  = (int)( FractalEllipse.hwRatio * 100 );
      slider = new LabeledSlider( "HW Ratio", 25, 200, initVal );
      slider.addChangeListener( new HWratioListener() );
      sliders.add( slider );
      _hwRatioSlider = slider.getJSlider();
      
      //------------- width Slider  ------------------------------------
      initVal = FractalEllipse.width;
      slider = new LabeledSlider( "Initial width", 100, 300, initVal );
      slider.addChangeListener( new WidthListener() );
      sliders.add( slider );
      _widthSlider = slider.getJSlider();
   }

   //--------------------- buildButtons( Container ) ------------------------
   /**
    * create the buttons and spinner in the Container passed as a parameter
    */
   private void buildButtons( Container buttonPanel )
   {      
      LabeledSpinner depthSpinner = new LabeledSpinner( 
                                       "Recursion depth", 0, maxMaxDepth, 1 );
      depthSpinner.addChangeListener( new DepthListener() );
      buttonPanel.add( depthSpinner );
                 
      LabeledSpinner countSpinner = new LabeledSpinner( 
                                        "Child Count", 1, 8, 4 );
      countSpinner.addChangeListener( new NumChildListener() );
      buttonPanel.add( countSpinner );
                 
      JRadioButton rButton = new JRadioButton( "Fill" ); 
      rButton.addActionListener( new FillListener() );
      rButton.setSelected( true );
      buttonPanel.add( rButton );
      
      
      JButton button = new JButton( "Reset parameters" );
      button.addActionListener( new ResetListener() );
      button.setSelected( false );
      buttonPanel.add( button );
   }
   //--------------------- reset() ------------------------------------------
   /**
    * reset all generation parameters to their initial values (except for 
    * the spinner.
    */
   private void reset()
   {
      FractalEllipse.sizeRatio = defaultSizeRatio;
      FractalEllipse.offset    = defaultOffset;
      FractalEllipse.width     = defaultWidth;     
      FractalEllipse.hwRatio   = default_hwRatio;
      
      _sizeSlider.setValue( (int) Math.round( FractalEllipse.sizeRatio * 100 ) );
      _offsetSlider.setValue( (int) Math.round( FractalEllipse.offset * 100 ) );
      _widthSlider.setValue( FractalEllipse.width );
      _hwRatioSlider.setValue( (int) Math.round( FractalEllipse.hwRatio * 100 ));
      makeScene();      
   }
 
      
   //++++++++++++++++++ inner classes ++++++++++++++++++++++++++++++++++
   //+++++++++++++++++++++ SizeListener class +++++++++++++++++++++++++
   /**
    * SizeListener inner class - responds to slider that specifies
    * the size ratio of a child to its parent.
    * The integer percentage value from the slider must be converted to a
    * floating point (double) fraction. This value is assigned to the 
    * FractalEllipse class variable, sizeRatio, but only if the value is
    * greater than or equal to 5.
    */
   public class SizeListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      /**
       * Invoked whenever user changes the state of a slider
       */
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         int val = slider.getValue();
         if ( val >= 5 )
            FractalEllipse.sizeRatio = slider.getValue() / 100.0;
         makeScene();
      }
   }
   //+++++++++++++++++++++ OffsetListener class +++++++++++++++++++++++++
   /**
    * OffsetListener inner class - responds to slider that specifies
    * the offset of the position of a child triangle on its parent's edge.
    * The integer percentage value from the slider must be converted to a
    * floating point (double) fraction. This value is assigned to the 
    * FractalEllipse class variable, offset. 
    */
   public class OffsetListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      /**
       * Invoked whenever user changes the state of a slider
       */
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalEllipse.offset = slider.getValue() / 100.0;
         makeScene();
      }
   }
   //+++++++++++++++++++++ WidthListener class +++++++++++++++++++++++++
    /**
    * WidthListener inner class - responds to slider that specifies
    * the position of the projection of p2 onto the line formed by p0 and p1.
    * The integer percentage value from the slider must be converted to a
    * floating point (double) fraction.This value is assigned to the 
    * FractalEllipse class variable, p2projection. 
    */
   public class WidthListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      /**
       * Invoked whenever user changes the state of a slider
       */
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalEllipse.width = slider.getValue();
         makeScene();
      }
   }
   
   //+++++++++++++++++++++ HWratioListener class +++++++++++++++++++++++++
   /**
    * responds to a slider that sets the height for the initial triangle. 
    * The value is assigned to an instance variable of this class, height.
    */
   public class HWratioListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalEllipse.hwRatio = slider.getValue() / 100.0;
         makeScene();
      }
   }
   
   //+++++++++++++++++++++++++++ DepthListener class +++++++++++++++++++++++++
   /**
    * responds to a Spinner that sets the max recursion depth. The value
    * is assigned to a FractalEllipse class variable, maxDepth.
    */
   public class DepthListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      public void stateChanged( ChangeEvent ev )
      {
         JSpinner spinner = (JSpinner) ev.getSource();
         Number spinValue = (Number) spinner.getValue();
         FractalEllipse.maxDepth = spinValue.intValue();
         makeScene();
      }
   }
   
   //+++++++++++++++++++++++ NumChildListener class +++++++++++++++++++
   /**
    * responds to a Spinner that sets the max recursion depth. The value
    * is assigned to a FractalEllipse class variable, maxDepth.
    */
   public class NumChildListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      public void stateChanged( ChangeEvent ev )
      {
         JSpinner spinner = (JSpinner) ev.getSource();
         Number spinValue = (Number) spinner.getValue();
         FractalEllipse.numChildren = spinValue.intValue();
         makeScene();
      }
   }
   
   //+++++++++++++++++++++++++++ FillListener class +++++++++++++++++++++++++
   /**
    * responds to an event from a JRadioButton that identifies whether 
    * to fill the triangles or just draw outlines
    */
   private class FillListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         FractalEllipse.fill = !FractalEllipse.fill;
         makeScene();
      }
   }

   //+++++++++++++++++++++++++++ ResetListener class +++++++++++++++++++++++++
   /**
    * responds to an event from a JButton that identifies that the generation
    * parameters should be reset to their initial values
    */
   private class ResetListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         reset();
      }
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
