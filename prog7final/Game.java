/**
 * Game.java - skeleton  for the Golf solitaire game
 * 
 * @author rdb
 * March 2011
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Game extends JPanel
{
   //------------------------- class variables --------------------------------
   public static int     numPlayPiles = 7;     // real game uses 7
   public static int     cardsPerPile = 5;  // real game uses 5
   public static int     seed = 0;

   //----------------------- instance variables -------------------------------
   private ArrayList<Card> _baseDeck = null;
   private CardStack       _drawPile;
   private CardStack       _discardPile;
   private JLabel         label;
   private CardStack[]   cardColumn;
   private Card          curCard;
   private int          flag;
   public static int          cardCount;
   private  ArrayList<Node> children;
   private Node       root;
   private Node       cur;
   private Node       _lastMove;
   private static int totalChildren =0;
   private static int _nodeCounter = 0;
   private Boolean treeState = false;
   //--------------------- position/size variables ---------------------------   
    
   //++++++++++++++++++++++++++++ constructors ++++++++++++++++++++++++++++++
   
   //---------------------- Game() ------------------------------------
   /**
    * Starts the game in the specified JPanel
    */
   public Game()
   {
      _baseDeck = new ArrayList<Card>();
      createDeck();    
      ///////////////////////////////////////////////////////////
      // Add code to create and show a JLabel with count of cards left
      //   in drawPile
      //////////////////////////////////////////////////////////////
 
      label = new JLabel();
      label.setSize(180, 180);
      label.setLocation(0, 0);
      this.add(label);     
      initialize();
   }  
   //------------------------ initialize() --------------------------------
   /**
    * Intializes the piles at the start of the program
    * Create them and set the location
    */
   public void initialize()
   {  
     treeState = false;
      cardColumn = new CardStack[numPlayPiles];
     _discardPile = new CardStack(this);
     _drawPile = new CardStack(this);
    
     label.setText("Cards left in deck: " + _drawPile.size());
          
     for (int i = 0; i < cardColumn.length; i++) // initilize each column of card stacks
     {
       cardColumn[i] = new CardStack(this);     
       cardColumn[i].setLocation(100 *(i+1), 100);
       cardColumn[i].setYOffset(25);
     }
     _discardPile.setLocation(0,275);
     _discardPile.setXOffset(15);
     _drawPile.setLocation( 0, 100);
     makeNewDeck();
     showDrawPile(false); // keep draw pile from showing. 
     
   }
   //++++++++++++++++++++++++++++ public methods ++++++++++++++++++++++++++++
   //----------------- showDrawPile( boolean ) ------------------------
   /**
    * toggle display of top card of Draw Pile
    */
   public void showDrawPile( boolean onOff )
   {
     if (onOff)
     {
       for(int i =0; i < _drawPile.size(); i++)
       {
         _drawPile.get(i).setFaceUp(true);
       }
     }
     else {
       for(int i =0; i < _drawPile.size(); i++)
       {
         _drawPile.get(i).setFaceUp(false);
       }
     }
   }
//-----------card val to int method--------------------------------------------
   public int cardToInt( Card c)
   { 
     int val = c.getRank().ordinal(); // easier than typing Card.getRank().ordinal() every time
     return val;
     
   }
