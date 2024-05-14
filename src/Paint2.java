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

        DrawingCursor c = t.getDrawingCursor(0);

        c.setXY(300,300);
        t.drawLine();

        c.setDirection(135);
        c.move(50);
        t.drawLine();

        c.setColor(Color.RED);

        Interpreter test = new Interpreter(t,  "FWD 40\n    FWD 20%   ");
        try{
            test.runNextInstruction();

        }catch (Exception e){
            System.out.println(e);
        }

        c.setColor(Color.PURPLE);


        try{
            test.runNextInstruction();

        }catch (Exception e){
            System.out.println(e);
        }

        try{
            test.runNextInstruction();

        }catch (Exception e){
            System.out.println(e);
        }

    }
    
    public static void main(String[] args) {
        launch();

    }
}
