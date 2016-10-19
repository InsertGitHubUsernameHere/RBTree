/*
 * CSC 330 - Assignment 2 (bottom-up red-black tree rebalanacing) test code
 */
package assign2;

/**
 *
 * @author srt
 */
public class ARBTTest {
    public static int testerSize(BinaryNode t) {
        if (t == null) {
            return 0;
        } else {
            return 1+testerSize(t.getLeftOrNull())+testerSize(t.getRightOrNull());
        }
    }
    
    public static int testerHeight(BinaryNode t) {
        if (t == null) {
            return -1;
        } else {
            return 1+Math.max(testerHeight(t.getLeftOrNull()),testerHeight(t.getRightOrNull()));
        }
    }
    
    public static int testerIPL(BinaryNode t, int thisDepth) {
        if (t == null) {
            return 0;
        } else {
            return thisDepth+testerIPL(t.getLeftOrNull(),thisDepth+1)+testerIPL(t.getRightOrNull(),thisDepth+1);
        }
    }
    
    public static void main(String argv[]) {
        boolean allOK = true;
        AltRedBlackTree<Integer> t = new AltRedBlackTree<>();
        final int MOD = 41351;
        final int MULT = 5629;
        int val = 6134;
        for (int i = 0; i < 4000; i++) {
            t.insert(val);
            if (t.testTree()) {
                System.out.println("Invalid tree after inserting " + val);
                allOK = false;
            }
            val = (val * MULT) % MOD;
        }
        if (allOK) {
            System.out.println("Good tree operations!");
        }

        allOK = true;
        long goodVal = testerSize(t.getRoot());
        long testVal = t.getSize();
        if (goodVal != testVal) {
            System.out.println("StatsTest: getSize() test on tree failed: got "+
                    testVal+" when should be "+goodVal);
            allOK = false;
        }
        goodVal = testerHeight(t.getRoot());
        testVal = t.getHeight();
        if (goodVal != testVal) {
            System.out.println("StatsTest: getHeight() test on tree failed: got "+
                    testVal+" when should be "+goodVal);
            allOK = false;
        }
        goodVal = testerIPL(t.getRoot(),0);
        testVal = t.getInternalPathLength();
        if (goodVal != testVal) {
            System.out.println("StatsTest: getInternalPathLength() test on tree failed: got "+
                    testVal+" when should be "+goodVal);
            allOK = false;
        }
        if (allOK) {
            System.out.println("Passed all tree stats tests (full tree)");
        }
        
        allOK = true;
        BinarySearchTree<String> emptyTree = new AltRedBlackTree<>();
        testVal = emptyTree.getSize();
        if (testVal != 0) {
            System.out.println("StatsTest: getSize() test on empty tree failed - got "+testVal);
            allOK = false;
        }
        testVal = emptyTree.getHeight();
        if (testVal != -1) {
            System.out.println("StatsTest: getHeight() test on empty tree failed - got "+testVal);
            allOK = false;
        }
        testVal = emptyTree.getInternalPathLength();
        if (testVal != 0) {
            System.out.println("StatsTest: getIPL() test on empty tree failed - got "+testVal);
            allOK = false;
        }
        if (allOK) {
            System.out.println("Passed all tree stats tests (empty tree)");
        }
        
    }
}
