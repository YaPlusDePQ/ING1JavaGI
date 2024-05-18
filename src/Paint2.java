import fx.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.event.EventHandler; 

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
        
        Interpreter test = new Interpreter(t,  "COLOR #FF0000 \n TURN 45\n FWD 300\n TURN 90\n COLOR '#00 FF00'\n FWD 50\n COLOR #0000FF\n TURN -45\n THICK 5\n FWD 2% \n TURN 90\n BWD 50\n CRICLE 10");
        
        Button b = new Button("button"); 
        
        // action event 
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                Thread thread = new Thread(()->{
                    try{
                        test.runAllInstructions();
                        
                    }catch (Exception f){
                        System.out.println("\u001B[41m"+f+"\u001B[0m");
                    }
                });
                thread.start();
            } 
        }; 
        
        // when button is pressed 
        b.setOnAction(event); 
        root.getChildren().add(b);
        
        stage.setScene(s);
        stage.show();
        
    }
    
    public static void main(String[] args) {
        launch();
        
    }
}
