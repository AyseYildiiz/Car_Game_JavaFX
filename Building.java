/*Group Members:
 * Zehra ÖZLEN 150122065
 * Ayşe YILDIZ 150122015
 * Canan PAZARBAŞI 150123035 */
 
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class Building {
	//Color array for buildings 
    private static Color[] buildingColor = {Color.PURPLE, Color.SEAGREEN, Color.NAVY, Color.RED};

    //This method creates buildings and adds them to the pane
    public static void createBuilding(Pane p, int buildingType, int buildingRotation, int colorIndex, int cellX, int cellY) {
        Pane buildingPane = new Pane();
        Shape building = null;
        double buildingPaneX = cellX * Game.cellWidth;
        double buildingPaneY = cellY * Game.cellHeight;
        
        switch (buildingType) {
            case 0:
                building = new Rectangle(Game.cellWidth, 0, Game.cellWidth * 2, Game.cellHeight * 3);
                Rectangle bottom = new Rectangle(Game.cellWidth * 1.25, Game.cellWidth * 0.25, Game.cellWidth * 1.5, Game.cellHeight * 1.5);
                Rectangle top = new Rectangle(Game.cellWidth * 1.5, Game.cellWidth * 0.5, Game.cellWidth * 1.0, Game.cellHeight * 1);
                building.setStyle("-fx-stroke: black; -fx-fill: LIGHTCORAL; -fx-stroke-width: 2");
                bottom.setFill(buildingColor[colorIndex]);
                bottom.setStyle("-fx-stroke: black; -fx-stroke-width: 2");
                top.setFill(buildingColor[colorIndex]);
                top.setStyle("-fx-stroke: black; -fx-stroke-width: 2");
                buildingPane.getChildren().addAll(building, bottom, top);
                buildingPane.setRotate(-buildingRotation);
                break;
            case 1:
                building = new Rectangle(0, 0, Game.cellWidth * 2.01, Game.cellHeight * 3.02);
                Circle bot = new Circle(Game.cellWidth * 0.75);
                Circle upper = new Circle(Game.cellWidth * 0.50);
                bot.setCenterX(Game.cellWidth);
                bot.setCenterY(Game.cellHeight);
                upper.setCenterX(Game.cellWidth);
                upper.setCenterY(Game.cellHeight);
                building.setStyle("-fx-stroke: black; -fx-fill: LIGHTCORAL; -fx-stroke-width: 2");
                bot.setFill(buildingColor[colorIndex]);
                bot.setStyle("-fx-stroke: black; -fx-stroke-width: 2");
                upper.setFill(buildingColor[colorIndex]);
                upper.setStyle("-fx-stroke: black; -fx-stroke-width: 2");
                buildingPane.getChildren().addAll(building, bot, upper);
                buildingPane.setRotate(buildingRotation);
                break;
            case 2:
                building = new Rectangle(0, 0, Game.cellWidth, Game.cellHeight);
                building.setFill(buildingColor[colorIndex]);
                building.setStyle("-fx-stroke: black;-fx-stroke-width: 2");
                buildingPane.getChildren().addAll(building);
                buildingPane.setRotate(buildingRotation);

        }
        p.getChildren().add(buildingPane);
        buildingPane.setLayoutX(buildingPaneX);
        buildingPane.setLayoutY(buildingPaneY);
    }
}