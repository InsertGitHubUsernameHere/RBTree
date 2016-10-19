/*
 * This file is for Assignment 2 in UNCG's CSC 330 class in Fall 2016.
 */

package assign2;

// BinarySearchTree class
//
// This is a slightly modified version of the BinarySearchTree implementation from
// the Weiss "Data Structures and Problem Solving Using Java" textbook, created
// for the UNCG CSC 330 class, Fall 2016
//
// It differs in that this class is designed to be extended by other BST
// implementations that include balancing/rebalancing operations. The changes
// needed here include introducing some generic functions (like getRoot()) that
// can be overridden in subclasses that differ in implementation details.
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// void removeMin( )      --> Remove minimum item
// Comparable find( x )   --> Return item that matches x
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// ******************ERRORS********************************
// Exceptions are thrown by insert, remove, and removeMin if warranted

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the tree.
     */
    public BinarySearchTree( )
    {
        root = null;
    }

    /**
     * Insert into the tree - old disabled version (recursive).
     * @param x the item to insert.
     * @throws DuplicateItemException if x is already present.
     */
    public void insert_disabled( AnyType x )
    {
        root = insert( x, root );
    }

    /**
     * Insert into the tree (rewritten as non-recursive...)
     * @param x the item to insert.
     * @throws DuplicateItemException if x is already present.
     */
    public void insert( AnyType x )
    {
        if (isEmpty()) {
            root = new BinaryNode<>(x);
        } else {
            BinaryNode<AnyType> parent = null;
            BinaryNode<AnyType> curr = root;
            
            while( curr != null ) {
                parent = curr;
                if( x.compareTo( curr.element ) < 0 ) {
                    curr = curr.getLeftOrNull();
                } else if( x.compareTo( curr.element ) > 0 ) {
                    curr = curr.getRightOrNull();
                } else {
                    throw new DuplicateItemException( x.toString( ) );
                }
            }
            
            if (x.compareTo(parent.element) < 0) {
                parent.left = new BinaryNode<>(x);
            } else {
                parent.right = new BinaryNode<>(x);
            }
        }        
    }

    /**
     * Remove from the tree..
     * @param x the item to remove.
     * @throws ItemNotFoundException if x is not found.
     */
    public void remove( AnyType x )
    {
        root = remove( x, root );
    }

    /**
     * Remove minimum item from the tree.
     * @throws ItemNotFoundException if tree is empty.
     */
    public void removeMin( )
    {
        root = removeMin( root );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( )
    {
        return isEmpty() ? null : elementAt( findMin( getRoot() ) );
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item or null if empty.
     */
    public AnyType findMax( )
    {
        return isEmpty() ? null : elementAt( findMax( getRoot() ) );
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return the matching item or null if not found.
     */
    public AnyType find( AnyType x )
    {
        return isEmpty() ? null : elementAt( find( x, getRoot() ) );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print all items.
     */
    public void printTree( )
    {
        if (!isEmpty())
            printTree( getRoot() );
    }

    /**
     * Count the number of nodes in this tree
     * @return the number of nodes
     */
    public int getSize() {
        if (isEmpty()) {
            return 0;
        } else {
            return getSize(getRoot());
        }
    }

    private int getSize(BinaryNode node) {
        int retValue = 1; // Go ahead and add in the depth of this node
        if (node.getLeftOrNull() != null) retValue += getSize(node.left);
        if (node.getRightOrNull() != null) retValue += getSize(node.right);
        return retValue;        
    }

    /**
     * Calculate the height of this tree
     * @return the height of the tree
     */    
    public long getHeight() {
        if (isEmpty()) {
            return -1;
        } else {
            return getHeight(getRoot());
        }
    }

    private int getHeight(BinaryNode node) {
        int heightLeft = -1;
        if (node.getLeftOrNull() != null) heightLeft = getHeight(node.left);
        int heightRight = -1;
        if (node.getRightOrNull() != null) heightRight = getHeight(node.right);
        return Math.max(heightRight+1, heightLeft+1);
    }

    /**
     * Calculate the internal path length of the tree. The internal path length
     * of a tree is the sum of the depths of all nodes in the tree - if you
     * divide this by the number of nodes in the tree, you get the average cost
     * of performing searching for a randomly chosen item in the tree. For a
     * little discussion of internal path length and its use in analyzing
     * average run time, see the Weiss book, pages 703-704.
     * @return 
     */
    public long getInternalPathLength() {
        if (isEmpty()) {
            return 0;
        } else {
            return getInternalPathLength(getRoot(), 0);
        }
    }

    private long getInternalPathLength(BinaryNode node, int depth) {
        long retValue = depth; // Go ahead and add in the depth of this node
        if (node.getLeftOrNull() != null) retValue += getInternalPathLength(node.left, depth+1);
        if (node.getRightOrNull() != null) retValue += getInternalPathLength(node.right, depth+1);
        return retValue;
    }
        
    /**
     * Internal method to print a subtree in sorted order. t must be an actual
     * node (can't be null).
     * @param t the node that roots the tree.
     */
    private void printTree( BinaryNode<AnyType> t )
    {
        if (t.getLeftOrNull() != null) printTree( t.left );
        System.out.println( t.element );
        if (t.getRightOrNull() != null) printTree( t.right );
    }

    /**
     * Used internally to get the root of the tree in an implementation-
     * independent way. Classes that extend BinarySearchTree, and use a
     * different representation of the root (e.g., a pointer from a header
     * node) can override this method so that the generic BST methods in
     * this class still work.
     * @return the root node
     */
    BinaryNode<AnyType> getRoot() {
        return root;
    }
    
    /**
     * Internal method to get element field.
     * @param t the node.
     * @return the element field or null if t is null.
     */
    private AnyType elementAt( BinaryNode<AnyType> t )
    {
        return t == null ? null : t.element;
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the tree.
     * @return the new root.
     * @throws DuplicateItemException if x is already present.
     */
    protected BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            t = new BinaryNode<AnyType>( x );
        else if( x.compareTo( t.element ) < 0 )
            t.left = insert( x, t.left );
        else if( x.compareTo( t.element ) > 0 )
            t.right = insert( x, t.right );
        else
            throw new DuplicateItemException( x.toString( ) );  // Duplicate
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the tree.
     * @return the new root.
     * @throws ItemNotFoundException if x is not found.
     */
    protected BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            throw new ItemNotFoundException( x.toString( ) );
        if( x.compareTo( t.element ) < 0 )
            t.left = remove( x, t.left );
        else if( x.compareTo( t.element ) > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = removeMin( t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }

    /**
     * Internal method to remove minimum item from a subtree.
     * @param t the node that roots the tree.
     * @return the new root.
     * @throws ItemNotFoundException if t is empty.
     */
    protected BinaryNode<AnyType> removeMin( BinaryNode<AnyType> t )
    {
        if( t == null )
            throw new ItemNotFoundException( );
        else if( t.left != null )
        {
            t.left = removeMin( t.left );
            return t;
        }
        else
            return t.right;
    }    

    /**
     * Internal method to find the smallest item in a subtree. Note - must
     * be called with non-null t
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
    {
        while( t.getLeftOrNull() != null )
            t = t.left;

        return t;
    }

    /**
     * Internal method to find the largest item in a subtree. Note - must
     * be called with non-null t
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
    {
        while( t.getRightOrNull() != null )
            t = t.right;

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private BinaryNode<AnyType> find( AnyType x, BinaryNode<AnyType> t )
    {
        while( t != null )
        {
            if( x.compareTo( t.element ) < 0 )
                t = t.getLeftOrNull();
            else if( x.compareTo( t.element ) > 0 )
                t = t.getRightOrNull();
            else
                return t;    // Match
        }
        
        return null;         // Not found
    }

      /** The tree root. */
    protected BinaryNode<AnyType> root;


        // Test program
    public static void main( String [ ] args )
    {
        BinarySearchTree<Integer> t = new BinarySearchTree<Integer>( );
        final int NUMS = 4000;
        final int GAP  =   37;

        System.out.println( "Checking... (no more output means success)" );

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            t.insert( i );

        for( int i = 1; i < NUMS; i += 2 )
            t.remove( i );

        if( t.findMin( ) != 2 || t.findMax( ) != NUMS - 2 )
            System.out.println( "FindMin or FindMax error!" );

        for( int i = 2; i < NUMS; i += 2 )
             if( t.find( i ) != i )
                 System.out.println( "Find error1!" );

        for( int i = 1; i < NUMS; i += 2 )
            if( t.find( i ) != null )
                System.out.println( "Find error2!" );
    }
}
