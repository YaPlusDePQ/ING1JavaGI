import fx.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;

/**
 * Main of the application
*/
public class Paint2 extends Application{
    
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene s = new Scene(root, 500, 500, Color.BLACK);
        DrawingTab t = new DrawingTab(400,400);

        root.getChildren().add(t);
        stage.setScene(s);
        stage.show();

        DrawingCursor c = t.getDrawingCursor(0);

        c.setXY(300,300);
        t.drawLine();

        c.setDirection(135);
        c.move(50);
        t.drawLine();


    }
    
    public static void main(String[] args) {
        launch();
    }
}
