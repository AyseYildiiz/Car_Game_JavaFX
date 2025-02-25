import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RoadTile {

	//This method creates roads and adds them to the pane
    public static void createRoadTile(Pane p, int roadType, int roadRotation, int cellX, int cellY) {
        Shape road = null;
        Pane roadPane = new Pane();
        double roadPaneX = cellX * Game.cellWidth;
        double roadPaneY = cellY * Game.cellHeight;
        roadPane.setPrefSize(Game.cellWidth, Game.cellHeight);

        switch (roadType) {
            case 0:
                road = new Rectangle(0, 4, Game.cellWidth, Game.cellHeight - 8);
                road.setFill(Color.BISQUE);
                roadPane.getChildren().add(road);
                roadPane.setRotate(-roadRotation);
                break;
            case 1:
                road = new Arc(0, Game.cellHeight, Game.cellHeight - 4, Game.cellHeight - 4, 0, 90);
                ((Arc) road).setType(ArcType.ROUND);
                road.setFill(Color.BISQUE);
                Arc corner = new Arc(0, Game.cellHeight, 4, 4, 0, 90);
                corner.setFill(Color.TEAL);
                corner.setType(ArcType.ROUND);
                roadPane.getChildren().addAll(road, corner);
                roadPane.setRotate(-roadRotation);
                break;
            case 2:
                road = new Rectangle(0, 0, Game.cellWidth, Game.cellHeight);
                road.setFill(Color.BISQUE);
                roadPane.setRotate(-roadRotation);
                Rectangle[] corners = {
                        new Rectangle(0, 0, 4, 4),
                        new Rectangle(0, Game.cellWidth - 4, 4, 4),
                        new Rectangle(Game.cellHeight - 4, 0, 4, 4),
                        new Rectangle(Game.cellWidth - 4, Game.cellHeight - 4, 4, 4)
                };
                roadPane.getChildren().add(road);
                for (Rectangle c : corners) {
                    c.setFill(Color.TEAL);
                    roadPane.getChildren().add(c);
                }
                roadPane.setRotate(-roadRotation);
                break;
            case 3:
                road = new Rectangle(0, 4, Game.cellWidth, Game.cellHeight - 4);
                road.setFill(Color.BISQUE);
                Rectangle cor1 = new Rectangle(0, Game.cellHeight - 4, 4, 4);
                Rectangle cor2 = new Rectangle(Game.cellWidth - 4, Game.cellHeight - 4, 4, 4);

                cor1.setFill(Color.TEAL);
                cor2.setFill(Color.TEAL);
                roadPane.getChildren().addAll(road, cor1, cor2);
                roadPane.setRotate(roadRotation);

                break;
        }
      p.getChildren().add(roadPane);
       roadPane.setLayoutX(roadPaneX);
       roadPane.setLayoutY(roadPaneY);
    }
}