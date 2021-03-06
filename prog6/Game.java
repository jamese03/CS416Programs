/**
 * Game.java - implementation of a card game
 * 
 * @author rdb
 * March 2009
 * mlb 10/09: new cards , new shuffle, new baseDeck
 */

import javax.swing.*;
import java.util.*;

public class Game
{
    //----------------------- class variables ---------------------------
    public  static int       seed = 0;
    //----------------------- instance variables ------------------------

    private JPanel          _parent;
    private ArrayList<Card> _baseDeck = null;
    private CardStack       _deck = null;
    private Random          _rng = null;     // DO NOT USE THIS VARIABLE
    
    private Hand[]          _hands;
    private CardStack[]     _tricks;
    private Card[]      _playedCards;
    private Boolean         gameStart = true;
    private int cardCount = 0;
    private int playerCount = 0;
    private int[] _pScore = null;
    private int firstPlayer = 0;
    //------------------  constants --------------------------------
    //----- positioning variables   
    private int xOffset  = 15;        // "standard" card offset
    private int handDY   = 100;       // offset of hands rows
    private int handX    = 150;       // x-loc for all hands
    private int topHandY = 200;       // top hands row
    private int tricksX  = 40;        // x-loc for all tricks stacks
    private int deckX    = 40;
    private int deckY    = 20;
    private String label[] = { "North", "East", "South", "West" };
    private Card.Suit _curSuit;
    private Card.Suit _nextSuit;
    //---------------------- constructor --------------------------------
    public Game( JPanel parent )
    {         
        _parent = parent; 
        _pScore = new int[4];
        createBaseDeck();   // build the base deck
        _deck = new CardStack( _parent, deckX, deckY );
        _deck.setXOffset( xOffset );
        
        _hands  = new Hand[ 4 ];
        _tricks = new CardStack[ 4 ];
        _playedCards = new Card[ 4 ];
        for ( int i = 0, y = topHandY; i < _hands.length; i++, y += handDY )
        {
           _hands[ i ]  = new Hand( _parent, handX, y );
           _hands[ i ].setXOffset( xOffset );
      
           _tricks[ i ] = new CardStack( _parent, tricksX, y );
           _tricks[ i ].makeBaseIcon( label[ i ] );
                  
        }
        
        _rng = new Random( seed );
        makeDeck(); // make deck from which to deal 
    }
        
