/*
 * This file is for Assignment 2 in UNCG's CSC 330 class in Fall 2016.
 *
 * Solution and testing code/
 */
package assign2;

// RedBlackTree class
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
 * Implements a red-black tree. Note that all "matching" is based on the
 * compareTo method.
 *
 * @author Mark Allen Weiss
 * @param <AnyType>
 */
public class AltRedBlackTree<AnyType extends Comparable<? super AnyType>> extends BinarySearchTree<AnyType> {
    private AltRBNode<AnyType> header;
    private AltRBNode<AnyType> nullNode;

    private static final int BLACK = 1;    // BLACK must be 1
    private static final int RED = 0;

    private class AltRBNode<AnyType> extends BinaryNode<AnyType> {
        int color;
        AltRBNode<AnyType> parent;

        // Constructors
        AltRBNode(AnyType theElement, AltRBNode<AnyType> par) {
            super(theElement);
            color = BLACK;
            parent = par;
        }

        AltRBNode(AnyType theElement, AltRBNode<AnyType> par, AltRBNode<AnyType> lt, AltRBNode<AnyType> rt) {
            super(theElement, lt, rt);
            color = BLACK;
            parent = par;
        }

        // Getters simplify left/right access to avoid casting every time
        AltRBNode<AnyType> getLeft() {
            return (AltRBNode) this.left;
        }

        AltRBNode<AnyType> getRight() {
            return (AltRBNode) this.right;
        }

        @Override
        protected BinaryNode<AnyType> getLeftOrNull() {
            return (left == nullNode) ? null : left;
        }

        @Override
        protected BinaryNode<AnyType> getRightOrNull() {
            return (right == nullNode) ? null : right;
        }
    }
    
    /**
     * Construct the tree.
     */
    public AltRedBlackTree() {
        nullNode = new AltRBNode<>(null, null);
        nullNode.left = nullNode.right = nullNode.parent = nullNode;
        header = new AltRBNode<>(null, nullNode);
        header.left = header.right = nullNode;
    }

    /**
     * Compare item and t.element, using compareTo, with caveat that if t is
     * header, then item is always larger. This routine is called if is possible
     * that t is header. If it is not possible for t to be header, use compareTo
     * directly.
     */
    private int compare(AnyType item, AltRBNode<AnyType> t) {
        if (t == header) {
            return 1;
        } else {
            return item.compareTo(t.element);
        }
    }

