package sample;

import sample.shape.Circle;

import java.util.NoSuchElementException;
import java.util.Objects;

public class BinaryTree {

    public TreeNode root;

    public BinaryTree() {
        root = null;
    }

    public TreeNode getRoot() {
        if (root == null) {
            throw new RuntimeException("Empty Tree");
        }
        return root;
    }

    public void insertItem(Circle newCircle) {
        root = insertItem(root, newCircle);
    }

    private TreeNode insertItem(TreeNode tNode, Circle newCircle) {
        if (tNode == null) {
            tNode = new TreeNode(newCircle);
            return tNode;
        }
        Circle nodeItem = tNode.circle;

        int cmp = Integer.compare(newCircle.getKey(), nodeItem.getKey());
        if (cmp > 0) {
            tNode.right = insertItem(tNode.right, newCircle);
        } else if (cmp < 0) {
            tNode.left = insertItem(tNode.left, newCircle);
        }
        return tNode;
    }

    public void balance() {
        int[] elems = new int[] {}; //get the elements
        int n; //middle element to be set as root of tree
        if (elems.length % 2 != 0) {
            n = (elems.length / 2) + 1;
        } else {
            n = elems.length / 2;
        }
        //TODO rebalance so that each node has a left and right leaf node
    }

    public Circle retrieveItem(int searchKey) {
        return retrieveItem(root, searchKey);
    }

    private Circle retrieveItem(TreeNode tNode, int searchKey) {
        Circle treeItem;
        int cmp = Integer.compare(searchKey, tNode.circle.getKey());

        if (cmp == 0) {
            treeItem = tNode.circle;
        } else if (cmp < 0) {
            treeItem = retrieveItem(tNode.left, searchKey);
        } else {
            treeItem = retrieveItem(tNode.right, searchKey);
        }

        return treeItem;
    }

    /**
     * Deletes a circle from the tree.
     * @param searchKey a unique identifying value
     */
    public void deleteItem(Integer searchKey) throws NoSuchElementException {
        root = deleteItem(root, searchKey);
    }

    /**
     * Deletes a circle from the tree.
     * @param tNode a tree node
     * @param searchKey a unique identifying value
     * @return A tree.TreeNode from within the tree
     * @Overload deleteItem()
     */
    protected TreeNode deleteItem(TreeNode tNode, Integer searchKey) {
        TreeNode newSubtree;

        if (tNode == null) {
            throw new NoSuchElementException();
        }

        Circle nodeItem = tNode.circle;
        if (Objects.equals(searchKey, nodeItem.getKey())) {
            tNode = deleteNode(tNode);

        } else if (searchKey < nodeItem.getKey()) {
            newSubtree = deleteItem(tNode.left, searchKey);
            tNode.left = newSubtree;
        } else {
            newSubtree = deleteItem(tNode.right, searchKey);
            tNode.right = newSubtree;
        }

        return tNode;
    }

    /**
     * Helper method finds and replaces a deleted node.
     * @param tNode A tree.TreeNode from within the tree
     * @return A tree.TreeNode from within the tree
     */
    private TreeNode deleteNode(TreeNode tNode) {
        if ((tNode.left == null) && (tNode.right == null)) {
            return null;
        } else if (tNode.left == null) {
            return tNode.right;
        } else if (tNode.right == null) {
            return tNode.left;
        } else {
            tNode.circle = findLeftmost(tNode.right);
            tNode.right = deleteLeftmost(tNode.right);
            return tNode;
        }
    }

    private Circle findLeftmost(TreeNode tNode) {
        if (tNode.left == null) {
            return tNode.circle;
        }
        return findLeftmost(tNode.left);
    }

    private TreeNode deleteLeftmost(TreeNode tNode) {
        if (tNode.left == null) {
            return tNode.right;
        }
        tNode.left = deleteLeftmost(tNode.left);
        return tNode;
    }

    public int getHeight(TreeNode root) {
        if (root == null)
            return 0;
        return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
    }

    public int getSize(TreeNode root) {
        if (root == null)
            return 0;
        return (getSize(root.left) + getSize(root.right)) + 1;
    }

    public static class TreeNode {
        public Circle circle;
        public TreeNode left;
        public TreeNode right;

        TreeNode(Circle circle) {
            this.circle = circle;
            this.left = null;
            this.right = null;
        }
    }
}
