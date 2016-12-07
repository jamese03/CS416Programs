/**
 * LinkedList - a generic linked list class with a public external Node class
 *              Objects stored in this list must implement the Comparable
 *              interface.
 * 
 *              Since the Node's are accessible to any one, there is little
 *              protection to insure that lists don't get corrupted. 
 * 
 * This is a skeleton framework for this class, including a main
 * program that tests it for LinkedList<String>
 * 
 * 
 * @author
 * rdb 10/03/10
 */
public class LinkedList<T extends Comparable<T>> 
{
   //---------------------- instance variables --------------------------
   private  Node<T> head;
   private  Node<T> tail;
   
   public int size;
   
   //----------------------- constructor --------------------------------
   public LinkedList() 
   {
      ////////////////////////////////////////////////////
      // Add any needed code
      ////////////////////////////////////////////////////
     head = null;
     tail = null;
     size = 0;
   }
  
   //-------------------------- head() ------------------------------------
   /**
    * return the head of the list
    */
   public Node<T> head() 
   {
      return head;
   }
   //-------------------------- tail() ------------------------------------
   /**
    * return the tail of the list
    */
   public Node<T> tail() 
   {
      return tail;
   }
   //---------------------- size() ------------------------------------
   /**
    * return the number of entries in the list
    */
   public int size() 
   {
      return size;
   }
    //--------------------- add( T ) ---------------------
   /**
    * add to tail; 
    */
   public Node<T> add( T t ) 
   {
      return addTail( t );     
   }
   
   //----------------------- addTail( T ) ---------------------------------
   /**
    * Create a node for the passed-in object and add the node to the 
    * end of the list. 
    * Return the added node; update the size.
    */
   public Node<T> addTail( T t ) 
   {
     Node<T> newNode = new Node<T>( t );
      size++; // increments size of node
    
     ///////////////////////////////////////////////////////
     // Complete the method
     ///////////////////////////////////////////////////////
     if (head == null)
     {
       head = tail = newNode; // head and tail = newNode
       return newNode;
       
     }

     newNode.prev = tail; // setting tail 
     tail.next = newNode; 
     newNode.next = null; // new node next equals null 
     tail = newNode;  // tail is the last node
  
     return newNode;
    
   }
   
   //--------------------- addBefore( Node, T ) ---------------------
   /**
    * Given a Node on the list and a new object, create a node for the
    * new object and add it in front of the existing node.
    */
   public Node<T> addBefore( Node<T> n, T t ) 
   {
    
     
     Node<T> newNode = new Node<T>( t );
     
     ///////////////////////////////////////////////////////
     // Complete the method
     ///////////////////////////////////////////////////////
     
     
     if ( n == null)
     {
       return null;
     }
     if ( head == null)
     {
       addTail(t); // if null uses add tail method because list is only 1 node
       return newNode;
     }
     
     size++; // increments size
     
     if ( n == head ) // special case for adding before the head
     {
       head.prev = newNode;
       newNode.next = head;
       head = newNode;
       n.prev = newNode;
       
       return newNode;     
     }      
     
     Node<T> save = n.prev; // saving the value of the previous value

          
     // normal case if we arent adding before the head 
       newNode.next = n;
       newNode.prev = save;
       save.next = newNode;
       n.prev = newNode;
       return newNode;
        
   }
   