    /**
     * Insert into the tree.
     *
     * @param item the item to insert.
     * @throws DuplicateItemException if item is already present.
     */
    @Override
    public void insert(AnyType item) {
        AltRBNode<AnyType> parent = header;
        AltRBNode<AnyType> current = header.getRight();
        nullNode.element = item;

        while (compare(item, current) != 0) {
            parent = current;
            if (compare(item, current) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        // Insertion fails if already present
        if (current != nullNode) {
            throw new DuplicateItemException(item.toString());
        }
        current = new AltRBNode<>(item, parent, nullNode, nullNode);
        current.color = RED;

        // Attach to parent
        if (compare(item, parent) < 0) {
            parent.left = current;
        } else {
            parent.right = current;
        }
        adjustTree(current);
    }

    /**
     * Remove from the tree.
     *
     * @param x the item to remove.
     * @throws UnsupportedOperationException if called.
     */
    @Override
    public void remove(AnyType x) {
        throw new UnsupportedOperationException();
    }

    /**
     * Make the tree logically empty.
     */
    @Override
    public void makeEmpty() {
        header.right = nullNode;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return header.right == nullNode;
    }

    /**
     * Private helper function to get the sibling of a node
     * @param node the node to find sibling for
     * @return node's sibling
     */
    private AltRBNode<AnyType> getSibling(AltRBNode<AnyType> node) {
        if (node.parent.left == node) {
            return node.parent.getRight();
        } else {
            return node.parent.getLeft();
        }
    }

    /**
     * Internal routine - walk up the tree from curr, fixing any situation where
     * two RED nodes are adjacent.
     *
     * @param curr
     */
    private void adjustTree(AltRBNode<AnyType> curr) {
        AnyType item = curr.element;   // For convenience...

        while ((curr.color == RED) && (curr.parent.color == RED)) {
            // Note: Since parent is red, and header and root (if not this node)
            // are both BLACK, must have a grandparent 
            AltRBNode<AnyType> parent = curr.parent;
            AltRBNode<AnyType> parSibling = getSibling(parent);
            if (parSibling.color == RED) {
                // No rotation needed - just push BLACK color down from gparent
                parent.color = BLACK;
                parSibling.color = BLACK;
                curr = parent.parent;
                curr.color = RED;
            } else {
                parent.parent.color = RED;
                if ((compare(item, parent.parent) < 0)
                        != (compare(item, parent) < 0)) {
                    // Need double rotation, so rotate at parent first
                    parent = rotate(item, parent.parent);
                }
                rotate(item, parent.parent.parent);
                parent.color = BLACK;
                break;   // No more looking up tree needed
            }
        }
        
        // Finally: Make sure root is black.
        header.getRight().color = BLACK;
    }

    /**
     * Internal routine that performs a single or double rotation. Because the
     * result is attached to the parent, there are four cases. Called by
     * adjustTree when rotations are needed.
     *
     * @param item the item in the "inserted" subtree.
     * @param parent the parent of the root of the rotated subtree.
     * @return the root of the rotated subtree.
     */
    private AltRBNode<AnyType> rotate(AnyType item, AltRBNode<AnyType> parent) {
        if (compare(item, parent) < 0) {
            parent.left = compare(item, parent.getLeft()) < 0
                    ? rotateWithLeftChild(parent.getLeft())
                    : // LL
                    rotateWithRightChild(parent.getLeft());  // LR
            return parent.getLeft();
        } else {
            parent.right = compare(item, parent.getRight()) < 0
                    ? rotateWithLeftChild(parent.getRight())
                    : // RL
                    rotateWithRightChild(parent.getRight());  // RR
            return parent.getRight();
        }
    }

    /**
     * Rotate alt red black tree node with left child.
     */
    private AltRBNode<AnyType> rotateWithLeftChild(AltRBNode<AnyType> k2) {
        AltRBNode<AnyType> k1 = k2.getLeft();
        k2.left = k1.right;
        k1.getRight().parent = k2;
        k1.right = k2;
        k1.parent = k2.parent;
        k2.parent = k1;
        return k1;
    }

    /**
     * Rotate alt red black tree node with right child.
     */
    private AltRBNode<AnyType> rotateWithRightChild(AltRBNode<AnyType> k1) {
        AltRBNode<AnyType> k2 = k1.getRight();
        k1.right = k2.left;
        k2.getLeft().parent = k1;
        k2.left = k1;
        k2.parent = k1.parent;
        k1.parent = k2;
        return k2;
    }

    @Override
    BinaryNode<AnyType> getRoot() {
        return (header.right != nullNode) ? header.right : null;
    }

    /**
     * RBTTester tests if a tree is a valid red-black tree (satisfies all
     * properties of a binary search tree and a red-black tree). Uses a logger
     * function to limit how many error messages of each type are printed, so
     * that buggy implementations don't output gigabytes upon gigabytes of
     * error messages.
     */
    private class RBTTester {
        boolean error;

        private int colorErrCount = 0;
        private int depthErrCount = 0;
        private int bstErrCount = 0;

        private static final int ERR_MISC = 0;
        private static final int ERR_COLOR = 1;
        private static final int ERR_DEPTH = 2;
        private static final int ERR_BST = 3;
        private static final int MAX_COLOR_ERR = 50;
        private static final int MAX_DEPTH_ERR = 20;
        private static final int MAX_BST_ERR = 20;

        private void loggerReset() {
            colorErrCount = 0;
            depthErrCount = 0;
        }

        private void loggerPrint(int type, String message) {
            error = true;
            if (ERR_COLOR == type) {
                if (colorErrCount < MAX_COLOR_ERR) {
                    System.out.println(message);
                    colorErrCount++;
                } else if (colorErrCount == MAX_COLOR_ERR) {
                    System.out.println("COLOR error maximum count exceeded");
                    colorErrCount++;
                }
            } else if (ERR_DEPTH == type) {
                if (depthErrCount < MAX_DEPTH_ERR) {
                    System.out.println(message);
                    depthErrCount++;
                } else if (depthErrCount == MAX_DEPTH_ERR) {
                    System.out.println("DEPTH error maximum count exceeded");
                    depthErrCount++;
                }
            } else if (ERR_BST == type) {
                if (bstErrCount < MAX_BST_ERR) {
                    System.out.println(message);
                    bstErrCount++;
                } else if (bstErrCount == MAX_BST_ERR) {
                    System.out.println("BST error maximum count exceeded");
                    bstErrCount++;
                }
            } else {
                System.out.println(message);
            }
        }

        private RBTTester() {
            // Disallow default constructor
        }

        // Note: Only call for a tree that is supposed to be non-empty!
        public RBTTester(AltRedBlackTree<AnyType> tree) {
            if (isEmpty()) {
                loggerPrint(ERR_MISC, "Tree is empty but should not be!");
            } else {
                error = false;
                loggerReset();
                AltRBNode<AnyType> root = (AltRBNode<AnyType>) tree.getRoot();
                if (root.color != BLACK) {
                    loggerPrint(ERR_COLOR, "Root node " + header.right.element + " not black!");
                }
                checkBlackDepth(root);
                checkColors(root);
                checkBSTOrder(root);
            }
        }

        // Note: Returning -2 means there was an error
        private int checkBlackDepth(AltRBNode<AnyType> node) {
            if (node == null) {
                return 0;
            } else {
                int leftBD = checkBlackDepth((AltRBNode)node.getLeftOrNull());
                int rightBD = checkBlackDepth((AltRBNode)node.getRightOrNull());
                if ((leftBD == -2) || (rightBD == -2)) {
                    // Case 1: Error further down in the tree
                    return -2;
                } else if (leftBD != rightBD) {
                    // Case 2: Error (imbalance) here
                    loggerPrint(ERR_DEPTH,
                          "Black depth below " + node.element + " unbalanced");
                    return -2;
                } else {
                    // Case 3: All good!
                    return leftBD + ((node.color==BLACK)?1:0);
                }
            }
        }
        
        private void checkColors(AltRBNode<AnyType> curr) {
            if (curr != nullNode) {
                if (curr.color == RED) {
                    if (curr.getLeft().color != BLACK) {
                        loggerPrint(ERR_COLOR, 
                            "RED node " + curr.element + " left child " +
                            curr.left.element + " also RED");
                    }
                    if (curr.getRight().color != BLACK) {
                        loggerPrint(ERR_COLOR,
                            "RED node " + curr.element + " right child " +
                            curr.right.element + " also RED");
                    }
                }
                checkColors(curr.getLeft());
                checkColors(curr.getRight());
            }
        }

        private void checkBSTOrder(AltRBNode<AnyType> curr) {
            if (curr != nullNode) {
                if ((curr.left != nullNode) && (curr.left.element.compareTo(curr.element) >= 0)) {
                    loggerPrint(ERR_BST, "BST error: Left >= node at node " + curr.element);
                }
                if ((curr.right != nullNode) && (curr.right.element.compareTo(curr.element) <= 0)) {
                    loggerPrint(ERR_BST, "BST error: Right <= node at node " + curr.element);
                }
                checkBSTOrder(curr.getLeft());
                checkBSTOrder(curr.getRight());
            }
        }
        
        public boolean hasError() {
            return error;
        }
    }

    public boolean testTree() {
        RBTTester tester = new RBTTester(this);
        return tester.hasError();
    }

    // Test program
    public static void main(String[] args) {
        AltRedBlackTree<Integer> t = new AltRedBlackTree<>();
        final int NUMS = 40000;
        final int GAP = 35461;

        System.out.println("Checking... (no more output means success)");

        for (int i = GAP; i != 0; i = (i + GAP) % NUMS) {
            System.out.println("Insert " + i);
            t.insert(i);
//            System.out.println("Inserting "+i);
//            t.validateTree();
//            if (t.error) return;
        }

        if (t.findMin() != 1 || t.findMax() != NUMS - 1) {
            System.out.println("FindMin or FindMax error!");
        }

        for (int i = 1; i < NUMS; i++) {
            if (t.find(i) != i) {
                System.out.println("Find error1!");
            }
        }
    }
}
