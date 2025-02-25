import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Car  {
    
  //This method creates and returns the cars as a pane and adds them to the main pane
    public static Pane createCar(double x, double y) {
        Pane temp = new Pane();

        Rectangle rec = new Rectangle(x, y, Game.paneWidth / 60, Game.paneHeight / 70);
        rec.setFill(Color.PURPLE);

        double radius = Game.paneWidth / 250;
        Circle tire1 = new Circle(x + radius, y + rec.getHeight(), radius);
        tire1.setFill(Color.BLACK);
        Circle tire2 = new Circle(x + rec.getWidth() - radius, y + rec.getHeight(), radius);
        tire2.setFill(Color.BLACK);

        temp.getChildren().addAll(rec, tire1, tire2);
        

      return temp;
    }
}