/**********************************************************************************************
 * @file : AVLTree.java
 * @description : AVL Tree class, including a root, left, right, and height variables.
 *                Public methods are insert(), contains(), remove(), and printTree();
 * @author : Ella Shipman
 * @date : October 18, 2025
 * @acknowledgement : Jessica Li's "Animal Crossing New Horizons Catalog", "villagers.csv" file.
 * https://www.kaggle.com/datasets/jessicali9530/animal-crossing-new-horizons-nookplaza-dataset.
 *********************************************************************************************/
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean remove( x )    --> Return true if x was present
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate


/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> {
    /** The tree root. */
    private AvlNode<AnyType> root;

    /**
     * Construct the tree.
     */
    public AVLTree( ) {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x ) {
        if (contains(x)) {      //if x is already in the tree, ignore insert
            System.out.println("AVLTree.java: insert - Node already exists in tree");
        } else {        //x is not in the tree, proceed with insert
            root = insert( x, root );
        }
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert( AnyType x, AvlNode<AnyType> t ) {
        // Traverse through the tree until an empty spot for x is found, insert x into tree, then update heights
        // for all nodes in the path from the root to x
        if (t == null) {        // Root is null, assign new root
            t = new AvlNode<AnyType>(x);
            return t;
        }
        if (t.getElement().compareTo(x) > 0) {       // root is greater than x -> left subtree
             t.setLeft(insert(x, t.getLeft()));
        } else if (t.getElement().compareTo(x) < 0){     // root is less than x -> right subtree
             t.setRight(insert(x, t.getRight()));
        }
        return balance(t);       // Return root of tree after balancing
    }


    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x ) {
        if (contains(x)) {      // check that x is in tree, if so -> remove
            root = remove(x, root);
        } else {        // if not in tree, ignore remove
            System.out.println("remove: cannot remove an element that is not in the tree (" + x + ")");
        }
    }


    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove( AnyType x, AvlNode<AnyType> t ) {
        if (t == null) {        // end of path, return null
            return t;
        }
        // x found in tree!
        if (t.getElement().compareTo(x) == 0) {
            // case: leaf and case: one child
            if (t.getLeft() == null) {
                return t.getRight();
            } else if (t.getRight() == null) {
                return t.getLeft();
            }
            // case: both children
            AvlNode<AnyType> min = findMin(t.getRight());      // find minimum of right subtree
            t.setElement(min.getElement());     // replace root's element with min in right subtree
            t.setRight(remove(min.getElement(), t.getRight()));     // remove min from root's right subtree
        // root is greater than x -> left subtree
        } else if (t.getElement().compareTo(x) > 0) {
            t.setLeft(remove(x, t.getLeft()));
        // root is less than x -> right subtree
        } else if (t.getElement().compareTo(x) < 0) {
            t.setRight(remove(x, t.getRight()));
        }
        //Update height for all nodes in the path from x to the root in order to gauge AVLTree's balance
        t.setHeight(1 + Math.max(height(t.getLeft()), height(t.getRight())));
        if (t.isLeaf()) { return t ; }
        return balance(t);
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMin( root ).element;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin( AvlNode<AnyType> t ) {
        if (t == null) { return t; }
        if (t.getLeft() == null) {      //if root does not have a left child, then root is the minimum -> return root
            return t;
        }
        return (findMin(t.getLeft()));      //there exists a left child, so findMin in left subtree
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMax( root ).element;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax( AvlNode<AnyType> t ) {
        if (t.getRight() == null) {      //if root does not have a right child, then root is the maximum -> return root
            return t;
        }
        return (findMax(t.getRight()));      //there exists a right child, so findMax in right subtree
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( AnyType x ) {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( ) {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( ) {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( ) {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    /**
     * Internal method to print a subtree in (sorted) order.
     * @param t the node that roots the tree.
     */
    private void printTree( AvlNode<AnyType> t ) {
        if (t == null) {
            return;
        }
        printTree(t.getLeft());
        System.out.print(t.getElement().toString() + "\n");
        printTree(t.getRight());
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced

    /**
     * Determines whether the root t should undergo a
     * single or double rotation to maintain the tree's balance.
     * @param t the root of the subtree
     * @return the new root of the subtree
     */
    private AvlNode<AnyType> balance( AvlNode<AnyType> t ) {
        if (t == null) {
            return t;
        }
        // imbalance in left subtree
        if (height(t.getLeft()) - height(t.getRight()) > ALLOWED_IMBALANCE) {
            if (height(t.getLeft().getLeft()) >= height(t.getLeft().getRight())) {
                //System.out.println("SR left");
                t = rotateWithLeftChild(t);         // imbalance is in left subtree of left child
            } else {
                //System.out.println("DR left");
                t = doubleWithLeftChild(t);     // imbalance is in right subtree of left child
            }
        // imbalance in right subtree
        } else if (height(t.getRight()) - height(t.getLeft()) > ALLOWED_IMBALANCE) {
            if (height(t.getRight().getRight()) >= height(t.getRight().getLeft())) {
                //System.out.println("SR right");
                t = rotateWithRightChild(t);        // imbalance is in right subtree of right child
            } else {
                t = doubleWithRightChild(t);     // imbalance is in left subtree of right child
            }
        }
        //Update height for all nodes in the path
        t.setHeight(Math.max(height(t.getLeft()), height(t.getRight())) + 1);
        return t;
    }

    /**
     * Verifies that the tree is indeed balanced.
     */
    public void checkBalance( ) {
        checkBalance( root );
    }

    private int checkBalance( AvlNode<AnyType> t ) {
        if( t == null )
            return -1;

        if( t != null ) {
            int hl = checkBalance( t.left );
            int hr = checkBalance( t.right );
            if( Math.abs( height( t.left ) - height( t.right ) ) > 1) {
                System.out.println("LH: " + height(t.getLeft()) + "  RH: " + height(t.getRight()) + "    LH - RH: " + (height(t.getLeft()) - height(t.getRight())));
                System.out.println("OOPS!! 1");
            }
            if (height( t.left ) != hl )
                System.out.println( "OOPS!! 2" );
            if (height( t.right ) != hr )
                System.out.println( "OOPS!! 3" );
        }

        return height( t );
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains( AnyType x, AvlNode<AnyType> t ) {
        if (t == null) {        //root is null, return false
            //System.out.println("contains - root is null, return false for " + x);
            return false;
        }
        if (t.getElement().compareTo(x) == 0) {      //found x in tree! return true
            //System.out.println("contains - found " + x + ", returning true");
            return true;
        } else if (t.getElement().compareTo(x) > 0) {       //root is greater than x -> left subtree
            //System.out.println("contains - moving into left subtree for " + x);
            return contains(x, t.getLeft());
        } else {     //root is less than x -> right subtree
            //System.out.println("contains - moving into right subtree for " + x);
            return contains(x, t.getRight());
        }
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height( AvlNode<AnyType> t ) {
        return t == null ? -1 : t.getHeight();
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild( AvlNode<AnyType> k2 ) {
        AvlNode<AnyType> k1 = k2.getLeft();
        // 1) identify the subtrees to be swapped
        AvlNode<AnyType> X = k1.getLeft();                          //         k2                      k2
        AvlNode<AnyType> Y = k1.getRight();                         //     k1      Z       ->       X       k1
        AvlNode<AnyType> Z = k2.getRight();                         //  X     Y                           Y     Z
        // 2) swap subtrees
        k2.setLeft(X);                                              // k2's left child is k1's left subtree, X
        k2.setRight(k1);                                            // k2's right child is k1
        k1.setLeft(Y);                                              // k1's left child is k1's right subtree, Y
        k1.setRight(Z);                                             // k1's right child is k2's right subtree, Z
        // adjust heights
        k1.setHeight(1 + Math.max(height(k1.getLeft()), height(k1.getRight())));
        k2.setHeight(1 + Math.max(height(k2.getLeft()), height(k2.getRight())));
        if (X != null) {X.setHeight(1 + Math.max(height(X.getLeft()), height(X.getRight())));}
        if (Y != null) { Y.setHeight(1 + Math.max(height(Y.getLeft()), height(Y.getRight())));}
        if (Z != null) { Z.setHeight(1 + Math.max(height(Z.getLeft()), height(Z.getRight())));}
        // 3) swap values of k1 and k2
        swapElements(k1, k2);                                       //     k2                        k1
        // 4) return the new root of the subtree,                   //  X      k1       ->        X       k2
        // k2 (whose element has been swapped with k1's element)    //       Y     Z                    Y     Z
        return k2;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild( AvlNode<AnyType> k1 ) {
        AvlNode<AnyType> k2 = k1.getRight();
        // 1) identify the subtrees to be swapped
        AvlNode<AnyType> X = k1.getLeft();                          //     k1                           k1
        AvlNode<AnyType> Y = k2.getLeft();                          //  X      k2       ->          k2      Z
        AvlNode<AnyType> Z = k2.getRight();                         //       Y     Z             X     Y
        // 2) swap subtrees
        k1.setLeft(k2);                                             // k1's left child is k2
        k1.setRight(Z);                                             // k1's right child is Z
        k2.setLeft(X);                                              // k2's left child is X
        k2.setRight(Y);                                             // k2's right child is Y
        // adjust heights
        k1.setHeight(1 + Math.max(height(k1.getLeft()), height(k1.getRight())));
        k2.setHeight(1 + Math.max(height(k2.getLeft()), height(k2.getRight())));
        if (X != null) {X.setHeight(1 + Math.max(height(X.getLeft()), height(X.getRight())));}
        if (Y != null) { Y.setHeight(1 + Math.max(height(Y.getLeft()), height(Y.getRight())));}
        if (Z != null) { Z.setHeight(1 + Math.max(height(Z.getLeft()), height(Z.getRight())));}
        // 3) swap values of k1 and k2
        swapElements(k1, k2);                                       //         k1                          k2
        // 4) return the new root of the subtree,                   //     k2      Z       ->          k1      Z
        // k1 (whose element has been swapped with k2's element)    //  X     Y                     X     Y
        return k1;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild( AvlNode<AnyType> k3 ) {
        AvlNode<AnyType> k1 = k3.getLeft();
        AvlNode<AnyType> k2 = k1.getRight();
        // 1) identify the subtrees to be swapped
        AvlNode<AnyType> A = k1.getLeft();                          //         k3                      k3
        AvlNode<AnyType> B = k2.getLeft();                          //     k1      D       ->       k1     k2
        AvlNode<AnyType> C = k2.getRight();                         //  A     k2                 A    B   C    D
        AvlNode<AnyType> D = k3.getRight();                         //      B    C
        // 2) swap subtrees
        k3.setLeft(k1);                                             // k3's left child -> k1
        k3.setRight(k2);                                            // k3's right child -> k2
        k1.setLeft(A);                                              // k1's left child -> A
        k1.setRight(B);                                             // k1's right child -> B
        k2.setLeft(C);                                              // k2's left child -> C
        k2.setRight(D);                                             // k2's right child -> D
        // adjust heights
        k1.setHeight(1 + Math.max(height(k1.getLeft()), height(k1.getRight())));
        k2.setHeight(1 + Math.max(height(k2.getLeft()), height(k2.getRight())));
        k3.setHeight(1 + Math.max(height(k3.getLeft()), height(k3.getRight())));
        if (A != null) {A.setHeight(1 + Math.max(height(A.getLeft()), height(A.getRight())));}
        if (B != null) { B.setHeight(1 + Math.max(height(B.getLeft()), height(B.getRight())));}
        if (C != null) { C.setHeight(1 + Math.max(height(C.getLeft()), height(C.getRight())));}
        if (D != null) { D.setHeight(1 + Math.max(height(D.getLeft()), height(D.getRight())));}
        // 3) swap values of k2 and k3
        swapElements(k2, k3);                                       //       k3                         k2
        // 4) return the new root of the subtree,                   //    k1     k2         ->       k1     k3
        // k3 (whose element has been swapped with k2's element)    // A    B   C    D            A    B   C    D
        return k3;
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild( AvlNode<AnyType> k1 ) {
        AvlNode<AnyType> k3 = k1.getRight();
        AvlNode<AnyType> k2 = k3.getLeft();
        // 1) identify the subtrees to be swapped
        AvlNode<AnyType> A = k1.getLeft();                          //         k1                      k1
        AvlNode<AnyType> B = k2.getLeft();                          //     A      k3       ->       k2     k3
        AvlNode<AnyType> C = k2.getRight();                         //        k2     D           A    B   C    D
        AvlNode<AnyType> D = k3.getRight();                         //      B    C
        // 2) swap subtrees
        k1.setLeft(k2);                                             // k1's left child -> k2
        k1.setRight(k3);                                            // k1's right child -> k3
        k2.setLeft(A);                                              // k2's left child -> A
        k2.setRight(B);                                             // k2's right child -> B
        k3.setLeft(C);                                              // k3's left child -> C
        k3.setRight(D);                                             // k3's right child -> D
        k1.setHeight(1 + Math.max(height(k1.getLeft()), height(k1.getRight())));
        k2.setHeight(1 + Math.max(height(k2.getLeft()), height(k2.getRight())));
        k3.setHeight(1 + Math.max(height(k3.getLeft()), height(k3.getRight())));
        if (A != null) {A.setHeight(1 + Math.max(height(A.getLeft()), height(A.getRight())));}
        if (B != null) { B.setHeight(1 + Math.max(height(B.getLeft()), height(B.getRight())));}
        if (C != null) { C.setHeight(1 + Math.max(height(C.getLeft()), height(C.getRight())));}
        if (D != null) { D.setHeight(1 + Math.max(height(D.getLeft()), height(D.getRight())));}
        // 3) swap values of k1 and k2
        swapElements(k1, k2);                                       //       k1                         k2
        // 4) return the new root of the subtree,                   //    k2     k3         ->       k1     k3
        // k1 (whose element has been swapped with k2's element)    // A    B   C    D            A    B   C    D
        return k1;
    }

    /**
     * Swap the elements of binary tree nodes k1 and k2.
     */
    private void swapElements(AvlNode<AnyType> k1, AvlNode<AnyType> k2) {
        AnyType val1 = k1.getElement();
        k1.setElement(k2.getElement());
        k2.setElement(val1);
    }

    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode( AnyType theElement ) {
            this( theElement, null, null );
        }

        AvlNode( AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt ) {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        AnyType           element;      // The data in the node
        AvlNode<AnyType>  left;         // Left child
        AvlNode<AnyType>  right;        // Right child
        int               height;       // Height

        //Get and set value
        public AnyType getElement() { return element; }
        public void setElement(AnyType newValue) {this.element = newValue;}

        //Get and set left
        public AvlNode<AnyType> getLeft() {return left;}
        public void setLeft(AvlNode<AnyType> nextNode) {left = nextNode;}

        //Get and set right
        public AvlNode<AnyType> getRight() {return right;}
        public void setRight(AvlNode<AnyType> nextNode) {right = nextNode;}

        //Get and set height
        public void setHeight(int h) { height = h; }
        public int getHeight() { return height; }

        //Returns true if node is a leaf, otherwise false
        public boolean isLeaf() {
            return ((right == null) && (left == null));
        }
    }
}
