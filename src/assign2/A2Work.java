/*
 * This file is for Assignment 2 in UNCG's CSC 330 class in Fall 2016.
 */
package assign2;

import java.util.Random;

/**
 * This class contains testing/benchmark code for different BST implementations.
 * 
 * @author Steve Tate
 * 
 */
public class A2Work {
    // Constants that define tests
    static final int TEST_COUNT = 30000;    // Num items to insert
    static final int TEST_MAXVAL = 1000000; // Max val (+1) of values to insert
    static final long TEST_SEED = 123;      // Seed - change for diff sequences

    /**
     * Prints average and worst case comparison counts for a binary tree
     * @param bst any BinarySearchTree (or extension)
     * @param title descriptive string to print at beginning of report
     */    
    public static void printStats(BinarySearchTree bst, String title) {
        double numNodes = bst.getSize();
        
        System.out.println(title);
        System.out.printf("Average comparisons, successful search = %.2f\n", ((bst.getInternalPathLength()+1)/numNodes));
        System.out.println("Worst-case comparisons, successful search = "+(bst.getHeight()+1));
        System.out.println();
    }

    /**
     * Inserts pseudorandom values into a binary search tree.
     * @param bst a binary search tree for insertions - typically empty on call
     */
    public static void makeRandomBST(BinarySearchTree<Integer> bst) {
        Random prng = new Random(TEST_SEED);
        for (int i=0; i<TEST_COUNT; i++) {
            int randInt = prng.nextInt(TEST_MAXVAL);
            if (bst.find(randInt) == null) {
                bst.insert(randInt);
            }
        }
    }

    /**
     * Builds a binary search tree with no balancing, using random insertions
     * @return the BST
     */
    public static BinarySearchTree testUnbalancedRandom() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        makeRandomBST(bst);
        return bst;
    }

    /**
     * Builds a red-black tree top-down (as in the Weiss textbook), using
     * random insertions
     * @return the BST
     */
    public static BinarySearchTree testWeissRandom() {
        BinarySearchTree<Integer> bst = new RedBlackTree<>();
        makeRandomBST(bst);
        return bst;
    }

    /**
     * Builds a red-black tree with bottom-up rebalancing, using random
     * insertions
     * @return the BST
     */
    public static BinarySearchTree testAltRandom() {
        BinarySearchTree<Integer> bst = new AltRedBlackTree<>();
        makeRandomBST(bst);
        return bst;
    }

    /**
     * Inserts sequential values into a binary search tree.
     * @param bst a binary search tree for insertions - typically empty on call
     */
    public static void makeSeqBST(BinarySearchTree<Integer> bst) {
        for (int i=0; i<TEST_COUNT; i++) {
            // Note: find always fails to find value, but test is included
            // for consistency with random case
            if (bst.find(i) == null) {
                bst.insert(i);
            }
        }
    }

    /**
     * Builds a binary search tree with no balancing, using sequential insertions
     * @return the BST
     */
    public static BinarySearchTree testUnbalancedSeq() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        makeSeqBST(bst);
        return bst;
    }

    /**
     * Builds a red-black tree top-down (as in the Weiss textbook), using
     * sequential insertions
     * @return the BST
     */
    public static BinarySearchTree testWeissSeq() {
        BinarySearchTree<Integer> bst = new RedBlackTree<>();
        makeSeqBST(bst);
        return bst;
    }

    /**
     * Builds a red-black tree with bottom-up rebalancing, using sequential
     * insertions
     * @return the BST
     */
    public static BinarySearchTree testAltSeq() {
        BinarySearchTree<Integer> bst = new AltRedBlackTree<>();
        makeSeqBST(bst);
        return bst;
    }

    /**
     * main program - drives all the tests
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Quick sanity-check for test parameters
        if (TEST_COUNT > TEST_MAXVAL) {
            System.err.println("Bad test parameters - count should be at least twice maxval");
            return;
        }
        
        BinarySearchTree bst1 = testUnbalancedRandom();
        printStats(bst1, "Unbalanced BST, random insertions");
        
        BinarySearchTree bst2 = testWeissRandom();
        printStats(bst2, "Textbook Red-Black Tree, random insertions");
        
        BinarySearchTree bst3 = testAltRandom();
        printStats(bst3, "Alternate Red-Black Tree, random insertions");
        
        BinarySearchTree bst4 = testUnbalancedSeq();
        printStats(bst4, "Unbalanced BST, sequential insertions");
        
        BinarySearchTree bst5 = testWeissSeq();
        printStats(bst5, "Textbook Red-Black Tree, sequential insertions");
        
        BinarySearchTree bst6 = testAltSeq();
        printStats(bst6, "Alternate Red-Black Tree, sequential insertions");        
    }
}
