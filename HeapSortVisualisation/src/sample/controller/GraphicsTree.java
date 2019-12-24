package sample.controller;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sample.BinaryTree;
import sample.HeapSort;
import sample.shape.Circle;
import sample.shape.Line;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GraphicsTree extends Canvas {

    private BinaryTree tree;
    private int[] numbers;
    private AtomicBoolean sorted = new AtomicBoolean(false);

    public GraphicsTree() {
        tree = new BinaryTree();
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void createTree(int[] numbers) {
        this.numbers = numbers;
        tree.insertItems(numbers);
        drawTree();
    }

    public boolean isSorted() {
        return sorted.get();
    }

    public void continuousSort() {
        HeapSort.sort(numbers);

        CompletableFuture.runAsync(() -> {
            try {
                displayEvents();
                sorted.compareAndSet(false, true);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        });
    }

    private void displayEvents() throws InterruptedException {
        for (HeapSort.Event event : HeapSort.events) {
            int[] elems = event.getElems();
            HeapSort.EventType eventType = event.getEventType();

            int elem1 = elems[0];
            int elem2 = elems[1];

            Circle c1 = tree.retrieveItem(elem1);
            Circle c2 = tree.retrieveItem(elem2);

            c1.setHighlighter(true);
            c2.setHighlighter(true);

            c1.draw(getGraphicsContext2D());
            c2.draw(getGraphicsContext2D());

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));

            if (eventType == HeapSort.EventType.CHANGE) {
                int temp = c1.getKey();
                c1.setKey(c2.getKey());
                c2.setKey(temp);

                //TODO paint then red, signalling a change

                c1.draw(getGraphicsContext2D());
                c2.draw(getGraphicsContext2D());

                Thread.sleep(TimeUnit.SECONDS.toMillis(1));

                c1.setHighlighter(false);
                c2.setHighlighter(false);

                c1.draw(getGraphicsContext2D());
                c2.draw(getGraphicsContext2D());
            }

            c1.setHighlighter(false);
            c2.setHighlighter(false);

            c1.draw(getGraphicsContext2D());
            c2.draw(getGraphicsContext2D());
        }
    }

    private void drawTree() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // If the tree is not empty; draw the lines and circles
        if (tree.getRoot() != null) {
            int treeHeight = tree.getHeight(tree.getRoot());

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
        if (treeNode.getLeft() != null) {
            // Determine the start and end points of the line
            linePoint1 = new Point2D(((xMin + xMax) / 2), yMin + yMax / 2);
            linePoint2 = new Point2D(((xMin + (xMin + xMax) / 2) / 2), yMin + yMax + yMax / 2);
            newLine.setPoint(linePoint1, linePoint2);// Set the points
            newLine.draw(gc);// Draw the line

            // Recurse left circle nodes
            drawTree(gc, treeNode.getLeft(), xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
        }

        // If right node is not null then draw a line to it
        if (treeNode.getRight() != null) {
            // Determine the start and end points of the line
            linePoint1 = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
            linePoint2 = new Point2D((xMax + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);// Draw the line

            // Recurse right circle nodes
            drawTree(gc, treeNode.getRight(), (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
        }
    }

    private void drawCircles(GraphicsContext gc, BinaryTree.TreeNode treeNode, double xMin, double xMax, double yMin, double yMax) {
        Circle circle = treeNode.getCircle();
        Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);

        circle.setHighlighter(false);
        circle.setPoint(point);

        circle.draw(gc);

        if (treeNode.getLeft() != null) {
            drawCircles(gc, treeNode.getLeft(), xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
        }
        if (treeNode.getRight() != null) {
            drawCircles(gc, treeNode.getRight(), (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
        }
    }

    public void clearCanvas() {
        getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void clearTree() {
        tree.clear();
    }

    public void previousEvent() {

    }

    public void nextEvent() {

    }
}

