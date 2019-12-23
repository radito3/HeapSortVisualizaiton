package sample.controller;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sample.BinaryTree;
import sample.shape.Circle;
import sample.shape.Line;

public final class GraphicsTree extends Canvas {

    private BinaryTree tree;

    public GraphicsTree() {
        tree = new BinaryTree();
        widthProperty().addListener(evt -> drawTree());
        heightProperty().addListener(evt -> drawTree());
    }

    public void createTree(Integer[] numbers) {
        for (int number : numbers) {
            Circle circle = new Circle(number);
            tree.insertItem(circle);
        }
        balanceTree();
        drawTree();
    }

    private void balanceTree() {
        tree.balance();
    }

    public Circle search(int key) {
        Circle c = tree.retrieveItem(key);
        c.draw(getGraphicsContext2D()); //when searching only the circle needs to be drawn, not the whole tree
        return tree.retrieveItem(key);
    }

    private void drawTree() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // If the tree is not empty; draw the lines and circles
        if (tree.root != null) {
            int treeHeight = tree.getHeight(tree.root);

            // Get the tree height
            drawTree(gc, tree.getRoot(), 0, this.getWidth(), 0, this.getHeight() / treeHeight);
            drawCircles(gc, tree.getRoot(), 0, this.getWidth(), 0, this.getHeight() / treeHeight);
        }
    }

    private void drawTree(GraphicsContext gc, BinaryTree.TreeNode treeNode, double xMin, double xMax, double yMin, double yMax) {
        Point2D linePoint1; 	// Point_1
        Point2D linePoint2;   // Point_2
        Line newLine = new Line();  // Blank line

        // If left node is not null then draw a line to it
        if (treeNode.left != null) {
            // Determine the start and end points of the line
            linePoint1 = new Point2D(((xMin + xMax) / 2), yMin + yMax / 2);
            linePoint2 = new Point2D(((xMin + (xMin + xMax) / 2) / 2), yMin + yMax + yMax / 2);
            newLine.setPoint(linePoint1, linePoint2);// Set the points
            newLine.draw(gc);// Draw the line

            // Recurse left circle nodes
            drawTree(gc, treeNode.left, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
        }

        // If right node is not null then draw a line to it
        if (treeNode.right != null) {
            // Determine the start and end points of the line
            linePoint1 = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
            linePoint2 = new Point2D((xMax + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);// Draw the line

            // Recurse right circle nodes
            drawTree(gc, treeNode.right, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
        }
    }

    private void drawCircles(GraphicsContext gc, BinaryTree.TreeNode treeNode, double xMin, double xMax, double yMin, double yMax) {
        Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);

        treeNode.circle.setHighlighter(false);
        treeNode.circle.setPoint(point);

        treeNode.circle.draw(gc);

        if (treeNode.left != null) {
            drawCircles(gc, treeNode.left, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
        }
        if (treeNode.right != null) {
            drawCircles(gc, treeNode.right, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
        }
    }

    public void clearCanvas() {
        getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
    }
}

