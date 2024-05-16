import fx.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;

import interpreter.Interpreter;

/**
 * Main of the application
*/
public class Paint2 extends Application{
    
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene s = new Scene(root, 500, 500, Color.WHITE);
        DrawingTab t = new DrawingTab(500,500);

        root.getChildren().add(t);
        stage.setScene(s);
        stage.show();

        Interpreter test = new Interpreter(t,  "COLOR #FF0000 \n TURN 45\n FWD 300\n TURN 90\n COLOR #00FF00\n FWD 50\n COLOR #0000FF\n TURN -45\n THICK 5\n FWD 2% \n TURN 90\n BWD 50");

        try{
            test.runAllInstructions();

        }catch (Exception e){
            System.out.println("\u001B[41m"+e+"\u001B[0m");
        }

    }
    
    public static void main(String[] args) {
        launch();

    }
}
