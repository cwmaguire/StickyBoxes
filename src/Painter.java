
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Painter extends JComponent {
    public static final int SHAPE_WIDTH = 40;
    public static final int SHAPE_HEIGHT = 40;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int BOX_SIZE = 40;

    private ShapeTracker shapeTracker;

    public Painter() {
        setFocusable(true);

        shapeTracker = new ShapeTracker();
        shapeTracker.setJoinDistance(BOX_SIZE);
        shapeTracker.addListener(this);
        this.addMouseListener(shapeTracker);
        this.addMouseMotionListener(shapeTracker);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        paintShapes(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    private void paintShapes(Graphics2D g2){
        g2.setColor(Color.BLUE);
        for(Shape shape : shapeTracker.getShapes()){
            g2.draw(shape);
        }
    }

}
