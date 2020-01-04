package sample.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sample.BinaryTree;
import sample.HeapSort;
import sample.shape.Circle;
import sample.shape.Line;

import java.util.ListIterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraphicsTree extends Canvas {

    private BinaryTree tree;
    private int[] numbers;
    private volatile boolean sorted = false;
    private ScheduledExecutorService service;
    private ListIterator<HeapSort.Event> events;

    public GraphicsTree() {
        tree = new BinaryTree();
        service = AsyncServiceSupplier.get();
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void createTree(int[] numbers) {
        this.numbers = numbers;
        tree.insertItems(numbers);
        drawTree();
    }

    public boolean isInitialized() {
        return numbers != null;
    }

    public void stepByStepSort() {
        HeapSort.sort(numbers);
        events = HeapSort.events.listIterator();
    }

    public void continuousSort() {
        HeapSort.sort(numbers);

        if (service.isShutdown())
            service = AsyncServiceSupplier.get();

        service.execute(() -> {
            try {
                displayEvents();
            } catch (InterruptedException e) {
                System.err.println("interrupted");
            } finally {
                if (!service.isShutdown())
                    service.shutdown();
            }
        });
    }

    private void displayEvents() throws InterruptedException {
        events = HeapSort.events.listIterator();
        while (!sorted) {
            displayNextEvent(false);
        }
        events = null;
    }

    public void clearTree() {
        tree.clear();
    }

    public boolean previousEvent() {
        if (!events.hasPrevious()) {
            sorted = false;
            return true;
        }

        if (service.isShutdown())
            service = AsyncServiceSupplier.get();

        service.execute(() -> {
            try {
                displayEvent(events.previous());
            } catch (InterruptedException e) {
                System.err.println("interrupted");
            } finally {
                if (!service.isShutdown())
                    service.shutdown();
            }
        });

        return false;
    }

    public boolean nextEvent() {
        try {
            displayNextEvent(true);

            if (sorted && !service.isShutdown())
                service.shutdown();

            return sorted;
        } catch (InterruptedException e) {
            System.err.println("interrupted");
        }
        return false;
    }

    private void displayNextEvent(boolean async) throws InterruptedException {
        if (!events.hasNext()) {
            sorted = true;
            return;
        }

        if (async) {
            if (service.isShutdown())
                service = AsyncServiceSupplier.get();

            service.execute(() -> {
                try {
                    displayEvent(events.next());
                } catch (InterruptedException e) {
                    System.err.println("interrupted");
                }
            });
        } else {
            displayEvent(events.next());
        }

        if (!events.hasNext())
            sorted = true;
    }

    private void displayEvent(HeapSort.Event event) throws InterruptedException {
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
    }

    private void drawTree() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        if (tree.getRoot() != null) {
            int treeHeight = tree.getHeight(tree.getRoot());

            drawTree(gc, tree.getRoot(), 0, this.getWidth(), 0, this.getHeight() / treeHeight);

            drawCircles(gc, tree.getRoot(), 0, this.getWidth(), 0, this.getHeight() / treeHeight);
        }
    }

    private void drawTree(GraphicsContext gc, BinaryTree.TreeNode treeNode, double xMin, double xMax, double yMin, double yMax) {
        // If left node is not null then draw a line to it
        if (treeNode.getLeft() != null) {
            Line line = new Line((xMin + xMax) / 2, yMin + yMax / 2, (xMin + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);

            line.draw(gc);

            // Recurse left circle nodes
            drawTree(gc, treeNode.getLeft(), xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
        }

        // If right node is not null then draw a line to it
        if (treeNode.getRight() != null) {
            Line line = new Line((xMin + xMax) / 2, yMin + yMax / 2, (xMax + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);

            line.draw(gc);

            // Recurse right circle nodes
            drawTree(gc, treeNode.getRight(), (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
        }
    }

    private void drawCircles(GraphicsContext gc, BinaryTree.TreeNode treeNode, double xMin, double xMax, double yMin, double yMax) {
        Circle circle = treeNode.getCircle();

        circle.setCenterX((xMin + xMax) / 2);
        circle.setCenterY(yMin + yMax / 2);

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

}

