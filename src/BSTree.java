/**********************************************************************************************
 * @file : BSTree.java
 * @description : Binary Search Tree class, including a root, left, and right children.
 *                Public methods are insert(), contains(), remove(), and printTree();
 * @author : Ella Shipman
 * @date : October 18, 2025
 * @acknowledgement : Jessica Li's "Animal Crossing New Horizons Catalog", "villagers.csv" file.
 * https://www.kaggle.com/datasets/jessicali9530/animal-crossing-new-horizons-nookplaza-dataset.
 *********************************************************************************************/

public class BSTree <AnyType extends Comparable<? super AnyType>> {
    //The tree's root
    BSTNode<AnyType> root;

    /**
     * Construct the tree.
     */
    public BSTree( ) {
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
    private BSTNode<AnyType> insert(AnyType x, BSTNode<AnyType> t ) {
        // Traverse through the tree until an empty spot for x is found, insert x into tree, then update heights
        // for all nodes in the path from the root to x
        if (t == null) {        // Root is null, assign new root
            t = new BSTNode<AnyType>(x);
            return t;
        }
        if (t.getElement().compareTo(x) > 0) {       // root is greater than x -> left subtree
            t.setLeft(insert(x, t.getLeft()));
        } else if (t.getElement().compareTo(x) < 0){     // root is less than x -> right subtree
            t.setRight(insert(x, t.getRight()));
        }
        return t;       // Return root of tree
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
    private BSTNode<AnyType> remove(AnyType x, BSTNode<AnyType> t ) {
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
            BSTNode<AnyType> min = findMin(t.getRight());      // find minimum of right subtree
            t.setElement(min.getElement());     // replace root's element with min in right subtree
            t.setRight(remove(min.getElement(), t.getRight()));     // remove min from root's right subtree
            // root is greater than x -> left subtree
        } else if (t.getElement().compareTo(x) > 0) {
            t.setLeft(remove(x, t.getLeft()));
            // root is less than x -> right subtree
        } else if (t.getElement().compareTo(x) < 0) {
            t.setRight(remove(x, t.getRight()));
        }
        return t;       //Return the root
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
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMax( root ).element;
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
     * Prints the tree on the screen.
     * @param t the root of the tree
     */
    private void printTree(BSTNode<AnyType> t) {
        if (t == null) { return;}
        printTree(t.getLeft());
        System.out.print(t.getElement().toString() + "\n");
        printTree(t.getRight());
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private BSTNode<AnyType> findMin(BSTNode<AnyType> t ) {
        if (t == null) { return t; }
        if (t.getLeft() == null) {      //if root does not have a left child, then root is the minimum -> return root
            return t;
        }
        return (findMin(t.getLeft()));      //there exists a left child, so findMin in left subtree
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private BSTNode<AnyType> findMax(BSTNode<AnyType> t ) {
        if (t.getRight() == null) {      //if root does not have a right child, then root is the maximum -> return root
            return t;
        }
        return (findMax(t.getRight()));      //there exists a right child, so findMax in right subtree
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains( AnyType x, BSTNode<AnyType> t ) {
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


    private static class BSTNode<AnyType> {
        // Constructors
        BSTNode( AnyType theElement ) {
            this( theElement, null, null );
        }

        BSTNode(AnyType theElement, BSTNode<AnyType> lt, BSTNode<AnyType> rt ) {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        AnyType element;         // The data in the node
        BSTNode<AnyType> left;   // Left child
        BSTNode<AnyType> right;  // Right child

        //Get and set value
        public AnyType getElement() { return element; }
        public void setElement(AnyType newValue) {this.element = newValue;}

        //Get and set left
        public BSTNode<AnyType> getLeft() {return left;}
        public void setLeft(BSTNode<AnyType> nextNode) {left = nextNode;}

        //Get and set right
        public BSTNode<AnyType> getRight() {return right;}
        public void setRight(BSTNode<AnyType> nextNode) {right = nextNode;}

        //Returns true if node is a leaf, otherwise false
        public boolean isLeaf() {
            return ((right == null) && (left == null));
        }
    }
}