//------------------- deals first card of stack-------------
   public void dealCard() 
   {      // deal first card onto the pile
     curCard = _drawPile.pop();
     _discardPile.push(curCard);
     _discardPile.displayCards( -1, true);     // displays the card    
   }
   

   
   //-------------------- pauseAtLeaf( boolean ) -------------------------
   /**
    * toggle display of top card of Draw Pile
    */
   public void pauseAtLeaf( boolean onOff )
   {
     if (onOff)
     {
       _drawPile.get(0).setFaceUp(true);
     }
     else {
      _drawPile.get(0).setFaceUp(false); 
     }

   
   }
   //------------- playCard() -------------------------------------------
   /**
    * Does a play.
    * If no tree is built, it does the first legal move it finds
    * If a tree is built, it does the best move from the given state
    * Returns a string with an error if no moves possible; else
    *  it returns null.
    */
   public String playCard()
   {
     if(_drawPile.isEmpty() && allMoves().size() == 0) // if the draw pile is empty, and flag is not 0 the game still has cards
     {      
       return "YOU LOSE with score of: " + getScore();// on the board so its a losing case.
     }
     
     if ( flag == 0 && _drawPile.size() == 0 && cardCount == 0) // if the flag is zero at this point you win the game
     {      
       return "You WIN";   
     }
     if (treeState) // case where a tree has been made and you must play by those rules
     {      
       Node tempNode = cur.kids.get(0);
       
       for ( int i = 0; i < cur.kids.size(); i++)
       {
         int temp = 100;        
         if ( cur.kids.get(i).score < temp)
         {
           temp = cur.kids.get(i).score;
           tempNode = cur.kids.get(i);              
         } 
       }
       tempNode.move();
       
       cur = tempNode;
       
       update();  
     }
     
     else {
       flag = 0; // checks to see if game is over 
       curCard = _discardPile.top();
       System.out.println(allMoves().size());
       for ( int i = 0; i < cardColumn.length; i++)
       { 
         if ( cardColumn[i].size() != 0)
         {  
           flag =1; // its a seperate value to keep playing
           int last = cardColumn[i].size()-1;
           int val = cardToInt(cardColumn[i].get(last));
           if ( Math.abs(val - cardToInt(curCard)) == 1) // checks if its a valid card
           {     
             _discardPile.push(cardColumn[i].get(last));
             cardColumn[i].pop();       
             update();
             cardCount --;          
             return null; 
           }       
         }
       }
     }
     
     
     
     if ( !treeState) 
       _discardPile.push(_drawPile.pop()); 
     // pushes draw piles cards into discard if play cant be made from the board and tree hasnt been made  
     update();
     return null;
     
   }
   
   //------------------------ makeNewDeck() -----------------------------------
   /**
    * Makes a new deck, ie a new game.
    * Resets all the piles
    * Does not shuffle
    */
   public void makeNewDeck()
   {
     for (int i = 0; i < cardColumn.length; i++) // initilize each column of card stacks
     {
       cardColumn[i].clear();
     }  
     _drawPile.clear();
     _discardPile.clear();
     for ( int i = 0; i < _baseDeck.size(); i++)
     {
       _drawPile.push(_baseDeck.get(i));
     }     
     for ( int j = 0; j < numPlayPiles*cardsPerPile; j++)
     {
       cardColumn[j%numPlayPiles].push(_drawPile.pop()); // sets up each colum for their cards      
     }
     for( int i =0; i < numPlayPiles; i++)
     {
       cardColumn[i].displayCards(-1, true); 
     }
     cardCount =  numPlayPiles*cardsPerPile;
     
     dealCard();
     update();
   }
   
   //------------------------ shuffle( ) --------------------------------------
   /**
    * Shuffles the current base deck according to the seed in the game
    */
   public void shuffle() 
   {
      Collections.shuffle( _baseDeck, new Random( seed ) );
      makeNewDeck();
      treeState = false;
      update();    
   }
   //-------------- undo() --------------------------------------------
   /**
    * Undo a move
    * Only works if the tree is built
    * Returns a string if a problem occurs
    * "Undoes" the move be setting the game state
    */
   public String undo()
   {
     System.out.println("GOT HERE");
     undo(cur);
     update();
     return null;
   }
   /**************************************************************************/       
   //++++++++++++++++++++++++++  private methods ++++++++++++++++++++++++++++++
   //----------------------------- update() -----------------------------------
   /**
    * Update the display components as needed.
    */
   private void update()
   {      
      /////////////////////////////////////////////////////////
      // This method needs to invoke the "displayCards" method of
      // CardStack for all the card piles. The parameters to 
      // "displayCards" may be different for different piles.
      ////////////////////////////////////////////////////////
      // W A R N I N G
      //    Surround whatever code you put here in a big "if" !!!!
      //    where you do NOT do any of this while you are tree 
      //    building.
      ///////////////////////////////////////////////////////
     
     _drawPile.displayCards(-1, false);
     _discardPile.displayCards(-1, true);
     label.setText("Cards left in deck: " + _drawPile.size());
     
   }
   //------------------------ makeTree() ------------------------------------
   /**
    * Make the tree by simulating recursion in the TreeNode class
    */
   public void makeTree()
   {
     // calls the makeTree below
     totalChildren = 0;
     root = new Node();
     makeTree(root, -1);
     System.out.println("Tree size is: " + totalChildren);
   }
