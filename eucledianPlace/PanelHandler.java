package eucledianPlace;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Dermentzis Vassileios
 */
public interface PanelHandler extends MouseListener{
    public void setNextHandlerInChain(PanelHandler nextInChain);
    
    @Override
    public void mousePressed(MouseEvent e);
}