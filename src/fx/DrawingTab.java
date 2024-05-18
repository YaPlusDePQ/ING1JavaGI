package fx;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

/**
* Group that store a tab for the application. Each DrawingTab is independant from each other and dont interract.
*/
public class DrawingTab extends Group{
    
    private double width;
    private double height;
    private Canvas mainCanvas;
    private Canvas cursorCanvas;
    private GraphicsContext mainCanvasGC;
    private GraphicsContext cursorCanvasGC;
    private List<DrawingCursor> cursorsList;
    
    /**
    * Constructor
    *
    * @param  width  width of the canvas
    * @param  height  height of the canvas
    */
    public DrawingTab(double width, double height){
        super();
        this.width = width;
        this.height = height;
        this.mainCanvas = new Canvas(width,height);
        this.cursorCanvas = new Canvas(width,height);
        this.mainCanvasGC = this.mainCanvas.getGraphicsContext2D();
        this.cursorCanvasGC = this.cursorCanvas.getGraphicsContext2D();
        this.cursorsList = new ArrayList<DrawingCursor>();

        this.cursorsList.add(new DrawingCursor(0));
        this.setActiveCursor(0);
        //adding all the widget so they show up
        
        this.getChildren().add(this.mainCanvas);
        this.getChildren().add(this.cursorCanvas);
    }
    
    /**
    * Get the width of the canvas
    *
    * @return width of the canvas
    */
    public double getWidth(){
        return this.width;
    }
    
    /**
    * Get the height of the canvas
    *
    * @return height of the canvas
    */
    public double getHeight(){
        return this.height;
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
                cursor.setActive(true);
            }
            else{
                cursor.setActive(false);
            }
        }
    }
    
    /**
    * Add a new cursor in the list
    *
    * @param  id id for the cursor
    * @param  xStart start position on the X axis
    * @param  yStart start position on the Y axis
    * @param  color start color, dont set the alpha setting here
    * @param  opacity start opacity
    * @param  thickness start thicness
    * @param  active must be true if the cursor will be directly use to draw
    */
    public void addCursor(int id, double xStart, double yStart, Color color, double opacity, double thickness, boolean active){
        this.cursorsList.add(new DrawingCursor(id,xStart, yStart, color, opacity, thickness, active));
    }

    /**
    * Add a new cursor in the list
    *
    * @param  id id for the cursor
    */
    public void addCursor(int id){
        this.cursorsList.add(new DrawingCursor(id));
    }
    
    /**
    * Add a new cursor in the list
    *
    * @param  id id for the cursor
    * @param  existingCursor cursor to copy the configuration from
    * @param  active must be true if the cursor will be directly use to draw
    */
    public void addCursor(int id, DrawingCursor existingCursor, boolean active){
        this.cursorsList.add(new DrawingCursor(id, existingCursor, active));
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
        this.mainCanvasGC.setFill(cursor.getColor());
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
        
        this.drawCursor();

    }

    public void drawCursor(){
        this.cursorCanvasGC.clearRect(0, 0, this.cursorCanvas.getWidth(), this.cursorCanvas.getHeight());

        for(DrawingCursor cursor : this.cursorsList){
            if(cursor.isVisible()){
                this.cursorCanvasGC.setGlobalAlpha(1);
                this.cursorCanvasGC.setStroke(cursor.getColor());
                this.cursorCanvasGC.setLineWidth(1);
                double Xs[] = {cursor.getCurrentX(), cursor.getCurrentX(), cursor.getCurrentX()+10};
                double Ys[] = {cursor.getCurrentY(), cursor.getCurrentY()+10, cursor.getCurrentY()};
                this.cursorCanvasGC.fillPolygon(Xs, Ys, 3);
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
    
    
    
    /**
    * Draw a circle in the canvas from all active cursor. Its recommanded to only have one cursor active at a time.
    *
    * @param radius circle Radius
    */
    public void drawCricle(double radius, boolean fill){
        for(DrawingCursor cursor : this.cursorsList){
            if(cursor.isActive()){
                this.configMainCanvasGC(cursor);
                if(fill){
                    this.mainCanvasGC.fillOval(cursor.getCurrentX()-radius, cursor.getCurrentY(), radius*2, radius*2);
                }
                else {
                    this.mainCanvasGC.strokeOval(cursor.getCurrentX()-radius, cursor.getCurrentY(), radius*2, radius*2);
                }
            }
        }
    }
    
    /**
    * Draw a line in the canvas from a specific active cursor.
    *
    * @param cursorID Id of the cursor
    * @param radius circle Radius
    */
    public void drawCricle(int cursorID, double radius){
        DrawingCursor cursor = this.getDrawingCursor(cursorID);
        if(cursor == null){
            return;
        }
        
        if(cursor.isActive()){
            this.configMainCanvasGC(cursor);
            this.mainCanvasGC.strokeOval(cursor.getCurrentX()-radius, cursor.getCurrentY(), radius*2, radius*2);
        }
    }
}   
