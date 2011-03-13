import java.awt.geom.Point2D;

public class ShapeCoordinateTranslator {

    public static Point2D.Double toCenter(Point2D origin, int shapeWidth, int shapeHeight){
        return new Point2D.Double(origin.getX() - (shapeWidth / 2), origin.getY() - (shapeHeight / 2));
    }
}
