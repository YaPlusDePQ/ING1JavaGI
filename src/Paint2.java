import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        
        Interpreter test = new Interpreter(t, "");
        
        try {
            File myObj = new File("C:\\Users\\alexr\\OneDrive\\Documents\\programmation\\java\\ING1\\script.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                test.addInstruction(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        Button b = new Button("button"); 
        
        // action event 
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                Thread thread = new Thread(()->{
                    try{
                        test.runAllInstructions(100);
                        
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
