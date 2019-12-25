package sample.controller;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sample.BinaryTree;
import sample.HeapSort;
import sample.shape.Circle;
import sample.shape.Line;

import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GraphicsTree extends Canvas {

    private BinaryTree tree;
    private int[] numbers;
    private AtomicBoolean sorted = new AtomicBoolean(false);
    private ListIterator<HeapSort.Event> events;

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

    public boolean stepByStepSort() {
        if (numbers != null) {
            HeapSort.sort(numbers);
            events = HeapSort.events.listIterator();
            return true;
        }
        return false;
    }

    public void continuousSort() {
        HeapSort.sort(numbers);

        CompletableFuture.runAsync(() -> {
            try {
                displayEvents();
                sorted.set(true);
            } catch (InterruptedException e) {
                System.err.println("interrupted");
            }
        });
    }

    private void displayEvents() throws InterruptedException {
        events = HeapSort.events.listIterator();
        while (displayNextEvent()) ;
        events = null;
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
        Point2D linePoint1;
        Point2D linePoint2;
        Line newLine = new Line();

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

    public boolean previousEvent() throws InterruptedException {
        if (!events.hasPrevious())
            return false;

        HeapSort.Event event = events.previous();
        GraphicsContext gc = getGraphicsContext2D();

        int[] elems = event.getElems();
        HeapSort.EventType eventType = event.getEventType();

        int elem1 = elems[0];
        int elem2 = elems[1];

        Circle c1 = tree.retrieveItem(elem1);
        Circle c2 = tree.retrieveItem(elem2);

        c1.setComparisonColorScheme();
        c2.setComparisonColorScheme();

        c1.draw(gc);
        c2.draw(gc);

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        if (eventType == HeapSort.EventType.CHANGE) {
            int temp = c1.getKey();
            c1.setKey(c2.getKey());
            c2.setKey(temp);

            c1.setChangeColorScheme();
            c2.setChangeColorScheme();

            c1.draw(gc);
            c2.draw(gc);

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }

        c1.setDefaultColorScheme();
        c2.setDefaultColorScheme();

        c1.draw(gc);
        c2.draw(gc);

        return true;
    }

    public boolean nextEvent() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return displayNextEvent();
            } catch (InterruptedException e) {
                System.err.println("interrupted");
            }
            return false;
        })
        .join();
    }

    private boolean displayNextEvent() throws InterruptedException {
        if (!events.hasNext())
            return false;

        HeapSort.Event event = events.next();
        GraphicsContext gc = getGraphicsContext2D();

        int[] elems = event.getElems();
        HeapSort.EventType eventType = event.getEventType();

        int elem1 = elems[0];
        int elem2 = elems[1];

        Circle c1 = tree.retrieveItem(elem1);
        Circle c2 = tree.retrieveItem(elem2);

        c1.setComparisonColorScheme();
        c2.setComparisonColorScheme();

        c1.draw(gc);
        c2.draw(gc);

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        if (eventType == HeapSort.EventType.CHANGE) {
            int temp = c1.getKey();
            c1.setKey(c2.getKey());
            c2.setKey(temp);

            c1.setChangeColorScheme();
            c2.setChangeColorScheme();

            c1.draw(gc);
            c2.draw(gc);

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }

        c1.setDefaultColorScheme();
        c2.setDefaultColorScheme();

        c1.draw(gc);
        c2.draw(gc);

        if (!events.hasNext())
            sorted.set(true);

        return true;
    }
}

