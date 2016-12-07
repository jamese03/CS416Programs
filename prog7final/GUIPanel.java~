/**
 * GUIPanel.java -- skeleton class
 * Creates the panel for showing cards
 *
 * 03/18/08: rdb
 * 07/13/10: jb, edited for aces up game
 * 07/20/10: jb, edited for Golf game
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;


public class GUIPanel extends JPanel 
{
   //------------------------- instance variables -----------------------
   private Game    _game;              // the game object
   private boolean _showFlag = false;  // whether top draw card is shown
   private boolean _pauseAtLeaf = false; // if true, pause at leaf during build
   
   //------------------------- constructor ----------------------------
   /**
    * GUIPanel constructor creates the game and waits for interaction.
    */
   public GUIPanel() 
   {
      super();                 
      setLayout( new BorderLayout() );

      JPanel gamePanel = new JPanel();
      gamePanel.setLayout( null );
      gamePanel.setBackground( Color.WHITE );

      _game = new Game();
      _game.setLayout( null );
      _game.setBackground( Color.WHITE );
      this.add( _game );

      setSize( 850, 400 );

      ///////////////////////////////////////////////////////
      // build the gui
      ///////////////////////////////////////////////////////
      String[] buttonLabels = { "Deal", "Shuffle", "Make Tree", 
          "Play", "Undo" };
      Component buttonMenu = makeButtonMenu( buttonLabels );
      add( buttonMenu, BorderLayout.NORTH );
      
      Component controlMenu = makeControlMenu();
      add( controlMenu, BorderLayout.SOUTH );
      
      _game.makeNewDeck();
   }
   //------------------- makeControlMenu --------------------------------
   /**
    * create a control menu that includes Spinners for the seed,
    * number of cards in a play pile and number of play piles
    * and radio buttons for Show/Hide and Leaf Pause
    */
   private Component makeControlMenu( )
   {
      JPanel sMenu = new JPanel(); 
      LabeledSpinner cardSpinner = new LabeledSpinner( "Cards/Pile", 2, 5, 3 );
      cardSpinner.addChangeListener( new ChangeListener()
         {  
            public void stateChanged( ChangeEvent ev )
            {  
               JSpinner spinner = (JSpinner) ev.getSource();
               Number value = (Number) spinner.getValue();
               Game.cardsPerPile = value.intValue();
               _game.initialize();
            }
         }     
      );
      sMenu.add( cardSpinner );

      LabeledSpinner pileSpinner = new LabeledSpinner( "# Piles", 3, 7, 3 );
      pileSpinner.addChangeListener( new ChangeListener()
         {  
            public void stateChanged( ChangeEvent ev )
            {  
               JSpinner spinner = (JSpinner) ev.getSource();
               Number value = (Number) spinner.getValue();
               Game.numPlayPiles = value.intValue();
               _game.initialize();
            }
         }     
      );
      sMenu.add( pileSpinner );
 
      LabeledSpinner seedSpinner = new LabeledSpinner( "RandomSeed", 0, 30, 0 );      
      seedSpinner.addChangeListener( new ChangeListener()
      {  
         public void stateChanged( ChangeEvent ev )
         {  
            JSpinner spinner = (JSpinner) ev.getSource();
            Number value = (Number) spinner.getValue();
            Game.seed = value.intValue();
         }
      });
      sMenu.add( seedSpinner );
      
      ///---------- add the 2 toggle buttons -----------------
      JPanel toggles = new JPanel();
      JRadioButton showButton = new JRadioButton( "Show Draw" );
      showButton.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {
            _showFlag = !_showFlag;
            _game.showDrawPile( _showFlag );
         }
      });
      toggles.add( showButton );
      
      JRadioButton pauseButton = new JRadioButton( "Pause at Leaf" );
      pauseButton.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent ev )
         {
            _pauseAtLeaf = !_pauseAtLeaf;
            _game.pauseAtLeaf( _pauseAtLeaf );
         }
      });
      toggles.add( pauseButton );
      sMenu.add( toggles );
      
      return sMenu;
   }
   //------------------- makeButtonMenu --------------------------------
   /**
    * create the button menu for this application
    */
   private Component makeButtonMenu( String[] labels )
   {
      JPanel bMenu = new JPanel(); 
      JButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         button = new JButton( labels[ i ] );
         bMenu.add( button );
         button.addActionListener( new ButtonListener( i ));
      }
      bMenu.setSize( 40, 400 );
      
      return bMenu;
   }
   //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener -- distributes button events to appropriate methods
    *                   of the GUIPanel class.
    */
   public class ButtonListener implements ActionListener
   {
      int _buttonId;
      public ButtonListener( int buttonId )
      {
         _buttonId = buttonId;
      }
      public void actionPerformed( ActionEvent ev )
      {
         String msg = null;
         switch ( _buttonId )
         {
            case 0:
               _game.makeNewDeck();
               break;
            case 1:
               _game.shuffle();
               break;
            case 2:
               _game.makeNewDeck();
               _game.makeTree();
               //_game.printTree();
               break;
            case 3:
                msg = _game.playCard();
                if ( msg != null )
                  endGame( msg );
               break;
            case 4:
               msg = _game.undo();
               if ( msg != null )
                  JOptionPane.showMessageDialog( null, msg );
               break;
          }               
      }
   }
   //------------------- endGame( String ) ----------------------------
   /**
    * If a play can't be performed, this is called showing why a play
    * can't be performed and asks for a new deck
    */
   private void endGame( String msg )
   {
      msg = msg + "\nWant a new deck?";
      int choice = JOptionPane.showConfirmDialog( null, msg );
      if ( choice == 0 )
      {
         _game.makeNewDeck();
         _game.shuffle();
      }
      else if ( choice == 1 )
         System.exit( 0 );
   }
   
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      Golf.main( args );
   }
}   
