package instructions.commandes;

import fx.DrawingTab;

public class CommandBwd extends command{

    public void Bwd(DrawingTab t, float value, boolean isPercentage) {
        float pixelsToMove;
        
        if (isPercentage) {
            float largestDimension = Math.max(t.getwidth(), t.getheight());
            pixelsToMove = value * largestDimension / 100;
        } 
        else {
            pixelsToMove = value;
        }
        
        t.Turn(180);
        t.move(pixelsToMove);
        t.drawLine();
    }
}