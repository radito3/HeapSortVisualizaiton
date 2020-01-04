package sample;

import sample.shape.Circle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Objects;

public class BinaryTree {

    private TreeNode root;
    private List<TreeNode> nodes = new ArrayList<>();

    public TreeNode getRoot() {
        return root;
    }

    public void insertItems(int[] nums) {
        if (nums.length == 0)
            return;

        int i = 0;
        root = new TreeNode(new Circle(nums[i++]));
        nodes.add(root);
        if (i + 1 >= nums.length) {
            return;
        }

        Queue<TreeNode> levelOneQueue = new LinkedList<>();
        Queue<TreeNode> levelTwoQueue = new LinkedList<>();

        root.left = new TreeNode(new Circle(nums[i++]));
        root.right = new TreeNode(new Circle(nums[i++]));
        levelOneQueue.add(root.left);
        levelOneQueue.add(root.right);
        nodes.add(root.left);
        nodes.add(root.right);

        for (;;) {
            for (TreeNode t : levelOneQueue) {
                if (i == nums.length)
                    return;
                t.left = new TreeNode(new Circle(nums[i++]));
                nodes.add(t.left);
                if (i == nums.length)
                    return;
                t.right = new TreeNode(new Circle(nums[i++]));
                nodes.add(t.right);
                levelTwoQueue.add(t.left);
                levelTwoQueue.add(t.right);
            }
            levelOneQueue.clear();

            for (TreeNode t : levelTwoQueue) {
                if (i == nums.length)
                    return;
                t.left = new TreeNode(new Circle(nums[i++]));
                nodes.add(t.left);
                if (i == nums.length)
                    return;
                t.right = new TreeNode(new Circle(nums[i++]));
                nodes.add(t.right);
                levelOneQueue.add(t.left);
                levelOneQueue.add(t.right);
            }
            levelTwoQueue.clear();
        }
    }

    public Circle retrieveItem(int searchKey) {
        return nodes.get(nodes.indexOf(new TreeNode(new Circle(searchKey)))).circle;
    }

    public void clear() {
        root = null;
        nodes.clear();
    }

    public int getHeight(TreeNode root) {
        if (root == null)
            return 0;
        return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
    }

    public static class TreeNode {
        Circle circle;
        TreeNode left;
        TreeNode right;

        TreeNode(Circle circle) {
            this.circle = circle;
        }

        public Circle getCircle() {
            return circle;
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TreeNode treeNode = (TreeNode) o;
            return Objects.equals(circle, treeNode.circle);
        }

        @Override
        public int hashCode() {
            return Objects.hash(circle);
        }

        @Override
        public String toString() {
            return "TreeNode{" + "circle=" + circle + '}';
        }
    }
}
