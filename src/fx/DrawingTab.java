package fx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

/**
* Group that store a tab for the application. Each DrawingTab is independant from each other and dont interract.
*/
public class DrawingTab extends Group{
    
    private Canvas mainCanvas;
    private GraphicsContext mainCanvasGC;
    private List<DrawingCursor> cursorsList;
    
    /**
    * Constructor
    *
    * @param  width  width of the canvas
    * @param  height  height of the canvas
    */
    public DrawingTab(double width, double height){
        super();
        this.mainCanvas = new Canvas(width,height);
        this.mainCanvasGC = this.mainCanvas.getGraphicsContext2D();
        this.cursorsList = new ArrayList<>( Arrays.asList(new DrawingCursor(0, 0, 0, Color.web("rgb(0,0,255)"), 1, 1, true)) );
        
        
        //adding all the widget so they show up
        this.getChildren().add(this.mainCanvas);
    }
    
    /**
    * Get the list of all cursor of the canvas
    *
    * @return List of DrawingCursor
    */
    public List<DrawingCursor> getAllDrawingCursor(){
        return this.cursorsList;
    }
    
    /**
    * Get the DrawingCursor with the corresponding ID. 
    * In case of multiple match, it only return the first in the list. 
    *
    * @return cursor with the corresponding ID, or null if no match 
    */
    public DrawingCursor getDrawingCursor(int cursorID){
        for(DrawingCursor cursor : this.cursorsList){
            if(cursor.getID() == cursorID){
                return cursor;
            }
        }
        return null;
    }

    /**
    * Set the cursor with the corresponding id as active, other are disabled.
    *
    * @param cursorID Id of the cursor
    */
    public void setActiveCursor(int cursorID){
        for(DrawingCursor cursor : this.cursorsList){
            if(cursor.getID() == cursorID){
                cursor.setActiveStatus(true);
            }
            else{
                cursor.setActiveStatus(false);
            }
        }
    }

    /**
    * Remove a cursor from the list. This method only work if there is at least 2 cursors.
    * If the cursor removed was active, set the first cursor as the new active one.
    *
    * @param cursorID Id of the cursor
    */
    public void removeCursor(int cursorID){
        boolean setNewActive = false;

        if(cursorsList.size() <= 1){
            return;
        }

        for(int i=0; i<this.cursorsList.size(); i++){
            if(this.cursorsList.get(i).getID() == cursorID){
                if(this.cursorsList.get(i).isActive()){
                    setNewActive = true;
                }
                this.cursorsList.remove(i);
            }
        }

        if(setNewActive){
            this.setActiveCursor(this.cursorsList.get(0).getID());
        }
    }
    
    /**
    * Configure the graphics context so that it correspond to the cursor specification
    *
    * @param cursor cursor to use the configuration from
    */
    public void configMainCanvasGC(DrawingCursor cursor){
        this.mainCanvasGC.setGlobalAlpha(cursor.getOpacity());
        this.mainCanvasGC.setStroke(cursor.getColor());
        this.mainCanvasGC.setLineWidth(cursor.getThickness());
    }
    
    /**
    * Draw a line in the canvas from all active cursor. Its recommanded to only hhave one cursor active at a time.
    *
    */
    public void drawLine(){
        for(DrawingCursor cursor : this.cursorsList){
            if(cursor.isActive()){
                this.configMainCanvasGC(cursor);
                this.mainCanvasGC.strokeLine(cursor.getOldX(), cursor.getOldY(), cursor.getCurrentX(), cursor.getCurrentY());
            }
        }
    }
    
    /**
    * Draw a line in the canvas from a specific active cursor.
    *
    * @param cursorID Id of the cursor
    */
    public void drawLine(int cursorID){
        DrawingCursor cursor = this.getDrawingCursor(cursorID);
        if(cursor == null){
            return;
        }

        if(cursor.isActive()){
            this.configMainCanvasGC(cursor);
            this.mainCanvasGC.strokeLine(cursor.getOldX(), cursor.getOldY(), cursor.getCurrentX(), cursor.getCurrentY());
        }
    }
    
}   