package fx;

import javafx.scene.paint.Color;

/**
* This class create a Cursor that can be use to draw line inside a canvas. Any line can be drawn by using 
* the old and current coordinate as start and end point. Each cursor got it own configuration (color, opacity, ect...).
* 
*/
public class DrawingCursor {
    
    
    /**
    * Constant to for direction modificator, it can be use to change the comportement of a cursor when changing direction.
    * <p>
    * No changement
    */
    public final static int NONE = 0; 
    /**
    * Constant to for direction modificator, it can be use to change the comportement of a cursor when changing direction.
    * <p>
    * While change direction as if the point follow an other point around a central axe
    */
    public final static int MIRRORED_AXIS = 1;
    /**
    * Constant to for direction modificator, it can be use to change the comportement of a cursor when changing direction.
    * <p>
    * While change direction as if the point follow an other point around a central point
    */
    public final static int MIRRORED_POINT = 2;
    
    private int id; //id of the cursor, it as to be unique for an independante cursor to be created.
    private double currentX; //explicite
    private double currentY; //explicite
    private double oldX; //explicite
    private double oldY; //explicite
    private double direction = 0; // direction the point is looking at in radian
    private Color color; // color configuriration used by the cursor (alpha must be always at full value)
    private double opacity; // explicite
    private double thickness; // explicite
    private boolean active; // if the cursor is being actif on the screen
    private boolean visible; // if the icone of the cursor is visible
    /**
    * store the current direction modificator.
    * @see DrawingCursor.NONE, DrawingCursor.MIRRORED_AXIS, DrawingCursor.MIRRORED_POINT
    */
    private int directionModificator = NONE;
    
    /**
    * Constructor
    *
    * @param  id id for the cursor, it as to be unique for an independant cursor to be created.
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
    * @param  id id for the cursor, it as to be unique for an independant cursor to be created.
    * @param  existingCursor cursor to copy the configuration from (copy all except: direction, active state, id and directionModificator)
    * @param  active must be true if the cursor will be directly use to draw
    */
    public DrawingCursor(int id, DrawingCursor existingCursor, boolean active){
        this.id = id;
        this.oldX = existingCursor.getOldX();
        this.oldY = existingCursor.getOldY();
        this.currentX = existingCursor.getCurrentX();
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
    * @param  id id for the cursor, it as to be unique for an independant cursor to be created.
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
    * Getter for visibility of the cursor
    *
    * @return return is the cursor is visible
    */
    public boolean isVisible(){
        return this.visible;
    }
    
    /**
    * Getter for direction modificator in use
    *
    * @return return is the cursor is visible
    */
    public int getDirectionModificator(){
        return this.directionModificator;
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
    * Set the current direction
    *
    * @param  changeInDirection newDirection in radian
    */
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
    * Turn the cursor
    *
    * @param  changeInDirection change in degrees
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
    
    /**
    * Configure the cursor so that it is set in the right place and facing the correct direction when applying a direction modificator.
    * By exemple it can be use to copy a cursor and applying an axial symmetrie on the copy
    *
    * @param  modificator modificator selected (DrawingCursor.NONE, DrawingCursor.MIRRORED_AXIS, DrawingCursor.MIRRORED_POINT)
    * @param  targetCursor cursor to copy the configuration from
    * @param  referringPoint array of coordinnates of points used depending of the direction modificator use. (exemple, points coordinate's where the line of a axial symmetrie passes )
    * @see DrawingCursor.NONE, DrawingCursor.MIRRORED_AXIS, DrawingCursor.MIRRORED_POINT
    */
    public void linkToOtherCursor(int modificator, DrawingCursor targetCursor, double[] referringPoint){
        this.directionModificator = modificator;
        
        /* 
        * If the MIRROR and MIMIC doesnt work as intended just know that it juste the math that are a miss. If you get them right it will work perfectly fine.
        * But I have to admit that after WAY to much time doing this f*****g math, it look like it work so I will seatle with this.
        */
        switch (modificator) {
            case MIRRORED_AXIS:
                
                /* The math, if you want detail juste ask google, chatGPT, or something*/
                double x = targetCursor.getCurrentX();
                double y = targetCursor.getCurrentY();
                
                if(referringPoint[2] - referringPoint[0] != 0 && referringPoint[3] - referringPoint[1] != 0){
                    double a = (referringPoint[3] - referringPoint[1]) / (referringPoint[2] - referringPoint[0]);
                    double b = -1;
                    double c = referringPoint[1] - a*referringPoint[0];
                    
                    setXY(x-( (2*a*(a*x+b*y+c))/(a*a+b*b)  ), y-( (2*b*(a*x+b*y+c))/(a*a+b*b)  ));
                }
                else if(referringPoint[2] - referringPoint[0] == 0){
                    setXY(2*referringPoint[0]-x, y);
                }
                else{
                    setXY(x, 2*referringPoint[1]-y);
                }
                
                
                double m = (referringPoint[2] - referringPoint[0]) == 0 ? 0 : Math.atan( (referringPoint[3] - referringPoint[1]) / (referringPoint[2] - referringPoint[0]) );
                if(m == 0 && !( (referringPoint[2] - referringPoint[0]) == 0)){
                    m = -Math.PI/2;
                }
                
                this.direction = 2*m-Math.toRadians(targetCursor.getDirection());//set the direction the cursor should look at
                this.directionModificator = targetCursor.directionModificator == MIRRORED_AXIS ?  NONE : MIRRORED_AXIS; // in case of a copy of an already mirrored, cancel the mode
                break;
            case MIRRORED_POINT:
                this.direction = Math.toRadians(targetCursor.getDirection())+Math.PI;
                setXY(2*referringPoint[0]-targetCursor.getCurrentX(), 2*referringPoint[1] - targetCursor.getCurrentY());
                break;
            default:
                setXY(targetCursor.getCurrentX(),  targetCursor.getCurrentY());
                break;
        }
    }
    
    @Override
    public String toString(){
        return String.format("(%.1f; %.1f) %s {\n   was at: (%.1f; %.1f)\n   Direction (deg): %.2f\n   alpha: %.2f\n   thickness: %.2f\n}", this.currentX, this.currentY, this.active ? "[ACTIVE]" : "[DISABLE]", this.oldX, this.oldY, Math.toDegrees(this.direction),this.opacity, this.thickness);
    }
}
