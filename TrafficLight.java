import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TrafficLight {
	
	public static boolean isRed= true;
	//List for traffic lights
	public static List<Circle> trafficLights = new ArrayList<>();
	//This method creates traffic lights and adds them to the pane
	public static void createLight(Pane p, double x1, double y1, double x2, double y2) {
	    Line line = new Line(x1, y1, x2, y2);
	    line.setStroke(Color.BLACK);
	    p.getChildren().add(line);

	    Circle light = new Circle((x1 + x2) / 2, (y1 + y2) / 2, 5);
	    light.setFill(Color.RED);
	    //When clicked, the color of the traffic light changes
	    light.setOnMouseClicked(event -> {
	        if (light.getFill() == Color.RED) {
	            light.setFill(Color.GREEN);
	            isRed = false;
	        } else {
	            light.setFill(Color.RED);
	            isRed = true;
	        }
	    });
	
	    p.getChildren().add(light);
	    trafficLights.add(light);
	}

}