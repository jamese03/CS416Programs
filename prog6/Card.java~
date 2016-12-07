/** 
 * Card -- models playing cards
 *       It has 2 public enums: one for the suit, one for the rank
 * 
 *       It has a framework for handling mouse events.
 *       It can keep track of the CardGroup it belongs to.
 *       
 * 
 *    mlb
 * 10/01/10 rdb modified to support either Ace HIGH or ACE low
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;

public class Card extends JLabel implements Comparable<Card>, MouseListener
{
   //-------------------------- class variables -----------------------------
    public static final int width = 71, height = 96;

    private static final String CARD_DIR = "cards_gif/";

    public static enum Rank     // symbolic names for the 13 cards, ACE high
                     { TWO, THREE, FOUR, FIVE, SIX,
                       SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    public static enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

   //-------------------------- instance variables --------------------------
    private BufferedImage faceImage, backImage;
    private Rank rank     = null;
    private Suit suit     = null;
    private CardGroup     sRef;
    private boolean       _faceUp = false;
    private JPanel        _parent;
    
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
    }
   //---------------------- getFaceImage -------------------------------
   /**
    * Get the appropriate images to represent the front and backs of the cards
    */ 
   public void getFaceImage(  )
   {
      String fileName = CARD_DIR;
      
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
        
        // append  the rank
        //  the filename rank is rank.ordinal() + 1, for Ace LOW
        //                       rank.ordinal() + 2, for Ace HIGH
        fileName += ( rank.ordinal() + 2 );
        
        // append .gif to the file name
        fileName += ".gif";

       //System.out.println("Image file name: " + fileName );
       try {
          ///////////////////////////////////////////////////////////////////
          // Once you have the filename correct
          // get the faceimage instead of the joker
          // replace the next line with the one following it
          //faceImage = ImageIO.read( new File(CARD_DIR + "rj.gif") );
          
          faceImage = ImageIO.read( new File( fileName ));
       } catch ( Exception e ) { 
          System.err.println( "Card image error: " + e.getMessage() ); 
       }   
   }    
   //---------------------- getBackImage -------------------------------
   /**
    * Get the back image for all cards
    */ 
   public void getBackImage(  )
   {
      try {
         backImage = ImageIO.read( new File( CARD_DIR + "rb.gif" ));
      } catch ( Exception e ) { 
         System.err.println( "Card constructor image error: " 
                               +  e.getMessage() ); 
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
        if( _faceUp )
          brush2.drawImage( faceImage, 0, 0, null );
        else
          brush2.drawImage( backImage, 0, 0, null );
    }

   //--------------------- mouseClicked --------------------------
   /**
    * mouseClicked -- unused
    */ 
   public void mouseClicked( MouseEvent e ) 
   {
      System.out.println( "Card Clicked - " + this );      
   }
   
   //----------------------------------------------------------
   /**
    * Unimplemented  mouse methods
    */ 
   public void mousePressed( MouseEvent e ) { }
   public void mouseReleased( MouseEvent e ) {}
   public void mouseEntered( MouseEvent e ) {}
   public void mouseExited( MouseEvent e ) {}
       
   //--------------------- compareTo ------------------------------
   /**
    * compareTo uses the Rank as the major comparison, the Suit
    * as a secondary one.
    */ 
   public int compareTo( Card o ) 
   {
      if ( this.rank == o.rank )
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
      return compareTo( o ) == 0;
   }
   
   //-------------------------- toString -------------------------
   /**
    * toString()
    */ 
   public String toString() 
   {
      return this.rank + " of " + this.suit;
   }
   //------------------- main unit test ---------------------------------
   public static void main( String[] args )
   {
      //////////////////////////////////////////////////////
      // Edit test constructors to use Rank enum constants
      //////////////////////////////////////////////////////
      Card c1 = new Card( null, Rank.TEN, Suit.HEARTS ); // 10 of hearts
      System.out.print( c1 + " --- " );
      System.out.println( c1  );
      c1.setFaceUp( true );
      
      Card c2 = new Card( null,  Rank.QUEEN, Suit.SPADES );     // King of Spades
      System.out.print( c2 + " --- " );
      System.out.println( c2 );
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
