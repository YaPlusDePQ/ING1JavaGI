package fx;

import javafx.scene.paint.Color;

/**
* This class create a Cursor that can be use to draw line inside a canvas. Any line can be drawn by using 
* the old and current coordinate as start and end point. Each cursor got it own configuration (color, opacity, ect...).
* 
* 
*/
public class DrawingCursor {
    public final static int NONE = 0;
    public final static int MIRRORED_AXIS = 1;
    public final static int MIRRORED_POINT = 2;

    private int id; 
    private double currentX; 
    private double currentY;
    private double oldX;
    private double oldY;
    private double direction = 0;
    private Color color;
    private double opacity;
    private double thickness;
    private boolean active;
    private boolean visible;
    private int directionModificator = NONE;
    private double[] directionModificatorPoint = null;
    
    /**
    * Constructor
    *
    * @param  id id for the cursor
    * @param  xStart start position on the X axis
    * @param  yStart start position on the Y axis
    * @param  color start color, dont set the alpha setting here
    * @param  opacity start opacity
    * @param  thickness start thicness
    * @param  active must be true if the cursor will be directly use to draw
    */
    public DrawingCursor(int id, double xStart, double yStart, Color color, double opacity, double thickness, boolean active){
        this.id = id;
        this.oldX = this.currentX = xStart;
        this.oldY = this.currentY = yStart;
        this.color = color;
        this.opacity = opacity;
        this.thickness = thickness;
        this.active = active;
        this.visible = true;
    }

    /**
    * Constructor
    *
    * @param  id id for the cursor
    * @param  existingCursor cursor to copy the configuration from
    * @param  active must be true if the cursor will be directly use to draw
    */
    public DrawingCursor(int id, DrawingCursor existingCursor, boolean active){
        this.id = id;
        this.oldX = existingCursor.getOldX();
        this.currentX = existingCursor.getCurrentX();
        this.oldY = existingCursor.getOldY();
         this.currentY = existingCursor.getCurrentY();;
        this.color = existingCursor.getColor();;
        this.opacity = existingCursor.getOpacity();;
        this.thickness = existingCursor.getThickness();;
        this.active = active;
        this.visible = existingCursor.isVisible();
    }

    /**
    * Constructor
    *
    * @param  id id for the cursor
    */
    public DrawingCursor(int id){
        this.id = id;
        this.oldX = 0;
        this.currentX = 0;
        this.oldY = 0;
        this.currentY = 0;
        this.color = Color.BLACK;
        this.opacity = 1.0;
        this.thickness = 1.0;
        this.active = false;
        this.visible = true;
    }
    
    /**
    * Getter for id
    *
    * @return return the id of the cursor
    */
    public int getID(){
        return this.id;
    }
    
    /**
    * Getter for the current X position
    *
    * @return return the current X position
    */
    public double getCurrentX(){
        return this.currentX;
    }
    
    /**
    * Getter for the current Y position
    *
    * @return return the current Y position
    */
    public double getCurrentY(){
        return this.currentY;
    }
    
    /**
    * Getter for the previous X position
    *
    * @return return the previous X position
    */
    public double getOldX(){
        return this.oldX;
    }
    
    /**
    * Getter for the previous Y position
    *
    * @return return the previous Y position
    */
    public double getOldY(){
        return this.oldY;
    }
    
    /**
    * Getter for the direction
    *
    * @return return the current direction in rad
    */
    public double getDirection(){
        return Math.toDegrees(this.direction);
    }
    
    /**
    * Getter for the color
    *
    * @return return the Color object in use
    */
    public Color getColor(){
        return this.color;
    }
    
    /**
    * Getter for the opacity
    *
    * @return return the alpha value for the cursor
    */
    public double getOpacity(){
        return this.opacity;
    }
    
    /**
    * Getter for the thickness
    *
    * @return return the thickness in pixel in use
    */
    public double getThickness(){
        return this.thickness;
    }
    
    /**
    * Getter for active
    *
    * @return return is the cursor is considered as active
    */
    public boolean isActive(){
        return this.active;
    }

    /**
    * Getter for cursor
    *
    * @return return is the cursor is visible
    */
    public boolean isVisible(){
        return this.visible;
    }
    
    /**
    * Set the current X and Y position
    *
    * @param  newX absolute position on the X axis
    * @param  newY absolute position on the Y axis
    */
    public void setXY(double newX, double newY){
        this.oldX = this.currentX;
        this.oldY = this.currentY;
        this.currentX = newX;
        this.currentY = newY;
    }
    
    /**
    * Set the current X and Y position by following the current direction
    *
    * @param  value length in pixel to go through
    */
    public void move(double value){
        this.oldX = this.currentX;
        this.oldY = this.currentY;
        
        this.currentX = this.currentX + value*Math.sin(this.direction);
        this.currentY = this.currentY + value*Math.cos(this.direction);
    }
    
