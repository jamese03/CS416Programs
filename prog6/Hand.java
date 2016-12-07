/**
 * Hand - a class to encapsulate the information about a set of cards
 *        that represent a "hand" for a game like hearts or whist.
 *
 * @author rdb
 * 10/04/10
 *
 * 03/21/2011 rdb: added remove( Card )
 */

import java.awt.*;

public class Hand implements CardGroup
{
   //--------------------- instance variables ---------------------
   private int        xLoc;
   private int        yLoc;
   private int        xOffset = 15;
   private int        yOffset = 0;
   private CardList[] suits = null;
   
   //--------------------- constructor ----------------------------
   public Hand( Container parent, int x, int y )
   {
      xLoc = x;
      yLoc = y;
      suits = new CardList[ 4 ];
      for ( int s = 0; s < suits.length; s++ )
      {
         suits[ s ] = new CardList( parent, x, y );
      }
      
      setOffsets();
   }
   //---------------------- getSuitLists() ----------------------------
   /**
    * return the array of CardLists, one for each suit ordered as specified
    *    by the Suit enum: CLUBS, DIAMONDS, HEARTS, SPADES.
    * 
    * Hens, if you assigned the value returned from getSuitLists to the 
    * variable suit, the individual lists can be accessed with 
    *     suit[ Card.Suit.SPADES.ordinal() ], 
    *     suit[ Card.Suit.HEARTS.ordinal() ],  etc.
    * Or with a variable of type Card.Suit with the invocation of 
    *     ordinal():
    *       CardList[] suitLists = getSuitLists();
    *       for ( Card.Suit s: Card.Suit.values() )       
    *       {
    *          CardList list = suitLists[ s.ordinal() ];
    *       }
    *     
    * or with an int from 0 to 3.
    */
   public CardList[] getSuitLists()
   {
      return suits;
   }
   
   //---------------------- getSuitList( Suit ) --------------------
   /**
    * return the hands list for the suit
    */
   public CardList getSuitList( Card.Suit suit )
   {
      return suits[ suit.ordinal() ];
   }
   //-------------------- setOffsets() -----------------------------------
   /**
    * set the default offsets in x and y for displaying multiple cards in the
    * collection.
    */
   private void setOffsets()
   {
      for ( int s = 0; s < suits.length; s++ )
      {
         suits[ s ].setXOffset( xOffset );
         suits[ s ].setYOffset( yOffset );
      }
   }
   //--------------------- displayCards( int, boolean ) -------------------
   /**
    * redisplay the first "num" cards in the list; show face if 2nd param is
    *    true, else show back
    */ 
   public void displayCards( int num, boolean face )
   {
      if ( num < 0 )
         num = 52;
      int x = xLoc;
      int y = yLoc;
            
      for ( int s = 0; s < suits.length; s++ )
      {
         suits[ s ].setLocation( x, y );
         int size = suits[ s ].size();
         suits[ s ].displayCards( num, face );
         num -= size;
         x = x + xOffset * size;
         y = y + yOffset * size;
      }
   }
   
   //---------------------- get( int ) --------------------------------
   /**
    * cards in hand should be ordered by spades, hearts, diamonds, clubs.
    */
   public Card get( int n ) 
   {
      if ( n < 0 || n > size() - 1 )
         return null;
      
      for ( int s = 0; s < suits.length; s++ )
      {
         int size = suits[ s ].size();
         if ( n < size )
            return suits[ s ].get( n );
         n -= size;
      }
      return null;
   }
   //--------------------- setXOffset( int )--------------------------
   /**
    * set the offset in x for showing the edges of the cards in the stack
    */ 
   public void setXOffset( int m )
   {
      xOffset = m;  
      setOffsets();
   }    
   
   //---------------------setYOffset( int )-----------------------
   /**
    * set the offset in y for showing the edges of the cards in the stack
    */ 
   public void setYOffset( int m )
   {
      yOffset = m;  
      setOffsets();
   }
   
   //------------------- getXLocation() --------------------------
   /**
    * return the x location for the display of this card stack
    */   
   public int getXLocation()
   {
      return xLoc;
   }
   
   //------------------- getYLocation() --------------------------
   /**
    * return the y location for the display of this card stack
    */
   public int getYLocation()
   {
      return yLoc;
   }
   //------------------------ clear() -----------------------------
   public void clear()
   {
      for ( int s = 0; s < suits.length; s++ )
         suits[ s ].clear();
   }
   //------------------------ size() -----------------------------
   public int size()
   {
      int size = 0;
      for ( int s = 0; s < suits.length; s++ )
         size += suits[ s ].size();
      return size;
   }
   
   //-------------------- add( Card ) ------------------------------
   public void add( Card c )
   {
      CardList suit = getSuitList( c.getSuit() );
      suit.addByRank( c );
   }
   //-------------------- remove( Card ) ------------------------------
   public void remove( Card c )
   {
      CardList suit = getSuitList( c.getSuit() );
      suit.remove( c );
   }
   //----------------- find( Suit, Rank ) -------------------------
   /**
    * Look for a particular card in the hand. If it is there,
    * return a reference to it, else return null.
    */
   public Card find( Card.Suit suit, Card.Rank rank )
   {
      Card retCard = null;
      CardList cards = suits[ suit.ordinal() ];
      for ( int c = 0; c < cards.size() && retCard == null; c++ )
      {
         Card thisCard = cards.get( c );
         if ( thisCard.getRank() == rank )
            retCard = thisCard;
      }
      return retCard;
   }
}