    //------------------------- play() ----------------------------
    /**
     * One player must choose a card to lead, others must follow
     * suit if possible. Highest card of the suit led wins and
     * starts the next play. Player 0 is first player.
     * 
     * The String msg returned to GUIPanel is a mechanism for telling
     * the GUIPanel when the game is over (or if a serious error 
     * occurred). This method should normall return null (not a zero-length
     * string). Any non-null return value is interpreted as the end of 
     * the game.
     */
    public String play()
    {
      String msg = null;
      ///////////////////////////////////////////////////////////
      // Implement the method. Be sure to use lots of methods!!!
      //    You might think about whether any classes might help
      //////////////////////////////////////////////////////////
      Card c = null;    
      Card temp =  _playedCards[0];
      Hand winner = _hands[0];
    

      
      if (gameStart)
      {
        c = getLow(_hands[0]);    
        gameStart = false; 
        _hands[0].remove(c);
        _curSuit = c.getSuit();
        c.setFaceUp(true);
      }
    
      else if ( !gameStart && cardCount == 0)
      {
        c = getLow(_hands[playerCount]);
        _hands[playerCount].remove(c);
        _curSuit = c.getSuit();
        c.setFaceUp(true);
      }
       
      else if (cardCount > 0 && cardCount < 4) {
        
        if ( playerCount == 3)
        {
           
           c = getMidSuit(_hands[playerCount], _curSuit);
          
            if ( c !=null)
            {
          if ( c.getRank().ordinal() >  _playedCards[findWinner()].getRank().ordinal())
          {
            c = getHighSuit(_hands[playerCount], _curSuit);
   
          }
         }
        }
        
        
       else { 
        c = getMidSuit(_hands[playerCount], _curSuit);
        }
         
          
          if ( c == null)
          {    
            if ( _hands[playerCount].find(Card.Suit.SPADES, Card.Rank.QUEEN) !=null)
            {
              c = _hands[playerCount].find(Card.Suit.SPADES, Card.Rank.QUEEN);
            }
            else if (getHighSuit(_hands[playerCount], Card.Suit.HEARTS) !=null)
            {
              c = getHighSuit(_hands[playerCount], Card.Suit.HEARTS);
            }
            else {
              c = getHigh(_hands[playerCount]); 
            }        
          }     
          for ( int i = 0; i < _hands.length; i++)
          {
            _hands[i].remove(c);
          }
          c.setFaceUp(true);       
        }
      
      else  if (cardCount != 0)
      {
      playerCount = findWinner();
      firstPlayer = playerCount;
        
        JOptionPane.showMessageDialog(null, "Player1 " + _playedCards[0] +
                                      "\nPlayer2 " + _playedCards[1] +
                                      "\nPlayer3 " + _playedCards[2] +
                                      "\nPlayer4 " + _playedCards[3] + 
                                      "\nWinner " + _playedCards[findWinner()]);

        for ( int j = 0; j < _playedCards.length; j++)
        {
        
          _tricks[playerCount].add(_playedCards[j]);
          _tricks[playerCount].displayCards(_tricks[playerCount].size(), false);      
          _playedCards[j] = null;
        }
        
      if ( _hands[playerCount].size() == 0)
       {
        return gameOver();
       }
        
        _curSuit = temp.getSuit();
        cardCount = 0;
        return msg;
      }
      
      _playedCards[playerCount] = c; 
     
      cardCount++;  
      showPlayed();  
      playerCount++;
      
      if ( playerCount >3)
        playerCount = 0;
      
      
      // return "Don't Loop";  // In order to prevent infinite loop when
      // "Play to end" is clicked, the starter
      // code returns a non-null message.
      // as soon as you start working on play(),
      // you need to return null.
      return msg;  // return null if game is NOT over
      
    }

    //------------------------- gameOver ---------------------------
    /**
     * The game is over, compute score and build a message
     */
    private String gameOver()
    {
     
        
      addScore();
      String s = "Game results: \n" + "\nPlayer 1 Score : " + _pScore[0] +
        "\nPlayer 2 Score : " + _pScore[1] + "\nPlayer 3 Score : " + _pScore[2] + 
        "\nPlayer 3 Score : " + _pScore[3];
      
      ///////////////////////////////////////////////////////////////
      // Expand this code
      ///////////////////////////////////////////////////////////////
      return s;
    }
//----------------find who won the game ----------------------------------  
    public int findWinner()
    {
      Card temp =  _playedCards[firstPlayer];
      Hand winner = _hands[firstPlayer];
      int win = firstPlayer;
      
      for ( int i = 0; i < _playedCards.length; i++)
      {
        if ( _playedCards[i] !=null && _playedCards[i].getSuit() == Card.Suit.HEARTS)
        {
          if (temp.compareTo(_playedCards[i]) < 0 )
          {
            temp = _playedCards[i];        
            winner = _hands[i];     // winner if they played hearts     
            win = i;           
          }  
        } 
        
        else if ( _playedCards[i] !=null && _playedCards[i].getSuit() == _curSuit)
        {
          if (temp.compareTo(_playedCards[i]) < 0 )
          {
            temp = _playedCards[i];        
            winner = _hands[i];        // winner if they played the current suit
            win = i;           
          }  
        } 
      } 
      return win;
      
    }
    
