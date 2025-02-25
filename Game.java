import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game extends Application {
	private Pane mainPane;
	private int winCount = 0; 
	private int requiredWin = 0; 
	private Label winLabel;
	public static double paneWidth;
	public static double paneHeight;
	public static int rows;
	public static int columns;
	public static double cellWidth;
	public static double cellHeight;
	private List<PathTransition> transitionList = new ArrayList<>();
	private int collisionCount = 0;
	private Label collisionLabel;
	private int maxCollision = 0;
	private Label gameOverLabel;
	private int currentLev = 1; 


	@Override
	public void start(Stage primaryStage) {
		//This is main pane to add all the items
		mainPane = new Pane(); 

		StackPane root = new StackPane();
		Scene scene = new Scene(root, 800, 800);

		// Adds cover photo
		Image coverImage = new Image("car.jpg");
		ImageView coverImageView = new ImageView(coverImage);
		coverImageView.setFitWidth(scene.getWidth());
		coverImageView.setFitHeight(scene.getHeight());
		root.getChildren().add(coverImageView);

		//Creates button to start game 
		Button startButton = new Button("Start");
		startButton.setLayoutX(80);
		startButton.setLayoutY(70);
		startButton.setTextFill(Color.NAVY);
		startButton.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 25));
		startButton.setOnAction(event -> {
			VBox levelButtons = createLevelButtons(mainPane, coverImageView);
			root.getChildren().setAll(mainPane, coverImageView, levelButtons);
		});
        
		root.getChildren().addAll(startButton);
		primaryStage.setTitle("TRAFFIC CONTROL SIMULATOR");
	    primaryStage.setScene(scene);
		primaryStage.show();
	}
	//VBox for level buttons 
	private VBox createLevelButtons(Pane mainPane, ImageView coverImageView) {
		VBox levelButtons = new VBox(10);
		levelButtons.setTranslateY(50);

		for (int i = 1; i <= 5; i++) {
			int level = i;
			Button levelButton = new Button("Level " + level);
			levelButton.setLayoutX(80);
			levelButton.setLayoutY(70);
			levelButton.setTextFill(Color.NAVY);
			levelButton.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
			levelButton.setOnAction(event -> loadLevel( level , mainPane, coverImageView, levelButtons));			
			levelButtons.getChildren().add(levelButton);
		}

		// Exit button
		Button exitButton = new Button("Exit");
		exitButton.setLayoutX(80);
		exitButton.setLayoutY(70);
		exitButton.setTextFill(Color.NAVY);
		exitButton.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
		exitButton.setOnAction(event -> exit());
		levelButtons.getChildren().add(exitButton);
		return levelButtons;
	}

	//Loads levels from the input file, creates cells and other buttons
	private void loadLevel(int lev, Pane mainPane, ImageView coverImageView, VBox levelButtons) {
		currentLev=lev;
		String levelfile = "level"+lev +".txt";
		File level = new File(levelfile);
		coverImageView.setVisible(false);
		mainPane.setVisible(true);
		levelButtons.setVisible(false);
		mainPane.getChildren().clear();
		winCount = 0;
		requiredWin = 0;
		collisionCount = 0;
		maxCollision = 0;
		transitionList.clear();
		winLabel = null;
		collisionLabel = null;
		gameOverLabel = null;

		//Reads the file and create cells
		try  {
			Scanner input = new Scanner(level);
			String com = input.nextLine();
			createObject(com, mainPane);
			cellWidth = paneWidth / columns;
			cellHeight = paneHeight / rows;

			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < columns; col++) {
					Rectangle cell = new Rectangle(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
					cell.setStroke(Color.BLACK);
					cell.setFill(Color.TEAL);
					Text text1 = new Text(85, 30, "Score: ");
					text1.setFont(Font.font("Courier", FontWeight.BOLD,
							FontPosture.ITALIC, 25));
					Text text2 = new Text(85, 60, "Crashes: ");
					text2.setFont(Font.font("Courier", FontWeight.BOLD,
							FontPosture.ITALIC, 25));
					mainPane.getChildren().add(cell);
					mainPane.getChildren().add(text1);
					mainPane.getChildren().add(text2);
				}
			}
			//Creates back button
			Button backButton = new Button("Back");
			backButton.setTextFill(Color.NAVY);
			backButton.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
			backButton.setLayoutX(paneWidth - 60);
			backButton.setOnAction(event -> back(mainPane, coverImageView, levelButtons));
			mainPane.getChildren().add(backButton);
			//Creates next level button 
			Button nextLevelButton = new Button("Next Level");
			nextLevelButton.setLayoutX(80);
			nextLevelButton.setLayoutY(70);
			nextLevelButton.setTextFill(Color.NAVY);
			nextLevelButton.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
			nextLevelButton.setOnAction(event -> loadNextLevel(mainPane, coverImageView, levelButtons));
			mainPane.getChildren().add(nextLevelButton);

			while (input.hasNextLine()) {
				String s = input.nextLine();
				createObject(s, mainPane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createTraffic(mainPane, coverImageView, levelButtons);
	}
	//Sends back to level selection page
		private void back(Pane mainPane, ImageView coverImageView, VBox levelButtons) {
			coverImageView.setVisible(true);
			mainPane.setVisible(false);
			levelButtons.setVisible(true);
			mainPane.getChildren().clear();
		}


	//According to the data in the file creates objects 
	private void createObject(String com, Pane mainPane) {
		String[] words = com.split(" ");

		switch (words[0]) {
		case "Metadata":
			paneWidth = Double.parseDouble(words[1]);
			paneHeight = Double.parseDouble(words[2]);
			rows = Integer.parseInt(words[3]);
			columns = Integer.parseInt(words[4]);
			requiredWin = Integer.parseInt(words[6]);
			maxCollision = Integer.parseInt(words[7]);
			// requiredWinCount'u double olarak al
			break;
		case "RoadTile":
			int roadType = Integer.parseInt(words[1]);
			int roadRotation = Integer.parseInt(words[2]);
			int cellX = Integer.parseInt(words[3]);
			int cellY = Integer.parseInt(words[4]);
			RoadTile.createRoadTile(mainPane, roadType, roadRotation, cellX, cellY);
			break;
		case "Building":
			int buildingType = Integer.parseInt(words[1]);
			int buildingRotation = Integer.parseInt(words[2]);
			int colorIndex = Integer.parseInt(words[3]);
			int buildingCellX = Integer.parseInt(words[4]);
			int buildingCellY = Integer.parseInt(words[5]);
			Building.createBuilding(mainPane, buildingType, buildingRotation, colorIndex, buildingCellX, buildingCellY);
			break;
		case "TrafficLight":
			double x1 = Double.parseDouble(words[1]);
			double y1 = Double.parseDouble(words[2]);
			double x2 = Double.parseDouble(words[3]);
			double y2 = Double.parseDouble(words[4]);
			TrafficLight.createLight(mainPane, x1, y1, x2, y2);
			break;
		case "Path":
			int pathIndex = Integer.parseInt(words[1]);
			String pathType = words[2];
			double x = Double.parseDouble(words[3]);
			double y = Double.parseDouble(words[4]);
			PathWay.createPath(mainPane, pathIndex, pathType, x, y);
			break;
		default:
			System.out.println("Undefined!");
			break;
		}
	}
	//Moves to next level
	private void loadNextLevel(Pane mainPane, ImageView coverImageView, VBox levelButtons) {
		mainPane.getChildren().clear();
		currentLev++;
		if (currentLev <= 5) { 
			collisionCount=0;
			int nextLevel = currentLev ;
			loadLevel(nextLevel, mainPane, coverImageView, levelButtons);

		} 
			else {
			System.out.println("All levels have completed.");
		}
	}
	
	//Creates traffics, spawns cars, and checks traffic lights and collisions
	private void createTraffic(Pane mainPane, ImageView coverImageView, VBox levelButtons) {
		AnimationTimer timer = new AnimationTimer() {
			private double timeSinceLastSpawn = 0.0;

			@Override
			public void handle(long now) {
				timeSinceLastSpawn += 0.08; 

				// Check if enough time has passed since the last car spawn
				if (timeSinceLastSpawn > 4.0) {
					// Spawn a car (30% probability)
					if (Math.random() < 0.3) {
						spawnCar(mainPane,coverImageView,levelButtons);
						// Reset the timer
						timeSinceLastSpawn = 0.0;
					}
				}
				// Check traffic lights
				for (PathTransition pt : new ArrayList<>(transitionList)) {
					checkLights(pt);
				}
				// Check for collisions between cars
				checkCollisions();
			}
		};
		timer.start();
	}
	public static double spawnTime = 0;
	//Spawns cars to random path beginning and and creates path transition
	private void spawnCar(Pane mainPane,ImageView coverImageView, VBox levelButtons) {
		//Takes coordinates from path list
		if (!PathWay.pathList.isEmpty()) {
			int randomIndex = (int) (Math.random() * PathWay.pathList.size());
			Path randomPath = PathWay.pathList.get(randomIndex);
			randomPath.setStroke(Color.TRANSPARENT);
			double x = randomPath.getLayoutX();
			double y = randomPath.getLayoutY();

			Pane carPane = Car.createCar(x, y);
			mainPane.getChildren().add(carPane);

			PathTransition pt = new PathTransition();
			pt.setDuration(Duration.millis(6000));
			pt.setNode(carPane);
			pt.setPath(randomPath);
			spawnTime=System.currentTimeMillis();
			//Counts cars finishing the road
			pt.setOnFinished(event -> {
				winCount++; 
				updateWinLabel(coverImageView, levelButtons); 
			});
			transitionList.add(pt);
			pt.play();
		}
	}
	//Checks the traffic lights
	private void checkLights(PathTransition pt) {
		Pane carPane = (Pane) pt.getNode();
		boolean shouldStop = false;

		for (Circle light : TrafficLight.trafficLights) {
			if (Math.abs(carPane.getBoundsInParent().getCenterX() - light.getBoundsInParent().getCenterX()) < 20 &&
					Math.abs(carPane.getBoundsInParent().getCenterY() - light.getBoundsInParent().getCenterY()) < 20 && light.getFill() == Color.RED) {
				shouldStop = true;
				break;
			}
		}
		if (shouldStop) {
			pt.pause();
		} else {
			pt.play();
		}
	}
	//Checks collisions 
	private void checkCollisions() {
		for (int i = 0; i < transitionList.size(); i++) {
			PathTransition pt1 = transitionList.get(i);
			Pane carPane1 = (Pane) pt1.getNode();
			for (int j = i + 1; j < transitionList.size(); j++) {
				PathTransition pt2 = transitionList.get(j);
				Pane carPane2 = (Pane) pt2.getNode();
				//Checks the distance between cars going on the same path
				if (pt1.getPath().equals(pt2.getPath())) {
					if (Math.abs(carPane1.getBoundsInParent().getCenterX() - carPane2.getBoundsInParent().getCenterX())<20 &&
							Math.abs(carPane1.getBoundsInParent().getCenterY() - carPane2.getBoundsInParent().getCenterY())<20) {
						pt2.pause();
					}
					else
						pt2.play();
				}
				//Checks collisions between cars going on the different path
				else {
					if (carPane1.getBoundsInParent().intersects(carPane2.getBoundsInParent())) {
						pt1.pause();
						pt2.pause();
						removeCar(carPane1);
						removeCar(carPane2);
						collisionCount++;
						updateCollisionCounter();
					}
					//Prints game over label
					if(collisionCount/5==maxCollision) {
						if (gameOverLabel == null) {
							gameOverLabel = new Label();
							gameOverLabel.setTranslateX(paneWidth/4);
							gameOverLabel.setTranslateY(paneHeight/2.3); 
							gameOverLabel.setTextFill(Color.RED); 
							gameOverLabel.setFont(Font.font("Stencil",75));
							mainPane.getChildren().addAll(gameOverLabel);
							gameOverLabel.setText("GAME OVER");
						}		
						pt1.stop();
						pt2.stop();
						stopCarAnimations();
					}
				}
			}
		}
		Rectangle cover = new Rectangle(0, 0, cellWidth, cellHeight);
		cover.setFill(Color.TEAL);
		cover.setStroke(Color.TRANSPARENT);
		mainPane.getChildren().add(cover);
		if (currentLev> 5) {
			Rectangle rec = new Rectangle(paneWidth,paneHeight);
			Text message = new Text(250, 250, "CONGRATULATIONS!");
			message.setFill(Color.PALETURQUOISE); 
			message.setFont(Font.font("Courier", FontWeight.BOLD,
					FontPosture.ITALIC, 25));
			mainPane.getChildren().add(rec);
			mainPane.getChildren().add(message);
			
		}
		
	}
	//Updates collision counter 
	private void updateCollisionCounter() {
		if (collisionLabel == null) {

			collisionLabel = new Label();
			collisionLabel.setTranslateX(190);
			collisionLabel.setTranslateY(34); 
			collisionLabel.setTextFill(Color.NAVY); 
			collisionLabel.setFont(Font.font("Courier",FontWeight.BOLD,FontPosture.ITALIC, 25)); 
			mainPane.getChildren().add(collisionLabel);
		}
		collisionLabel.setText(collisionCount/5 + "/" + maxCollision );

	}
	//Updates win label 
	private void updateWinLabel(ImageView coverImageView, VBox levelButtons) {

		if (winCount >= requiredWin) {
			if (gameOverLabel == null) {
				gameOverLabel = new Label("You Won!"); 
				stopCarAnimations();
				gameOverLabel.setTextFill(Color.NAVY);
				gameOverLabel.setFont(Font.font("Stencil", 75));
				gameOverLabel.setLayoutX(paneWidth/3.8);
				gameOverLabel.setLayoutY(paneHeight/2.3);
				mainPane.getChildren().add(gameOverLabel);
			}
			stopCarAnimations();
			collisionCount = maxCollision;
		} else {
			if (gameOverLabel != null) {
				mainPane.getChildren().remove(gameOverLabel);
				gameOverLabel = null;
			}

			if (winLabel == null) {
				winLabel = new Label( winCount + "/" + requiredWin);
				winLabel.setTextFill(Color.NAVY);
				winLabel.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 25));
				winLabel.setLayoutX(165); 
				winLabel.setLayoutY(4); 
				mainPane.getChildren().add(winLabel); 
			} else {
				winLabel.setText( winCount + "/" + requiredWin); 
			}
		}
	}
	
	//Removes car after delay
	private void removeCar(Pane carPane) {
		new AnimationTimer() {
			private double elapsedTime = 0.0;

			@Override
			public void handle(long now) {
				elapsedTime += 0.16;

				if (elapsedTime >= 0.5) {
					transitionList.removeIf(pt -> pt.getNode() == carPane);
					carPane.setVisible(false);
					stop();
				}
			}
		}.start();
	}
	
	//Stops car animations
		private void stopCarAnimations() {
			for (PathTransition pt : transitionList) {
				pt.stop();
			}
			transitionList.clear(); 
		}
		
	//Exits the program
	private void exit() {
		System.exit(0);
	}
	
	//Main method
	public static void main(String args[]) {
		launch(args);
	}
}