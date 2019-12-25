package sample.shape;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

//TODO refactor
public class Circle extends javafx.scene.shape.Circle {

    private final Font font =  Font.font("Cooper Black", FontWeight.BOLD, 16);
    private final FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);

    private static final int RADIUS = 26;

    private int key;
    private Point2D point;
    private Color backgroundColor;
    private Color borderColor;
    private Color fontColor;

    public Circle(int key) {
        super(RADIUS);
        this.key = key;
        setDefaultColorScheme();
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(3);

        // Create a circle
        gc.setFill(backgroundColor);
        gc.fillOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);

        // Outline the circle border
        gc.setStroke(borderColor);
        gc.strokeOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);

        // Draw the id number inside the circle
        gc.setFont(font);
        gc.setFill(getFontColor());
        gc.fillText(getKeyAsString(),
                    point.getX() - (computeStringWidth() / 2),
                    point.getY() + (fm.getAscent() / 4));
    }

    private double computeStringWidth() {
        double res = 0;
        for (char ch : getKeyAsString().toCharArray()) {
            res += fm.getCharWidth(ch);
        }
        return res;
    }

    private String getKeyAsString() {
        return Integer.toString(getKey());
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    private void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    private void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    private void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public void setDefaultColorScheme() {
        setFontColor(Color.web("#FCFCFC"));
        setBackgroundColor(Color.rgb(49, 116, 222));
        setBorderColor(Color.rgb(99, 99, 99));
    }

    public void setComparisonColorScheme() {
        setFontColor(Color.rgb(49, 116, 222));
        setBackgroundColor(Color.rgb(155, 244, 167));
        setBorderColor(Color.rgb(49, 116, 222));
    }

    public void setChangeColorScheme() {
        setFontColor(Color.rgb(255, 255, 255));
        setBackgroundColor(Color.rgb(150, 0, 150));
        setBorderColor(Color.rgb(49, 116, 222));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Circle circle = (Circle) o;
        return key == circle.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Circle{" + "key=" + key + ", point=" + point + '}';
    }
}
