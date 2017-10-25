//Java program for insertion and deletion in AVl tree
import java.util.*;
import java.lang.*;

class Node
{
	Node left, right;
	int key, height;

	Node(int d) //constructor
	{
		key = d;
		height = 1; //implies height of leaf = 1
		left = null;
		right = null;
	}
}

public class AVLTree
{
	Node root;

	//utility function to find height of a node
	int height(Node N)
	{
		if(N == null)
			return 0;
		else
			return N.height;
	}

	// A utility function to get maximum of two integers
	int max(int a, int b)
	{
		return (a>b) ? a : b;
	}

	int getBalance(Node N)
	{
		if(N == null)
			return 0;

		return height(N.left)-height(N.right); 	
	}

	// A utility function to right rotate subtree rooted with y
	Node rightRotate(Node y)
	{
		//link pointers defined
		Node x = y.left;
		Node T2 = x.right; 

		//rotations
		x.right = y;
		y.left = T2;  // or simply y.left = x.right

		//update heights
		x.height = max(height(x.left), height(x.right)) +1;
		y.height = max(height(y.left), height(y.right)) +1; 

		//returning new root
		return x;

	}

	Node leftRotate(Node x)
	{
		//link pointers defined
		Node y = x.right;
		Node T2 = y.left;

		//rotations
		y.left = x;
		x.right = T2;

		//updating heights
		x.height = max(height(x.left), height(x.right)) +1;
		y.height = max(height(y.left), height(y.right)) +1; 

		//returning new root
		return y;

	}

	Node insert(Node root, int key)
	{
		/* 1.  Perform the normal BST insertion */
		if(root == null)
			return (new Node(key)); //calling the constructor

		if(key < root.key)
			root.left = insert(root.left, key);

		else if(key > root.key)
			root.right = insert(root.right, key); 

		else // Duplicate keys not allowed
			return root;

		/* 2. Update height of this ancestor node */
		root.height = 1 + max(height(root.left), height(root.right)) ;	

		int balance = getBalance(root);

		// If this node becomes unbalanced, then there are 4 cases: 

		//# Left Left Case
		if(balance > 1 && key < root.left.key)
			return rightRotate(root);

		//# Right Right Case
		if(balance < -1 && key > root.right.key)
			return leftRotate(root);

		//# Left Right Case
		if(balance > 1 && key > root.left.key)
		{
			root.left = leftRotate(root.left);
			return rightRotate(root);
		}

		//# Right Left Case
		if(balance < -1 && key < root.right.key)
		{
			root.right = rightRotate(root.right);
			return leftRotate(root);
		}
		/* return the (unchanged) root pointer */
		return root;
 	}

	Node findInOrderPreDecessor(Node N)
    {
        Node current = N;
        while(current.right != null)
            current = current.right;

        return current;

    }

    //deleting a node in AVL tree
    Node delete(Node root, int key)
    {
        // standard seach in BST
        if(root == null)
            return root;

        if(key < root.key)
            root.left = delete(root.left, key);

        if(key > root.key)       
            root.right = delete(root.right, key);

        //if key is found
        else
        {
                //case 1 and 2: node with 0 or 1 child
            if((root.left == null) || (root.right == null))
            {
                Node temp = null;

                if(temp == root.left)
                    temp = root.right;

                else 
                    temp = root.left;

                //no child 
                if(temp == null)
                {
                    temp = root;
                    root = null;
                }

                //one child
                else
                    root = temp;

            }

            //two children case
            else
            {
                Node temp = findInOrderPreDecessor(root.left);
                root.key = temp.key ;
                root.left = delete(root.left, temp.key);
            }

        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = 1+ max(height(root.left), height(root.right));
 
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        //  this node became unbalanced)
        int balance = getBalance(root);
 
        // If this node becomes unbalanced, then there are 4 cases: 
        // #Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
 
        // #Left Right Case
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
 
        // #Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
 
        // #Right Left Case
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
 
        return root;
        
    }

    //preOrder Traversal of tree
 	void preOrder(Node node) 
 	{
        if (node != null) 
        {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }
 
    public static void main(String[] args) 
    {
        AVLTree tree = new AVLTree();
 
        /* Constructing tree given in the below figure */
        tree.root = tree.insert(tree.root, 10);
        tree.root = tree.insert(tree.root, 20);
        tree.root = tree.insert(tree.root, 30);
        tree.root = tree.insert(tree.root, 40);
        tree.root = tree.insert(tree.root, 50);
        tree.root = tree.insert(tree.root, 25);
 
        /* The constructed AVL Tree would be
             30
            /  \
          20   40
         /  \     \
        10  25    50
        */

        //verifying by printing preOrder traversal
        System.out.println("Preorder traversal of constructed tree is : ");
        tree.preOrder(tree.root);
        System.out.println();

        tree.root = tree.delete(tree.root, 30);
        System.out.println("Preorder traversal of constructed tree after deletion is : ");
        tree.preOrder(tree.root);
        System.out.println();
    }
}