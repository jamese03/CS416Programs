/** 
 * Card -- models playing cards
 *       It has 2 public enums: one for the suit, one for the rank
 * 
 *       It has a framework for handling mouse events.
 *       It can keep track of the CardGroup it belongs to.
 *    mlb
 * 
 * 03/16/11 rdb modified to support card comparison of just rank, or
 *              rank then suit via a class variable.
 * 10/01/10 rdb modified to support either Ace HIGH or Ace LOW
 *              This requires editing the code to change the definition of
 *              the Rank enum.
 */

import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;

public class Card extends JLabel implements Comparable<Card>, MouseListener
{
   //-------------------------- class variables -------------------------------
   public  static boolean compareUsesSuit = false;
   public  static boolean aceHigh         = false;
   
   public  static final int width = 71, height = 96;
   
   // The arrays below give short character representations for the card's rank
   // Uncomment whichever is correct for your application.
   //
      
   private static final String CARD_DIR = "cards_gif/";
   private static BufferedImage backImage = null;
   
   /** ACE low version */
   public  enum Rank     // symbolic names for the 13 cards with Ace high
   { ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, 
     TEN, JACK, QUEEN, KING }
   /*******************/
   /** ACE high version * /
   public  enum Rank     // symbolic names for the 13 cards with Ace high
   { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
     TEN,  JACK, QUEEN, KING, ACE }
   /*******************/
   
   public static enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
   
   //-------------------------- instance variables -----------------------------
   private BufferedImage faceImage;
   private Rank          rank = null;
   private Suit          suit = null;
   private CardGroup     sRef;
   private boolean       _faceUp = false;
   private Point         lastPoint;
   private JPanel        _parent;
   private ArrayList<MouseListener> _listeners;
   //------------------------ Constructor ---------------------------------
   public Card( JPanel frame, Rank r, Suit s )
   {
      rank = r;
      suit = s;
      this.setSize( width, height );
      _parent = frame;
      
      addMouseListener( this );
      if ( backImage == null ) // only read the backImage once
         getBackImage();
      getFaceImage();  
      _listeners = new ArrayList<MouseListener>();     
   }
   //---------------------- getFaceImage -------------------------------
   /**
    * Get the appropriate images to represent the front and backs of the cards
    * The file names for clubs are:
    *     c1.gif == ace
    *     c2.gif == 2
    *     c3.gif == 3
    *      ...
    *     c10.gif == 10
    *     c11.gif == jack
    *     c12.gif == queen
    *     c13.gif == king
    *     c14.gif == ace
    * The other suits are the same except the first letter is d or h or s.
    */ 
   public void getFaceImage(  )
   {
      String fileName = "";
      
      ////////////////////////////////////////////////////////////////
      // Construct the string name of the correct image file
      // The image files have names such as "d1" for the ace of
      // diamonds and c12 for the queen of clubs. You need to build
      // the name based on the suit and the ordinal of the rank
      //  (rank.ordinal()). The filenames end in ".gif"
      //  
      // use a switch of the form...
      //     switch ( this.suit ) 
      //     {
      //       case CLUBS:
      //         fileName += "c";
      //         break;
      //       case DIAMONDS:
      //         etc... 
      //  
      /////////////////////////////////////////////////////////////////
      
      switch ( this.suit ) 
      {
         case CLUBS:
            fileName += "c";
            break;
         case DIAMONDS:
            fileName += "d";
            break;
         case HEARTS:
            fileName += "h";
            break;
         case SPADES:
            fileName += "s";
            break;
      }
      
      ///////////////////////////////////////////////////////////////
      // append  the rank
      // If you want Ace to be LOW
      //    the filename rank is rank.ordinal() + 1
      // If you want Ace to be HIGH
      //    the filename rank is rank.ordinal() + 2 
      //
      // you must put the expression in parenthesis so the integer
      // summation is done before Java converts the int to a string
      // in order to concatenate it with the fileName String variable value
      //////////////////////////////////////////////////////////////
      
      if ( aceHigh )
         fileName += ( rank.ordinal() + 2 );
      else
         fileName += ( rank.ordinal() + 1 );
      
      // append .gif to the file name
      fileName += ".gif";
      
      //System.out.println( "Image file name: " + fileName );
      try 
      {
         ///////////////////////////////////////////////////////////////////
         // Once you have the filename built
         // get the face image 
         ///////////////////////////////////////////////////////////////////
         if ( fileName.length() == 0 )
            fileName = "rj.gif"; // default name gets a joker
         
         faceImage = ImageIO.read( new File( CARD_DIR + fileName ));
         
      } 
      catch ( Exception e ) 
      { 
         System.err.println( "Card face image error: " + e.getMessage() ); 
      }   
   }    
   //---------------------- getBackImage -------------------------------
   /**
    * Get the back image for all cards; you can choose blue or red backs!
    */ 
   public void getBackImage(  )
   {
      String backImageFile = "b1fv.gif";  // blue back
      //String backImageFile = "b2fv.gif";  // red back
      
      try 
      {
         backImage = ImageIO.read( new File( CARD_DIR + backImageFile ));
      } 
      catch ( Exception e ) 
      { 
         System.err.println( "Card back image error: " + e.getMessage() ); 
      }    
   }
   //--------------------- setFaceUp( boolean ) --------------------
   /**
    * set the face up status of the card
    */ 
   public void setFaceUp( boolean b )
   {
      _faceUp = b;   
   }   
   //----------------------- getSuit() -------------------------
   /**
    * return the suit of this card
    */ 
   public Suit getSuit() 
   {
      return this.suit;
   }  
   //--------------------- getRank ------------------------------
   /**
    * return the rank of this card
    */ 
   public Rank getRank() 
   {
      return rank;
   }   
   //--------------------- setCurStack ------------------------
   /**
    * set the stack containing this card
    */ 
   public void setCurStack( CardGroup s ) 
   {
      this.sRef = s;
   }
   
