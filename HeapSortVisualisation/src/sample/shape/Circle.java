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

    // The circle attributes
    private Point2D point;
    private Color backgroundColor;
    private Color borderColor;
    private Color fontColor;

    /**
     * Creates a circle object with methods for controlling it's point locale,
     * background color, border color, font color, search key.
     * @param key a <code>Integer</code> search key for searching and deleting within an index.
     */
    public Circle(int key) {
        super(RADIUS);
        this.key = key;
        this.backgroundColor = Color.web("#FCFCFC");
    }

    /**
     *
     * @param key a integer id number for searching and deleting from an index.
     * @param point a Cartesian coordinate using x and y float numbers.
     */
    public Circle(int key, Point2D point) {
        this.key = key;
        this.point = point;
        this.backgroundColor = Color.rgb(49, 116, 222);
        this.setBorderColor(Color.rgb(99, 99, 99));
        this.fontColor = Color.web("#FCFCFC");

    }

    /**
     * Draws the circle at a new position
     * @param gc The graphics object to use for drawing to a component
     */
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(3); // Sets the width of the lines

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
    /**
     * Get the search key number.
     * @return A integer of the circle index value.
     */
    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Gets the border color.
     * @return A color for the circle border
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the border color.
     * @param borderColor
     */
    private void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Gets the point coordinates.
     * @return
     */
    public Point2D getPoint() {
        return point;
    }

    /**
     * Sets the point coordinates.
     * @param point
     */
    public void setPoint(Point2D point) {
        this.point = point;
    }

    /**
     * Gets the background color.
     * @return
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color.
     * @param color
     */
    private void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * Gets the font color.
     * @return
     */
    public Color getFontColor() {
        return this.fontColor;
    }

    /**
     * Sets the font color.
     * @param fontColor
     */
    private void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Sets the circle to use highlighted colors if the parameter
     * is true.
     * @param highlight a boolean value for switching on/off highlighted colors
     */
    public void setHighlighter(boolean highlight) {
        if (highlight) {
            setFontColor(Color.rgb(49, 116, 222));
            setBackgroundColor(Color.rgb(155, 244, 167));
            setBorderColor(Color.rgb(49, 116, 222));
        } else {
            setFontColor(Color.web("#FCFCFC"));
            setBackgroundColor(Color.rgb(49, 116, 222));
            setBorderColor(Color.rgb(99, 99, 99));
        }
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

    @Override public String toString() {
        return "Circle{" + "key=" + key + ", point=" + point + '}';
    }
}