    /**
    * Set the current direction
    *
    * @param  changeInDirection direction in degrees
    */
    public void turn(double changeInDirection){
        switch (this.directionModificator) {
            case MIRRORED_AXIS:
                this.direction -= Math.toRadians(changeInDirection);
                break;
            case MIRRORED_POINT:
                this.direction += Math.toRadians(changeInDirection);
                break;
            case NONE:
            default:
                this.direction += Math.toRadians(changeInDirection);
                break;
        }
        System.out.println(this);
    }

    
    public void setDirection(double newDirection){
        this.direction = newDirection;
    }
    
    /**
    * Set the color that will be use to draw. 
    * It is not recommanded to use any Color object that as an other value than 1 for alpha.
    *
    * @param  newColor Color to set
    */
    public void setColor(Color newColor){
        this.color = newColor;
    }
    
    /**
    * Set the opacity that will be use to draw. 
    *
    * @param  newOpacity opacity/alpha value betwen 0.0 and  1.0
    */
    public void setOpacity(double newOpacity){
        this.opacity = (newOpacity > 1 || newOpacity < 0.0 ? 1.0 : newOpacity);
    }
    
    /**
    * Set the Thickness that will be use to draw. 
    *
    * @param  newThickenss size in pixel
    */
    public void setThickness(double newThickenss){
        this.thickness = newThickenss < 0 ? 1.0 : newThickenss;
    }

    /**
    * Set the active status of the cursor. Its recommanded to have only one cursor active at a time.
    *
    * @param  status new status
    */
    public void setActive(boolean status){
        this.active = status;
    }
    
    /**
    * Set the visible status of the cursor.
    *
    * @param  status new status
    */
    public void setVisible(boolean status){
        this.visible = status;
    }

    public void linkToOtherCursor(int modificator, DrawingCursor targetCursor, double[] referringPoint){
        this.directionModificator = modificator;
        this.directionModificatorPoint = referringPoint;
        switch (modificator) {
            case MIRRORED_AXIS:

                this.directionModificator = targetCursor.directionModificator == MIRRORED_AXIS ?  NONE : MIRRORED_AXIS;

                double m = (this.directionModificatorPoint[2] - this.directionModificatorPoint[0]) == 0 ? 0 : Math.atan( (this.directionModificatorPoint[3] - this.directionModificatorPoint[1]) / (this.directionModificatorPoint[2] - this.directionModificatorPoint[0]) );
                
                if(m == 0 && !( (this.directionModificatorPoint[2] - this.directionModificatorPoint[0]) == 0)){
                    m = -Math.PI/2;
                }
                
                double x = targetCursor.getCurrentX();
                double y = targetCursor.getCurrentY();

                if(this.directionModificatorPoint[2] - this.directionModificatorPoint[0] != 0 && this.directionModificatorPoint[3] - this.directionModificatorPoint[1] != 0){
                    double a = (this.directionModificatorPoint[3] - this.directionModificatorPoint[1]) / (this.directionModificatorPoint[2] - this.directionModificatorPoint[0]);
                    double b = this.directionModificatorPoint[1] - a*this.directionModificatorPoint[0];
                    double xi = 0;
                    double yi = 0;

                    if(a == 1){
                        xi = (y-b)/(a+(1/a));
                        yi = a*xi+b;
                        setXY(2*xi, 2*yi);
                    }
                    else if( a == -1){
                        xi = (x+y)/Math.sqrt(2);
                        yi = -((y-x)/Math.sqrt(2));
                        setXY( (xi-yi)/Math.sqrt(2), (xi+yi)/Math.sqrt(2));
                    }
                    else{
                        setXY(2*xi-x, 2*yi-y);
                    }
                }
                else if(this.directionModificatorPoint[2] - this.directionModificatorPoint[0] == 0){
                    setXY(2*this.directionModificatorPoint[0]-x, y);
                }
                else{
                    setXY(x, 2*this.directionModificatorPoint[1]-y);
                }
                
                this.direction = 2*m-Math.toRadians(targetCursor.getDirection());
                break;
            case MIRRORED_POINT:
                this.direction = Math.toRadians(targetCursor.getDirection())+Math.PI;
                setXY(2*this.directionModificatorPoint[0]-targetCursor.getCurrentX(), 2*this.directionModificatorPoint[1] - targetCursor.getCurrentY());
                break;
            default:
                break;
        }
    }
    
    @Override
    public String toString(){
        return String.format("(%.1f; %.1f) %s {\n   was at: (%.1f; %.1f)\n   Direction (deg): %.2f\n   alpha: %.2f\n   thickness: %.2f\n}", this.currentX, this.currentY, this.active ? "[ACTIVE]" : "[DISABLE]", this.oldX, this.oldY, Math.toDegrees(this.direction),this.opacity, this.thickness);
    }
}
