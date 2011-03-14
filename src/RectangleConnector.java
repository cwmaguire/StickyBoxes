import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class RectangleConnector {
    public static List<Line2D> getConnectingLines(List<Rectangle2D> rectangles, int range){
        List<Line2D> lines = new ArrayList<Line2D>();

        Map<Rectangle2D, Rectangle2D> rectanglePairs = new HashMap<Rectangle2D, Rectangle2D>();

        for (Rectangle2D rectangle1 : rectangles) {
            for (Rectangle2D rectangle2 : rectangles) {
                if (rectangle1 != rectangle2
                        && !areRectanglesConnected(rectanglePairs, rectangle1, rectangle2)
                        && areRectanglesWithinRange(rectangle1, rectangle2, range)) {
                    lines.add(createConnectingLine(rectangle1, rectangle2));
                    rectanglePairs.put(rectangle1, rectangle2);
                }
            }
        }

        return lines;
    }

    private static boolean areRectanglesConnected(Map<Rectangle2D, Rectangle2D> rectanglePairs, Rectangle2D rectangle1, Rectangle2D rectangle2) {
        return rectangle1 == rectanglePairs.get(rectangle2) || rectangle2 == rectanglePairs.get(rectangle1);
    }

    private static boolean areRectanglesWithinRange(Rectangle2D rectangle1, Rectangle2D rectangle2, int range){
        Rectangle2D rangeRectangle = createRangeRectangle(rectangle1, range);
        return rangeRectangle.intersects(rectangle2);
    }

    private static Line2D createConnectingLine(Rectangle2D rectangle1, Rectangle2D rectangle2){
        Point2D coefficients = translateAngleToCoefficients(new Point2D.Double(rectangle1.getX(), rectangle1.getY()), new Point2D.Double(rectangle2.getX(), rectangle2.getY()));
        Point2D point1 = getRectanglePoint(rectangle1, coefficients);
        Point2D point2 = getRectanglePoint(rectangle2, reverseCoefficients(coefficients));
        return new Line2D.Double(point1, point2);
    }

    private static Rectangle2D createRangeRectangle(Rectangle2D rectangle, int range){
        return new Rectangle2D.Double(rectangle.getX() - range, rectangle.getY() - range, rectangle.getWidth() + (range * 2), rectangle.getHeight() + (range * 2));
    }

    private static Point2D translateAngleToCoefficients(Point2D point1, Point2D point2){
        Double quadrantBaseAngle = getQuadrantBaseAngle(point1, point2);
        Double angle = getAngle(point1, point2);
 
        // As the rectangles approach vertical, the angle approaches PI/2 radians (i.e. 90 degrees)
        // as they approach horizontal the angle approaches 0 radians/degrees
        // So if we're in the 1st or 3rd quandrants the angle is going to _decrease_ as we rotate
        // closer to the 2nd and 4th quadrants; that means we have to "invert" the angle, e.g. 
        // (PI/2 - angle) to get the angle that we need to add to the base. 
        if((quadrantBaseAngle % Math.PI) == 0){
            angle = Math.PI / 2 - angle; 
        }

        return getCoefficientsForAngle(quadrantBaseAngle + angle);
    }

    private static Point2D getRectanglePoint(Rectangle2D rectangle, Point2D coefficients){
        return new Point2D.Double(rectangle.getX() + (rectangle.getWidth() / 2 * coefficients.getX()),
                                  rectangle.getY() + (rectangle.getHeight() / 2 * coefficients.getY()));
    }

    private static Point2D reverseCoefficients(Point2D coefficients){
        return new Point2D.Double(Math.abs(coefficients.getX() - 2), Math.abs(coefficients.getY() - 2));
    }

    private static Double getAngle(Point2D point1, Point2D point2){
        Double rise = getRise(point1, point2);
        Double run = getRun(point1, point2);
        if(run.compareTo(0d) == 0){
            return Math.PI / 2;
        }else if(rise.compareTo(0d) == 0){
            return 0.0;
        }
        Double slope = rise/run;
        Double angle = Math.atan(slope);
        
        return Math.atan(slope);
    }

    private static Double getRise(Point2D point1, Point2D point2){
        return Math.abs(point1.getY() - point2.getY());
    }

    private static Double getRun(Point2D point1, Point2D point2) {
        return Math.abs(point1.getX() - point2.getX());
    }

    private static Point2D getCoefficientsForAngle(Double angle){
                
        // add 22.5 degrees so that the angle fits the calc below
        angle = (angle +  Math.PI / 8) % (Math.PI * 2);

        // Think (angle / (PI / 4)) then  multiply by the reciprocal: angle * (4 / PI)
        int quadrant = (int) (angle * (4 / Math.PI));

        switch(quadrant){
            case 0:
                return new Point2D.Double(1, 0);
            case 1:
                return new Point2D.Double(2, 0);
            case 2:
                return new Point2D.Double(2, 1);
            case 3:
                return new Point2D.Double(2, 2);
            case 4:
                return new Point2D.Double(1, 2);
            case 5:
                return new Point2D.Double(0, 2);
            case 6:
                return new Point2D.Double(0, 1);
            case 7:
                return new Point2D.Double(0, 0);
            default:
                return new Point2D.Double(1, 0); 
        }
    }

    private static Double getQuadrantBaseAngle(Point2D point1, Point2D point2){
        //return ((int) (getAngle(point1, point2) * 2 / Math.PI)) * Math.PI / 2;
        int quadrant;

        if(point1.getX() < point2.getX()){
            if(point1.getY() > point2.getY()){
                quadrant = 0;
            }else{
                quadrant = 1;
            }
        }else{
            if (point1.getY() > point2.getY()) {
                quadrant = 3;
            } else {
                quadrant = 2;
            }
        }

        //System.out.println("Quadrant = " + quadrant);
        return (Math.PI / 2) * quadrant;

    }
}
