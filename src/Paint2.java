import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import fx.*;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import java.awt.image.RenderedImage;
import javafx.scene.paint.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.EventHandler; 
import javafx.scene.Group; 
import javafx.scene.Scene;   

import interpreter.Interpreter;
import interpreter.Exceptions.SyntaxError;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;


/**
* Main of the application
*/
public class Paint2 extends Application{
    
    @Override
    public void start(Stage stage){
        Group root = new Group();
        Scene s = new Scene(root, 1860, 1000, Color.GREY);
        DrawingTab t = new DrawingTab(1430,932);
        
        
        root.getChildren().add(t);
        
        Interpreter test = new Interpreter(t, "");
        List<String> inst = new ArrayList<String>();        


        //Text zone
        TextArea textCommand = new TextArea();
        textCommand.setLayoutX(55);
        textCommand.setLayoutY(32);
        textCommand.setPrefWidth(271);
        textCommand.setPrefHeight(600);

        TextArea textTime = new TextArea();
        textTime.setLayoutX(55);
        textTime.setLayoutY(670);
        textTime.setPrefWidth(83);
        textTime.setPrefHeight(38);

        TextArea textTerminal = new TextArea();
        textTerminal.setLayoutX(55);
        textTerminal.setLayoutY(800);
        textTerminal.setPrefWidth(271);
        textTerminal.setPrefHeight(180);

        
        //Button zone
        Button Start = new Button("Start"); 
        Start.setLayoutX(55);
        Start.setLayoutY(740);
        Start.setPrefWidth(83);
        Start.setPrefHeight(38);

        Button Step = new Button("Step"); 
        Step.setLayoutX(149);
        Step.setLayoutY(740);
        Step.setPrefWidth(83);
        Step.setPrefHeight(38);

        Button Screen = new Button("Screen"); 
        Screen.setLayoutX(243);
        Screen.setLayoutY(740);
        Screen.setPrefWidth(83);
        Screen.setPrefHeight(38);

        Button Clear = new Button("Clear"); 
        Clear.setLayoutX(147);
        Clear.setLayoutY(670);
        Clear.setPrefWidth(83);
        Clear.setPrefHeight(38);

        Button Load = new Button("Load"); 
        Load.setLayoutX(241);
        Load.setLayoutY(670);
        Load.setPrefWidth(83);
        Load.setPrefHeight(38);

        t.setLayoutX(395);
        t.setLayoutY(32);

        //set all widget in the root
        root.getChildren().add(Clear);
        root.getChildren().add(Screen);
        root.getChildren().add(Step);
        root.getChildren().add(Load);
        root.getChildren().add(Start);
        root.getChildren().add(textCommand);
        root.getChildren().add(textTime);
        root.getChildren().add(textTerminal);
        textTime.appendText("10");
        
        /**
        * Event Start
        *
        * Test if the previous program is finished, then get all the new commands and execute them
        */
        EventHandler<ActionEvent> eventStart = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                if(test.isFinished()){
                    test.reset();
                }
                
                inst.clear();
                String data = textCommand.getText();
                inst.add(data);
                String Time = textTime.getText();
                long longTime = Long.parseLong(Time);
                if (longTime < 0) {
                    longTime = 0;
                    textTime.clear();
                    textTime.appendText("0");
                }
                final long longFinalTime = longTime;
                try{
                    test.setIntruction(data);
                    
                }
                catch(SyntaxError ex){
                    textTerminal.appendText("Terminal>"+ex.getMessage());
                }

                Thread thread = new Thread(()->{
                    try{
                        
                        test.runAllInstructions(longFinalTime);
                        
                    }catch (Exception f){
                        textTerminal.appendText("Terminal>"+f.getMessage()+"\n");
                    }
                });
                
                thread.start();
            } 
        }; 
        
        /**
        * Event Start
        *
        * Clear all the drawing and the cursors
        */
        EventHandler<ActionEvent> eventClear = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                t.getMainCanvasGC().clearRect(0, 0, 1430 ,932);
                t.getCursorCanvas().clearRect(0, 0, 1430 ,932);
                t.getMainCanvasGC().setFill(Color.WHITE);
                t.getMainCanvasGC().fillRect(0, 0, 1430,932);
            } 
        }; 

        /**
        * Event Start
        *
        * Load a file and write it in the text area 
        */
        EventHandler<ActionEvent> eventLoad = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisissez un fichier");
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null){
                    System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
                    
                } else {
                    System.out.println("Aucun fichier sélectionné.");
                }

                try (Scanner input = new Scanner(selectedFile)) {
                    textCommand.clear();
                    while (input.hasNextLine()) {
                        textCommand.appendText(input.nextLine()+"\n");
                    }
                } catch (FileNotFoundException ex) {
                    textTerminal.appendText("Terminal>"+ex.getMessage()+"\n");
                }
            } 
        }; 

        /**
        * Event Start
        *
        * take a screen shot if the drawing contain somthing
        */
        EventHandler<ActionEvent> eventScreen = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                if (t.isCanvasEmpty()) {
                    Random r= new Random();
                    int random = r.nextInt(100);
                    DirectoryChooser directory = new DirectoryChooser();
                    directory.setTitle("Choisissez un dossier");
                    File selectedDirectory = directory.showDialog(stage);
                    String fDirectory = selectedDirectory.getAbsolutePath();
                    fDirectory += File.separator;
                    File file = new File(fDirectory+"Screen shot"+random+".png");
                    if(file != null){
                        try{
                            WritableImage image = new WritableImage(1430, 932);
                            t.snapshot(null, image);
                            RenderedImage imagef = SwingFXUtils.fromFXImage(image, null);
                            ImageIO.write(imagef, "png", file);
                        }catch(Exception f){
                        textTerminal.appendText("Terminal>"+"Error saving image\n");
                        }
                    }
                }
            } 
        }; 


        /**
        * Event Start
        *
        * do the command step by step 
        */
        EventHandler<ActionEvent> eventStep = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                if(test.isFinished()){
                    test.reset();
                }
                
                inst.clear();
                String data = textCommand.getText();
                inst.add(data);
                
                try{
                    test.setIntruction(data);
                    
                }
                catch(SyntaxError ex){
                    textTerminal.appendText("Terminal>"+ex.getMessage());
                }

                Thread thread = new Thread(()->{
                    try{
                        
                        test.runNextInstruction();
                        
                    }catch (Exception f){
                        textTerminal.appendText("Terminal>"+f.getMessage()+"\n");
                    }
                });
                
                thread.start();
            } 
        }; 
        
        // when button is pressed 
        Start.setOnAction(eventStart); 
        Clear.setOnAction(eventClear);
        Screen.setOnAction(eventScreen);
        Load.setOnAction(eventLoad);
        Step.setOnAction(eventStep);

        stage.setScene(s);
        stage.show();
        
    }
    
    public static void main(String[] args) {
        launch();
        
    }
}