   //--------------------- addInOrder( T ) ---------------------
   /**
    * Create a Node for the argument, and insert that Node into the list
    * such that the list is ordered according the compareTo method of T.
    * The order should be from low to high.
    * Return the Node being added
    */
   public Node<T> addInOrder( T t ) 
   {
     Node<T> newNode = null;
     Node<T> save = head;
      ///////////////////////////////////////////////////////
      // Complete the method
      ///////////////////////////////////////////////////////
      while (save !=null)
      {
        if ( t.compareTo(save.data) < 0) 
        {
          newNode = addBefore(save, t);
          return newNode;
        }
        save = save.next; // pushes to next step to go through while loop     
      }
      newNode = addTail(t);
 
      return newNode;
   }
    //--------------------- addAfter( Node, T ) ---------------------
   /**
    * Given a Node on the list and a new object, create a node for the
    * new object and add it in after the existing node.
    */
   public Node<T> addAfter( Node<T> n, T t ) 
   {
      Node<T> newNode = new Node<T>( t );
      ///////////////////////////////////////////////////////
      // Complete the method
      ///////////////////////////////////////////////////////
      Node<T> temp = new Node<T>(t); // temp node 
      size++;
      
      if ( n !=null) // n exists temp is the next of n;
      {
        temp = n.next;
      }
      if ( n == null) // if the node given is null the temp must also be null
        temp = null;
      
      if ( temp == null) // the case where n is the tail or empty
      {
        tail = newNode;
        newNode.prev = n;
        newNode.next = null;
        n.next = newNode;
        return newNode;
      }
      else { // normal case 
       newNode.prev = n;
       newNode.next = temp;
       temp.prev = newNode;
       n.next = newNode;     
      }
      
      return newNode;
   }
   
   //----------------------remove( T ) ----------------------------
   /**
    * Find the node on the list that contains an object that compares
    * as equal to the parameter passed in. 
    * If such a node exists, remove it from the list, update list size
    * and return its object
    */
   public T remove( T d ) 
   {

     Node<T> temp = findNode(d); // uses find node method to return the node we are looking for
     
     if (temp == null)
       return null; // if we want to remove a null object return null
 
     
     if ( head.data.equals( d ) )
     {
       if ( head == tail ) // one size node ie head and tail are same object
       {
         head = tail = null;
         size--;
         return d;
       }
       
       head = head.next; // sets the head to its next node 
       head.prev = null; // the previous of head is removed 
       size--;
       return d;
     }
     
     if ( temp == tail) // if we need to remove the tail 
     {
       temp.prev.next = tail; 
       tail = temp.prev;
       tail.next =  null;
       size--;
       return d;    
     }
     /*Logic that removes one node, sets its previous to the temps next
      * sets the previous to the correct value
      * and sets the temp to null and decreases the size by one
      * */
      temp.prev.next = temp.next; 
      temp.next.prev = temp.prev;
      temp = null;
      size--;
 
     return d;
   }
   //----------------------- findNode( T ) --------------------------
   /**
    * Find the node on the list that contains an object that compares
    * as equal to the parameter passed in. Return the node or null.
    */   
   public Node<T> findNode( T d ) 
   {
      Node<T> cur = head;
      ///////////////////////////////////////////////////////
      // Complete the method
      ///////////////////////////////////////////////////////
      while ( cur !=null) // looping through 
      {
        if ( cur.data.equals(d)) // the case where we have found the node we are looking for
        {
          return cur;
        }
        cur = cur.next; // allows the loop to work until the end
      
      }
      return cur;
   }
  
   //---------------------- get( int ) --------------------------------
   /**
    * get the n-th Node in the list. If n-th element doesn't exist, return null
    */
   public Node<T> get( int n ) 
   {
     if ( n >= size )
       return null;
     Node<T> temp = head;
     int count = 0;
     ///////////////////////////////////////////////////////
     // Complete the method
     ///////////////////////////////////////////////////////
     
     /*Simliar logic to the method above for the loop but used counter as well
      * */
     
     while ( temp !=null) 
     {
       if ( n == count)
       {
         return temp;
       }
       temp = temp.next;
       count++;
     }
     
     return temp;
   }
  
   //---------------------- clear() ----------------------------------
   /**
    * remove all elements from the List.
    */
   public void clear()
   {
      head = tail = null;
      size = 0;
   }
   