    //------------------------ newGame() -----------------------------
    /**
     * Shuffle the cards and deal them to the hands
     */
    public void newGame()
    {
      ////////////////////////////////////////////////////////////
      // Implement this method
      //
      // The existing methods makeDeck, createBaseDeck, and shuffle below
      //    will be useful.
      /////////////////////////////////////////////////////////////
      for ( int i = 0; i < _hands.length; i++)
        _hands[i].clear();
      
      makeDeck();             // reset my variables for the new game 
      shuffle();   
      dealCard(); 
      _parent.repaint();
      playerCount = 0;
      cardCount = 0;
      firstPlayer = 0;
      
      _pScore = new int [4];
      
     for ( int i = 0, y = topHandY; i < _hands.length; i++, y += handDY )
        {
 
           _tricks[ i ] = new CardStack( _parent, tricksX, y );
           _tricks[ i ].makeBaseIcon( label[ i ] );
           // reset tricks for new game 
           
        }
     _playedCards = new Card[ 4 ];
     
    }
    

    
//---------------------get lowest lowest card---------------------------
    public Card getLow( Hand hand )
    {
    
     Card tempCard = hand.get(0); 
      for ( int i = 1; i < hand.size(); i++)
      {
        // get lowest card of any suit as long as its not hearts 
         if ( (hand.get(i).compareTo(tempCard) < 0) && hand.get(i).getSuit() != Card.Suit.HEARTS)  
        tempCard = hand.get(i);
      
      }
    return tempCard;
    }
//--------------------get lowest card with suit------------------------   
    public Card getLowSuit( Hand hand, Card.Suit suit )
    {
      // gets lowest card of a particular suit 
      Card tempCard = hand.get(0); 
      CardList suitList = hand.getSuitList(suit);
      
      if ( suitList.head() != null)
      {
        return suitList.head();
      }
      else { 
        return null;
      }    
    }
     
//-------------------get highest card----------------------------
    public Card getHigh( Hand hand)
    {
      // gets highest possible 
      Card tempCard = hand.get(0); 
      for ( int i = 1; i < hand.size(); i++)
      {
        if ( hand.get(i).compareTo(tempCard) > 0 ) // case to find the lower card 
          tempCard = hand.get(i);
        
      }
      return tempCard;
    }
    
//---------------get high suit------------------------------
        public Card getHighSuit( Hand hand, Card.Suit suit )
    {
     
    
     CardList suitList = hand.getSuitList(suit);
     
     if ( suitList.tail() != null)
     {
       return suitList.tail();
     }
     else { 
       return null;
     }
       
    }
//--------------------------get the highest card that wont win------------------------------------     
        public Card getMidSuit( Hand hand, Card.Suit suit)
        {
          
          CardList suitList = hand.getSuitList(suit);
          Card temp = suitList.head();
          Card highCard = _playedCards[findWinner()];
          Card save = suitList.head();
  
       for ( int i = 0; i < suitList.size(); i++)
            {
             temp = suitList.get(i);
             // the compare to method just kept getting confusing so I justed used ordinal
             
            if ( (temp.getRank().ordinal() > save.getRank().ordinal()) && 
                temp.getRank().ordinal() < highCard.getRank().ordinal())
            {
              save = temp;
              
            }       

          }

          return save;   
        }
//-------------------addScore method to add score to a player---------------------
        public void addScore ()
        {
          for ( int i = 0; i < 4; i++)
          {
            for ( int j = 0; j < _tricks[i].size(); j++)
            {
              if ( _tricks[i].get(j).getSuit() == Card.Suit.HEARTS)
                _pScore[i] +=1;
              
              if ( (_tricks[i].get(j).getSuit() == Card.Suit.SPADES) && 
                  (_tricks[i].get(j).getRank() == Card.Rank.QUEEN))
              {
                _pScore[i] += 13; // queen of spades 
              }                 
            }
          }
        }
        
    
//--------------------------dealCard method to deal the players the card---------------------
    public void dealCard()
    {
      for ( int i = 0; i < _deck.size(); i++) // loops through the head 
      {
          _hands[i%4].add(_deck.get(i)); // mod four for each hand 
          _hands[i%4].displayCards(i, false); // displays them 
      }
    }