//-------------------------------makeMoves--------------------------------
   public Node makeTree( Node parent, int depth)
   {
     /*Members of the PAC were the only reason I could have completed this method
      * This took me three days to correctly write, I would have nothing without them.
      * If this looks similar to anybody else's then they were probably in the same situation as I was.
      * */
     treeState = true;
     parent.kids = allMoves();
  
     for( int i =0; i < allMoves().size(); i++)
     {
       parent.kids.get(i).parent = parent;
     }
    
     parent.depth = depth;
 
     if (parent.kids.size() == 0)
     {
     parent.kids = null;
     parent.score = getScore();
     return parent;
     }
     
     for ( int i =0; i < parent.kids.size(); i++)
     {
       Node temp = parent.kids.get(i);
       move(temp);   
       makeTree(temp, depth +1);
       undo(temp);      
     }
     
     
     totalChildren += parent.kids.size();
     setBestChild(parent);
     cur = root;
     return parent;
   }
//----------------move method----------------------------------
   public void move(Node n)
   {
     if ( n.one !=null && n.two != null && n.one.size() !=0)
     {
       n.two.push(n.one.pop());
       n.score--;
     }
   }
//--------------------undo for the node------------------------- 
   public void undo(Node n )
   {
     //I couldn't get this implementation to work correctly with the game.
     if ( n !=null && n.one !=null && n.two != null && n.two.size() !=0)
     {
       n.one.push(n.two.pop());
       n.score++;
     }
     
   }
//-------------------------gets the score-------------------   
  public int getScore()
  {
    //gets the score of the game currently.
    int theScore = 0;
    for ( int i =0; i < cardColumn.length; i++)
    {
      CardStack s = cardColumn[i];
      theScore += s.size();
    }
    return theScore; 
  }
//-----------------------------setBestChild-------------------------
   public void setBestChild(Node n)
   {
     // finds the best child for a given node
     // best means the best possible game
     int topScore = 150;    
         
     for( int i =0; i < n.kids.size(); i++)
     {
       if (n.kids.get(i).score < topScore)
       {  
       topScore = n.kids.get(i).score;
       n.bestKid = i;
       n.score = n.kids.get(i).score;
       }
     }        
   }
//-----------------------------all possible moves--------------------
   public ArrayList<Node> allMoves()
   {
     /*This method gets all the at a certian point in the game.
      * */
     ArrayList<Node> moves = new ArrayList<Node>();
     
     Card discardTop = _discardPile.top(); 
     for ( int j =0; j < cardColumn.length; j++)
     { 
       if ( cardColumn[j].size() != 0)
       { 
         int lastCardVal = cardToInt(cardColumn[j].top());               
         int val = cardToInt(discardTop);     
         if (Math.abs(val - lastCardVal) == 1) // check if card is a valid play
         {
           Node n = new Node();
           n.one = cardColumn[j];
           n.two = _discardPile;
           moves.add(n);       
         }
       }
     }
       
     int i;
     for (i =0; i < cardColumn.length; i++)
     {
       if ( cardColumn[i].size() != 0)  
         break;
     }
     //case where no moves can be played from the bord so go to draw pile. 
      if ( _drawPile.size() !=0 && moves.size() == 0 && (i!=cardColumn.length))
     {
       Node n = new Node();
       n.one = _drawPile;
       n.two = _discardPile;
       moves.add(n);
     }

     
     return moves;
   }
   
   //------------------------ toInt( String, int ) ----------------
   /**
    * Convert the string to an int if possible; if fail, print error ot
    * Standard error and return the default value.
    */
   private static int toInt( String in, int defaultVal )
   {
      try 
      {
         return new Integer( in.trim() ).intValue();
      }
      catch ( Exception ex )
      {
         System.err.println( "toInt conversion error: " + ex.getMessage() );
      }
      return defaultVal;
   }
   //------------------------ createDeck() ------------------------------------
   /**
    * Creates the first basedeck at game start.
    */
   private void createDeck()
   {
      int  cardIndex = 0;
      
      for ( Card.Suit suit: Card.Suit.values() )
      {
         for ( Card.Rank rank: Card.Rank.values() )
         {
            Card card = new Card(this,  rank, suit );
            _baseDeck.add( 0, card );
            this.add( card );
         }
      }
   }
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   //--------------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      // Invoke main class's main
      Golf.main( args );
   }
}
