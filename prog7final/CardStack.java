/**
 * CardStack -- a stack of cards
 * 
 * Built on Stack.
 * 
 * @author rdb
 * March 8, 2009
 * 
 * mlb 10/09
 * 
 * 03/19/11 rdb: added show( boolean ) method to show/hide the background
 *               shape.
 */
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CardStack extends ArrayList<Card> implements CardGroup
{
   //--------------- instance variables ------------------------ 
   private int    _xLoc;
   private int    _yLoc;
   private int    _xOffset = 0;
   private int    _yOffset = 0;
   private JLabel _base;
   private JPanel _parent;
   
   //--------------------- constructors -------------------------
   /**
    * construct a CardStack at 0,0 
    */
   public CardStack( JPanel parent )
   {
      this( parent, 0, 0 );
   }
   /**
    * construct a CardStack at x,  
    */ 
   public CardStack( JPanel parent,  int x, int y )
   {
      super(); 
      
      _parent = parent;
      _xLoc = x;
      _yLoc = y;
      setLocation( _xLoc, _yLoc );
      
      _base = new JLabel();
      _base.setSize( Card.width, Card.height );
      _base.setLocation( _xLoc, _yLoc );
      _base.setBorder( new LineBorder( Color.BLACK, 1 ));
      _base.setText( "  Empty" );
      _base.setOpaque(true);
      _base.setBackground( Color.LIGHT_GRAY );
      parent.add( _base );
   }
   
   //------------------ push ---------------------------------
   /**
    * push a card onto the stack
    */ 
   public Card push( Card c )
   {
      if ( c == null )
         throw new java.lang.NullPointerException(); 
      if ( add( c ) )
         return c;
      else 
         return null;
   }
   
   //--------------------- setXOffset( int )--------------------------
   /**
    * set the offset in x for showing the edges of the cards in the stack
    */ 
   public void setXOffset( int m )
   {
      _xOffset = m;  
   }    
   
   //---------------------setYOffset( int )-----------------------
   /**
    * set the offset in y for showing the edges of the cards in the stack
    */ 
   public void setYOffset( int m )
   {
      _yOffset = m;  
   }
   
   //--------------------- displayCards( int ) --------------------------------
   /**
    * show the cards in the stack at their "correct" locations. This
    * needs to be called whenever a card in the stack as been added,
    * removed or moved.
    * 
    * if "face" is true, the top "num" cards should be face up.
    * if num < 0, then all cards should be face up
    */ 
   public void displayCards( int num, boolean face )
   {
      int size = size();
      if (  size == 0 )
         return;
      int show = Math.min( num, size );  // number to show
      if ( num < 0 )
         show = size;
      int firstToShow = size;
      if ( face )
         firstToShow = size - show;
      
      int count = 0;
      
      // place from bottom of stack
      for ( int c = 0; c < size(); c++ )
      {
         Card card = get( c );  
         if ( c >= firstToShow )
            card.setFaceUp( true );
         else
            card.setFaceUp( false );     
         card.setLocation( _xLoc + _xOffset * count,
                          _yLoc + _yOffset * count++ );
         
         _parent.setComponentZOrder( card, 0 );
      }
   }
   
   //------------------- top() -------------------------
   /**
    * returns the top of the stack without popping. The top is the last
    * entry in the array. 
    */
   public Card top()
   {
      if ( size() == 0 )
         return null;
      return get( size() - 1 );
   }
   //------------------- pop() -------------------------
   /**
    * returns and removes the top of the stack, entry size - 1
    * the stack. 
    */
   public Card pop()
   {
      if ( size() == 0 )
         return null;
      return remove( size() - 1 );
   }
   
   //------------------- setLocation( int, int ) ----------------------
   public void setLocation( int x, int y )
   {
      _xLoc = x;
      _yLoc = y;
      if ( _base != null )
         _base.setLocation( x, y );
      for ( int i = 0; i < this.size(); i++ )
         this.get( i ).setLocation( x + _xOffset * i, y + i  * _yOffset );
   }
   
   //------------------- getXLocation() --------------------------
   /**
    * return the x location for the display of this card stack
    */   
   public int getXLocation()
   {
      return _xLoc;
   }
   
   //------------------- getYLocation() --------------------------
   /**
    * return the y location for the display of this card stack
    */
   public int getYLocation()
   {
      return _yLoc;
   }
   
   //-------------------- show( boolean ) ---------------------------------
   /**
    * can hide/show the entire CardStack by adding/removing it from the panel
    */
   public void show( boolean show )
   {
      _parent.remove( _base );  // make sure we don't get it twice
      if ( show )
         _parent.add( _base );
   }
}