   //--------------------- getCurStack ------------------------
   /**
    * return a reference to the CardGroup containing this card
    */ 
   public CardGroup getCurStack() 
   {
      return this.sRef;
   }
   //-------------------- paintComponent ------------------------
   /**
    * paintComponent draws the card as either face up or face down
    */ 
   public void paintComponent( java.awt.Graphics brush )
   {
      super.paintComponent( brush ); 
      Graphics2D brush2 = (Graphics2D) brush;
      if ( _faceUp )
         brush2.drawImage( faceImage, 0, 0, null );
      else
         brush2.drawImage( backImage, 0, 0, null );
   }
   
   //--------------------- mouseClicked --------------------------
   /**
    * mouseClicked -- unused
    */ 
   public void mousePressed( MouseEvent e ) 
   {
      System.out.println( "Card Clicked - " + this );   
      for ( MouseListener listener: _listeners )
         listener.mousePressed( e );
   }
    
   //----------------------------------------------------------
   /**
    * Unimplemented  mouse methods
    */ 
   public void mouseClicked( MouseEvent e ) { }
   public void mouseReleased( MouseEvent e ) {}
   public void mouseEntered( MouseEvent e ) {}
   public void mouseExited( MouseEvent e ) {}
   
   //--------------------- compareTo ------------------------------
   /**
    * compareTo uses the Rank as the major comparison; if the
    * class variable compareUsesSuit is true, then the Suit
    * is used as a secondary comparison.
    */ 
   public int compareTo( Card o ) 
   {
      if ( this.rank == o.rank && compareUsesSuit )
         return this.suit.ordinal() - o.suit.ordinal();
      else
         return this.rank.ordinal() - o.rank.ordinal();
   }
   
   //----------------------- equals ---------------------------
   /**
    * equals uses the Rank and Suit components
    */ 
   public boolean equals( Card o )
   {
      return this.rank == o.rank && this.suit == o.suit;
   }

   //-------------------------- toString -------------------------
   /**
    * toString()
    */ 
   public String toString() 
   {
      return this.rank + " of " + this.suit;
   }
   //---------------------- addListener( MouseListener ) -----------------
   /**
    * Add a listener to this card's mouse events
    */
   public void addListener( MouseListener ml )
   {
      _listeners.add( ml );
   }
   //--------------------- removeListener( MouseListener ) -----------------
   /**
    * remove a listener from list of active listeners.
    */
   public void removeListener( MouseListener ml )
   {
      _listeners.remove( ml );
   }
    //------------------- main unit test ---------------------------------
   public static void main( String[] args )
   {
      //////////////////////////////////////////////////////
      // Edit test constructors to use Rank enum constants
      //////////////////////////////////////////////////////
      Rank rank;
      Suit suit;
      // Create Heart Card with rank, 8:
      //     8 is a 10 if Ace is High
      //     8 is a 9 if Ace is Low
      //rank = 8;
      rank = Rank.TEN;
      suit = Suit.HEARTS;
      
      Card c1 = new Card( null, rank, suit );
      System.out.println( rank + ", " + suit +  " --- " + c1 );
      c1.setFaceUp( true );
      
      // Create a Space Card with rank 12,
      //     11 is either a Queen or King
      //rank = 11;
      rank = Rank.KING;
      suit = Suit.SPADES;
      
      Card c2 = new Card( null,  rank, suit ); 
      System.out.println( rank + ", " + suit +  " --- " + c2 );
      c2.setFaceUp( true );
      /****/
      
      //------ graphical test -------------------
      JFrame f = new JFrame();
      f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      f.setLayout( null );
      
      c1.setLocation( 50, 50 );
      f.add( c1 );
      c2.setLocation( 200, 50 );
      f.add( c2 );
      
      f.setSize( 600, 200 );
      f.setVisible( true );      
   }   
}
