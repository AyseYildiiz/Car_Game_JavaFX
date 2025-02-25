import javafx.scene.shape.MoveTo;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Path;

public class PathWay {
  // Array list for paths
    public static ArrayList<Path> pathList = new ArrayList<>();
    
  //This method creates paths and adds them to the pane
    public static void createPath(Pane p,int pathIndex, String pathType, double x, double y) {
        Path path;

        while (pathList.size() <= pathIndex) {
            pathList.add(null);
        }
        

        switch (pathType) {
            case "MoveTo":
                
                path = new Path();
                path.getElements().add(new MoveTo(x, y));
                pathList.set(pathIndex, path);
                
                break;
            case "LineTo":
               
                path = pathList.get(pathIndex);
                path.getElements().add(new LineTo(x, y));
                pathList.set(pathIndex, path);
                break;
            default:
                return;
        }
        if (!p.getChildren().contains(path)) {
            p.getChildren().add(path);
         path.toBack();
        }
    }
}