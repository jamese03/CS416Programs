/** 
 * Node<T>:  a generic Node for a 2-way LinkedList class
 * 
 * Made it a public class so we can actually use it
 * Sometimes its awkward being an inner class, even when it is public.
 * 
 * @author rdb
 * October 2010
 */
public class Node<T extends Comparable<T> >
{
    public Node<T> next = null;
    public Node<T> prev = null;
    public T data = null;
    
    public Node( T d ) 
    {
        data = d;
    }
    public String toString()
    {
       return data.toString();
    }
}