   //------------------------ toString() ----------------------------  
   /**
    * return a String representation of the list; while we're at it it,
    * count the number of elements and verify that that is the same 
    * number that is reported to be the size of the list. Report an
    * error if there is something wrong with the list.
    */
   public String toString() 
   {
      StringBuffer sb = new StringBuffer();
      int checkSize = 0;
      if ( head == null ) 
         sb.append( "Empty List" );
      else
      {
         Node<T> cur = head.next;
         
         sb.append( head + "->" );
         checkSize++;
         
         while ( cur != null ) 
         {
            sb.append( cur + "->" );
            cur = cur.next;
            checkSize++;
         }
      } 
      
      if ( checkSize != size )
         System.err.println( "List error: #nodes != size: " 
                               + checkSize + " != " + size );
      
      return sb.toString();
   }
   //------------------------ checkList() ----------------------------  
   /**
    * Traverse the list to check the size value and the prev references:
    * At each step save the previous nodes reference then compare that
    * to the "prev" reference in the next node.
    */
   public void checkList() 
   {
      int index = 0;
      int checkSize = 0;
      Node<T> cur = head;
      Node<T> back = null;
 
      while ( cur != null ) 
      {
         if ( cur.prev != back )
            System.err.println( "List error: bad prev @ index: " + index );
         back = cur;
         cur = cur.next;
         checkSize++;
         index++;
      }
      if ( checkSize != size )
         System.err.println( "List error: #nodes != size: " 
                               + checkSize + " != " + size );
      
   }
   //+++++++++++++++++++++++++++ main Unit tester ++++++++++++++++++++++
   public static void main( String[] args )
   {
      LinkedList<String> list = new LinkedList<String>();
      Node<String> node;
      String[] names = { "a", "c", "e", "g", "i", "k", "m" };
      for ( int i = 0; i < names.length; i++ )
      {
         System.out.println( names[ i ] );
         list.addTail( names[ i ] );
      }
      System.out.println( "List: " + list );
      list.checkList();
      
      // test get
      node = list.get( 3 );
      System.out.println( "------ get( 3 ) ------------------" );
      System.out.println( "Size: 7 =? " + list.size() );
      System.out.println( "item 3 should be g: " + node );
      list.checkList();
      
      // test addBefore in middle
      System.out.println( "------ add f before g ------------------" );
      list.addBefore( node, "f" );
      System.out.println( "Size: 8 =? " + list.size() );
      System.out.println( "a c e f g i k m  : " + list );
      list.checkList();
      
      // test addAfter in middle
      System.out.println( "------ add h after g ------------------" );
      list.addAfter( node, "h" );
      System.out.println( "Size: 9 =? " + list.size() );
      System.out.println( "a c e f g h i k m  : " + list );
      list.checkList();
      
      // test addBefore head
      System.out.println( "------ add 1 before a ------------------" );
      node = list.head;
      list.addBefore( node, "1" );
      System.out.println( "Size: 10 =? " + list.size() );
      System.out.println( "1 a c e f g h i k m  : " + list );
      list.checkList();
      
      // test addAfter tail
      System.out.println( "------ add n after m ------------------" );
      node = list.tail;
      list.addAfter( node, "n" );
      System.out.println( "Size: 11 =? " + list.size() );
      System.out.println( "1 a c e f g h i k m n  : " + list );
      list.checkList();
      
      // test findNode
      System.out.println( "-------- search for 'k' ------- " );
      node = list.findNode( "k" );
      if ( node != null )
         System.out.println( "Found: " + node );
      
      System.out.println( "-------- search for 'z' ------- " );
      node = list.findNode( "z" );
      if ( node == null )
         System.out.println( "z isn't in list! correct!" );
      
      System.out.println( "--------- remove( f )  middle -----------" );
      String s =  list.remove( "f" );      
      System.out.println( "Size: 10  =? " + list.size() );
      System.out.println( "1 a c e g i h k m n  :" + list );
      list.checkList();
      
      System.out.println( "------------ clear() --------------" );
      list.clear();
      System.out.println( "Size: 0  =? " + list.size() );
      System.out.println( " " + list );
      
      System.out.println( "------------ addInOrder():  q b z r c a" );
      list.addInOrder( "q" );
      list.addInOrder( "b" );
      list.addInOrder( "z" );
      list.addInOrder( "r" );
      list.addInOrder( "c" );
      list.addInOrder( "a" );
      System.out.println( "Size: 6  =? " + list.size() );
      System.out.println( "a b c q r z  : " + list );
      list.checkList();
      
      System.out.println( "------------ remove all one at a time ------" );
      while ( list.head() != null )
      {
         node = list.get( 0 );
         list.remove( node.data );
         System.out.println( "List: " + list );
         list.checkList();
      }
      
   }
}
