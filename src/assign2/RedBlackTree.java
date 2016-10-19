/*
 * This file is for Assignment 2 in UNCG's CSC 330 class in Fall 2016.
 */

package assign2;

// RedBlackTree class
//
// This is a slightly modified version of the RedBlackTree implementation from
// the Weiss "Data Structures and Problem Solving Using Java" textbook, created
// for the UNCG CSC 330 class, Fall 2016
//
// It differs in that this class extends the BinarySearchTree class, overriding
// insert and remove (unimplemented), but falling back on generic operations
// for other BinarySearchTree operations (find, etc.).
//
// CONSTRUCTION: with no parameters
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// Comparable find( x )   --> Return item that matches x
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print all items
// ******************ERRORS********************************
// Exceptions are thrown by insert if warranted and remove.

/**
 * Implements a red-black tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 * @param <AnyType>
 */
public class RedBlackTree<AnyType extends Comparable<? super AnyType>> extends BinarySearchTree<AnyType>
{
    /**
     * Construct the tree.
     */
    public RedBlackTree( )
    {
        nullNode = new RedBlackNode<>( null );
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode<>( null );
        header.left = header.right = nullNode;
    }

    /**
     * Compare item and t.element, using compareTo, with
     * caveat that if t is header, then item is always larger.
     * This routine is called if is possible that t is header.
     * If it is not possible for t to be header, use compareTo directly.
     */
    private int compare( AnyType item, RedBlackNode<AnyType> t )
    {
        if( t == header )
            return 1;
        else
            return item.compareTo( t.element );    
    }
    
    /**
     * Insert into the tree.
     * @param item the item to insert.
     * @throws DuplicateItemException if item is already present.
     */
    @Override
    public void insert( AnyType item )
    {
        current = parent = grand = header;
        nullNode.element = item;

        while( compare( item, current ) != 0 )
        {
            great = grand; grand = parent; parent = current;
            current = compare( item, current ) < 0 ?
                         current.getLeft() : current.getRight();

                // Check if two red children; fix if so
            if( current.getLeft().color == RED && current.getRight().color == RED )
                 handleReorient( item );
        }

            // Insertion fails if already present
        if( current != nullNode )
            throw new DuplicateItemException( item.toString( ) );
        current = new RedBlackNode<>( item, nullNode, nullNode );

            // Attach to parent
        if( compare( item, parent ) < 0 )
            parent.left = current;
        else
            parent.right = current;
        handleReorient( item );
    }

    /**
     * Remove from the tree.
     * @param x the item to remove.
     * @throws UnsupportedOperationException if called.
     */
    @Override
    public void remove( AnyType x )
    {
        throw new UnsupportedOperationException( );
    }

    /**
     * Make the tree logically empty.
     */
    @Override
    public void makeEmpty( )
    {
        header.right = nullNode;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    @Override
    public boolean isEmpty( )
    {
        return header.right == nullNode;
    }

    /**
     * Internal routine that is called during an insertion
     * if a node has two red children. Performs flip and rotations.
     * @param item the item being inserted.
     */
    private void handleReorient( AnyType item )
    {
            // Do the color flip
        current.color = RED;
        current.getLeft().color = BLACK;
        current.getRight().color = BLACK;

        if( parent.color == RED )   // Have to rotate
        {
            grand.color = RED;
            if( ( compare( item, grand ) < 0 ) !=
                ( compare( item, parent ) < 0 ) )
                parent = rotate( item, grand );  // Start dbl rotate
            current = rotate( item, great );
            current.color = BLACK;
        }
        header.getRight().color = BLACK; // Make root black
    }

    /**
     * Internal routine that performs a single or double rotation.
     * Because the result is attached to the parent, there are four cases.
     * Called by handleReorient.
     * @param item the item in handleReorient.
     * @param parent the parent of the root of the rotated subtree.
     * @return the root of the rotated subtree.
     */
    private RedBlackNode<AnyType> rotate( AnyType item, RedBlackNode<AnyType> parent )
    {
        if( compare( item, parent ) < 0 ) {
            parent.left = compare( item, parent.getLeft() ) < 0 ?
                rotateWithLeftChild( parent.getLeft() )  :  // LL
                rotateWithRightChild( parent.getLeft() ) ;  // LR
            return parent.getLeft();
        } else {
            parent.right = compare( item, parent.getRight() ) < 0 ?
                rotateWithLeftChild( parent.getRight() ) :  // RL
                rotateWithRightChild( parent.getRight() );  // RR
            return parent.getRight();
        }
    }

    /**
     * Rotate binary tree node with left child.
     */
    private RedBlackNode<AnyType> rotateWithLeftChild( RedBlackNode<AnyType> k2 )
    {
        RedBlackNode<AnyType> k1 = k2.getLeft();
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     */
    private RedBlackNode<AnyType> rotateWithRightChild( RedBlackNode<AnyType> k1 )
    {
        RedBlackNode<AnyType> k2 = k1.getRight();
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private class RedBlackNode<AnyType> extends BinaryNode<AnyType>
    {
        int color;

        // Constructors
        RedBlackNode( AnyType theElement )
        {
            super(theElement);
            color = BLACK;
        }

        RedBlackNode( AnyType theElement, RedBlackNode<AnyType> lt, RedBlackNode<AnyType> rt )
        {
            super(theElement, lt, rt);
            color = BLACK;
        }

        // Getters simplify left/right access to avoid casting every time
        
        RedBlackNode<AnyType> getLeft() {
            return (RedBlackNode)this.left;
        }

        RedBlackNode<AnyType> getRight() {
            return (RedBlackNode)this.right;
        }
        
        @Override
        protected BinaryNode<AnyType> getLeftOrNull() {
            return (left==nullNode)?null:left;
        }

        @Override
        protected BinaryNode<AnyType> getRightOrNull() {
            return (right==nullNode)?null:right;
        }
    }

    @Override
    BinaryNode<AnyType> getRoot() {
        return header.right;
    }
    
    private RedBlackNode<AnyType> header;
    private RedBlackNode<AnyType> nullNode;

    private static final int BLACK = 1;    // BLACK must be 1
    private static final int RED   = 0;

        // Used in insert routine and its helpers
    private RedBlackNode<AnyType> current;
    private RedBlackNode<AnyType> parent;
    private RedBlackNode<AnyType> grand;
    private RedBlackNode<AnyType> great;


        // Test program
    public static void main( String [ ] args )
    {
        RedBlackTree<Integer> t = new RedBlackTree<>( );
        final int NUMS = 400000;
        final int GAP  =  35461;

        System.out.println( "Checking... (no more output means success)" );

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            t.insert( i );

        if( t.findMin( ) != 1 || t.findMax( ) != NUMS - 1 )
            System.out.println( "FindMin or FindMax error!" );

        for( int i = 1; i < NUMS; i++ )
             if( t.find( i ) != i )
                 System.out.println( "Find error1!" );
    }
}