    //------------------------show play hands------------------------------
    public void showPlayed()
    {
      
      for ( int i = 0; i < _playedCards.length; i++ )
      {
        if(_playedCards[i] !=null)
        {
          _playedCards[i].setFaceUp(true);
          _playedCards[i].setLocation( 400, (handDY*i) + 200);          
        }      
      }
       _parent.repaint();
      
      
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    ////////////////////////////////////////////////////////////////////
    //
    // You probably don't need to change any code below here.
    //
    ///////////////////////////////////////////////////////////////////
    //------------------ showHands() ----------------------------------
    /**
     * all cards in all hands should display their face side, rather than back.
     * also show the deck -- in case it's not empty
     */
    public void showHands()
    {
       for ( int i = 0; i < _hands.length; i++ )
         for(int k = 0; k < _hands[i].size(); k++) 
         _hands[ i ].get(k).setFaceUp( true );
       _parent.repaint();
    }

    
    //------------------ hideHands() ----------------------------------
    /**
     * all cards in the deck should display their back side.
     */
    public void hideHands()
    {
            for ( int i = 0; i < _hands.length; i++ )
         for(int k = 0; k < _hands[i].size(); k++) 
         _hands[ i ].get(k).setFaceUp( false );
       _parent.repaint();
    }
    //------------------ showTricks() ----------------------------------
    /**
     * all cards in the deck should display their face side, rather than back.
     */
    public void showTricks()
    {
        for ( int i = 0; i < _hands.length; i++ )
        {
           _tricks[ i ].setXOffset( xOffset );
           _tricks[ i ].displayCards( -1, true );
        }
        _parent.repaint();
    }
    //------------------ hideTricks() ----------------------------------
    /**
     * all cards in the deck should display their back side.
     */
    public void hideTricks()
    {
        for ( int i = 0; i < _hands.length; i++ )
        {
           _tricks[ i ].setXOffset( 0 );
           _tricks[ i ].displayCards( -1, false );
        }
        _parent.repaint();
    }
    //------------------------ shuffle( ) -----------------------------
    /**
     * shuffle the cards in _baseDeck. Shuffle by creating a loop
     * that generates 2 random integers from 0 to 51 and then swaps the two
     * cards at those positions in _baseDeck. Execute the loop 50 times.
     */
    public void shuffle()
    {
       //////////////////////////////////////////////////////////////
       // DO NOT MODIFY THIS CODE
       //
       // DO NOT USE THE _rng VARIABLE FOR ANYTHING ELSE
       //////////////////////////////////////////////////////////////
       for ( int i = 0; i < 50; i++ )
       {
          int p1 = _rng.nextInt( 52 );
          int p2 = _rng.nextInt( 52 );
          Card temp = _baseDeck.get( p1 );
          _baseDeck.set( p1, _baseDeck.get( p2 ) );
          _baseDeck.set( p2, temp );
       }
    }
    //------------------------ makeDeck() -------------------------
    /**
     * Make the deck of cards from _baseDeck
     */
    private void makeDeck()
    {
       if ( _deck != null )
          _deck.clear();
       _deck.displayCards( -1, false );
       
       for ( int c = 0; c < _baseDeck.size(); c++ )
          _deck.push( _baseDeck.get( c ));
       _deck.displayCards( 0, false );
    }
    //------------------------ createBaseDeck() -----------------------------
    /**
     * Create a "base" deck of cards. These will keep being re-used for
     * multiple game invocations.
     */
    private void createBaseDeck()
    {
       _baseDeck = new ArrayList<Card>();
       
       for ( Card.Suit suit: Card.Suit.values() )
       {
          for ( Card.Rank rank: Card.Rank.values() )
          {
             Card card = new Card( _parent,  rank, suit );
             _baseDeck.add( card );
             _parent.add( card );
          }
       }
    }
    
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //--------------------------- main -----------------------------------------
    public static void main( String[] args )
    {
        // Invoke main class's main
        new Hearts( "Hearts from Game" );
        
        /**** self test code below
          JFrame f = new JFrame();
          f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
          f.setLayout( null );
          
          Game g = new Game( f );
          g.showHands();
          f.setSize( 900, 500 );
          
          f.setVisible( true );
          /*******************************/
    }
}
