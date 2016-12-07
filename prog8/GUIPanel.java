/**
 * GUIPanel for QuadShoot assignment
 * 
 * Michael du Breuil
 * Summer 2010
 * 
 * Edited by rdb
 * April 2011
 * 
 * Edited by JB
 * Summer 2011
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUIPanel extends JFrame 
{   
   //--------------- class variables ----------------------------------
   public static int maxMaxDepth = 6;
   
   //--------------- instance variables --------------------------------
   private Game  field;
   
   //------------------ constructor -------------------------------------
   public GUIPanel( Game field ) 
   {
      super( "Control Panel" );
      JPanel panel = new JPanel( new FlowLayout());
      this.field = field;
      
      JPanel sliders = new JPanel();
      sliders.setLayout( new GridLayout( 3, 1 ));
      sliders.add( new BPSSlider() );
      sliders.add( new MinSlider() );
      sliders.add( new AngleSlider() );
      panel.add( sliders );
      
      JPanel buttonPanel = new JPanel( new GridLayout( 1, 2 ));
      buttonPanel.add( showHideButton( "Tree" ));
      LabeledSpinner depthSpinner = 
         new LabeledSpinner( "Tree depth", 0, maxMaxDepth, 4 );
      depthSpinner.addChangeListener( new DepthListener() );
      buttonPanel.add( depthSpinner );
      panel.add( buttonPanel );
      
      this.add( panel );
      
      //this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
      this.pack();
      this.setSize( new Dimension( 550, 300 ));
   }
   //--------------- showHideButton( String ) --------------------------
   /**
    * Create a toggle button to show/hide a feature
    */
   private JRadioButton showHideButton( String feature )
   {
      JRadioButton showButton = new JRadioButton( "Show " + feature );
      //showButton.setMnemonic( KeyEvent.VK_B );
      showButton.setActionCommand( feature );
      showButton.setSelected( true );
      showButton.addActionListener( new RadioListener());
      return showButton;
   }
   
   //+++++++++++++++++++++++ RadioListener inner class ++++++++++++++++++++
   /**
    * private inner class to handle button presses
    */
   private class RadioListener implements ActionListener 
   {      
      public void actionPerformed( ActionEvent e ) 
      {
         if( e.getActionCommand().equals( "Tree" )) {
            field.showTree = !field.showTree;
            field.repaint();
         }
      }
   }
   //+++++++++++++++++++++++++++ DepthListener class +++++++++++++++++++++++++
   /**
    * responds to a Spinner that sets the max recursion depth. The value
    * is assigned to a QuadShoot class variable, maxDepth.
    */
   public class DepthListener implements ChangeListener
   {
      //------------------- stateChanged -----------------------------
      public void stateChanged( ChangeEvent ev )
      {
         JSpinner spinner = (JSpinner) ev.getSource();
         Number spinValue = (Number) spinner.getValue();
         field.setMaxDepth( spinValue.intValue() );
         field.repaint();
      }
   }
   
   private class MinSlider extends LabeledSlider
   {
      public MinSlider()
      {
	  super( "Min Targets / Node", 1, 15, field.getMinTargets() );
      }      
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         int newValue = slider.getValue();
         field.setMinTargets( newValue );
	 field.rebuildTreeWhenDone();
      }
   }
   
   private class BPSSlider extends LabeledSlider
   {
      public BPSSlider()
      {
         super( "Bullet Speed", 1, 30, Game.bulletFrameFire );
      }      
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         int newValue = slider.getValue();
         field.setBFF( newValue );
      }
   }
   
   private class AngleSlider extends LabeledSlider
   {
      public AngleSlider()
      {
         super("Angle", -90, 90, 0 );
         JSlider js = getJSlider();
         //System.out.println( "Ticks: " + js.getMajorTickSpacing() );
         js.setMajorTickSpacing( 30 );
         //System.out.println( "Ticks: " + js.getMajorTickSpacing() );
         js.setMinorTickSpacing( 0 );
         js.revalidate();
      }
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         //System.out.println( "Event: " + slider.getMajorTickSpacing() );
         int newValue = slider.getValue();
         if ( newValue < -88 )
            newValue = -88;
         else if ( newValue > 88 )
            newValue = 88;
         field.setAngle( (float) newValue );
      }
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
