package sample.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends javafx.scene.shape.Line {

    private static final Color GRAY = Color.rgb(99, 99, 99);

    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(4);
        gc.setStroke(GRAY);
        gc.strokeLine(getStartX(), getStartY(), getEndX(), getEndY());
    }
}
