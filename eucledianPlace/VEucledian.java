package eucledianPlace;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Dermentzis Vassileios
 */
public class VEucledian extends JPanel{
    private Color panelColor = Color.green;
    private MEucledian mEucledian;
    
    public VEucledian() {
        setBackground(this.panelColor);
        setSize(this.getWidth(),this.getHeight());
        this.mEucledian = new MEucledian();
        this.mEucledian.drawAllEukleidian();
    }
    
    public void addMouseListener(PanelHandler handler){
        addMouseListener(handler);
    }
}