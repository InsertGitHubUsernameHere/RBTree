/*
 * This file is for Assignment 2 in UNCG's CSC 330 class in Fall 2016.
 */

package assign2;

// Basic node stored in unbalanced binary search trees
// Note that this class is not accessible outside
// of this package.

class BinaryNode<AnyType>
{
      // Data; accessible by other package routines
    AnyType             element;  // The data in the node
    BinaryNode<AnyType> left;     // Left child
    BinaryNode<AnyType> right;    // Right child

    // Constructor
    BinaryNode( AnyType theElement )
    {
        element = theElement;
        left = right = null;
    }

    protected BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt) {
        element = theElement;
        left = lt;
        right = rt;
    }

    /**
     * Returns either a valid binary tree node or a null reference. This isn't
     * very interesting for the basic BST implementation shown here, but
     * extensions that represent leaves in a different way (e.g., with a pointer
     * to a dummy "nullNode") can override this method to translate the more
     * complicated implementation back into the simple "node or null" case.
     * @return either a BinaryNode or null reference
     */
    protected BinaryNode<AnyType> getLeftOrNull() {
        return left;
    }

    /**
     * Returns either a valid binary tree node or a null reference. This isn't
     * very interesting for the basic BST implementation shown here, but
     * extensions that represent leaves in a different way (e.g., with a pointer
     * to a dummy "nullNode") can override this method to translate the more
     * complicated implementation back into the simple "node or null" case.
     * @return either a BinaryNode or null reference
     */
    protected BinaryNode<AnyType> getRightOrNull() {
        return right;
    }
}
