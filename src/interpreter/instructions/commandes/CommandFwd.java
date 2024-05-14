package instructions.commandes;

import fx.DrawingCursor;
import fx.DrawingTab;

public class CommandFwd extends command{

    public void Fwd(DrawingTab t, double value, boolean isPercentage) {
        double pixelsToMove;
        
        if (isPercentage) {
            double largestDimension = Math.max(t.getwidth(), t.getheight());
            pixelsToMove = value * largestDimension / 100;
        } 
        else {
            pixelsToMove = value;
        }
        
        c.move(pixelsToMove);
        t.drawLine();
    }
}