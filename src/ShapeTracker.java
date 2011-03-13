/*
 * Copyright 2009 Entero Corporation. All Rights Reserved.
 * www.entero.com
 */

import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/* Track rectangles and the lines between them, if any */

public class ShapeTracker extends MouseAdapter {
    private List<Rectangle2D> rectangles;
    private List<Line2D> lines;
    private int joinDistance;
    private List<Component> shapeListeners;
    private Rectangle2D movingRectangle;

    public ShapeTracker() {
        rectangles = new ArrayList<Rectangle2D>();
        shapeListeners = new ArrayList<Component>();
    }

    public List<Rectangle2D> getRectangles() {
        return rectangles;
    }

    public List<Line2D> getLines() {
        return lines;
    }

    public void setJoinDistance(int joinDistance) {
        this.joinDistance = joinDistance;
    }

    public void addRectangle(Rectangle2D rectangle) {
        rectangles.add(rectangle);

        calculateLines();
    }

    public void addListener(Component component) {
        shapeListeners.add(component);
    }

    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();

        shapes.addAll(rectangles);

        if (lines != null) {
            shapes.addAll(lines);
        }

        return shapes;
    }

    private void calculateLines() {
        lines = RectangleConnector.getConnectingLines(rectangles, joinDistance);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        Point2D origin = ShapeCoordinateTranslator.toCenter(e.getPoint(), Painter.SHAPE_WIDTH, Painter.SHAPE_HEIGHT);
        addRectangle(new Rectangle2D.Double(origin.getX(), origin.getY(), Painter.SHAPE_WIDTH, Painter.SHAPE_HEIGHT));

        repaintListeners();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        movingRectangle = getRectangle(e.getPoint());
        if (movingRectangle == null) {
            System.out.println("mousePressed (" + e.getPoint().x + "," + e.getPoint().y + "): null");
        } else {
            System.out.println("mousePressed (" + e.getPoint().x + "," + e.getPoint().y
                    + "): " + movingRectangle.getX()
                    + "," + movingRectangle.getY());
        }
    }

    private Rectangle2D getRectangle(Point point) {
        for (Rectangle2D rectangle : rectangles) {
            if (rectangle.contains(point)) {
                return rectangle;
            }
        }
        return null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        movingRectangle = null;
        //System.out.println("mouseReleased (" + e.getPoint().x + "," + e.getPoint().y + ")");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (movingRectangle != null) {
            Point2D origin = ShapeCoordinateTranslator.toCenter(e.getPoint(), Painter.SHAPE_WIDTH, Painter.SHAPE_HEIGHT);
            movingRectangle.setRect(origin.getX(), origin.getY(), Painter.SHAPE_WIDTH, Painter.SHAPE_HEIGHT);
        }

        calculateLines();

        repaintListeners();
    }

    private void repaintListeners() {
        for (Component component : shapeListeners) {
            component.repaint();
        }
    }
}
