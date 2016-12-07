/**
 * Hearts -- App class for experimenting with JLabels, enums and a card
 *            game tool
 * rdb
 * 03/10/09
 */
import javax.swing.*;
import java.awt.*;

public class Hearts extends JFrame
{
   //---------------------- instance variables ----------------------
   private GUIPanel _appPanel;      // the app's JPanel
   
   //--------------------------- constructor -----------------------
   public Hearts( String title )     
   {
      super( title );
      
      this.setBackground( Color.LIGHT_GRAY );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      _appPanel = new GUIPanel();
      this.add( _appPanel );
      
      this.setSize( _appPanel.getWidth(), _appPanel.getHeight() + 100 );
      
      this.setVisible( true );
   }
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      new Hearts( "Hearts" );
   }
}
