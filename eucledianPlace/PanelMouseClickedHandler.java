/*
 * Copyright (C) 2014 Dermentzis Vassileios
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eucledianPlace;

import entities.point.Pnt;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Dermentzis Vassileios
 */
public class PanelMouseClickedHandler implements PanelHandler{
    PanelHandler nextInChain;
    VEucledian vEucledian;
    MEucledian mEucledian;
    
    public PanelMouseClickedHandler(MEucledian mEucledian,VEucledian vEucledian){
        this.mEucledian = mEucledian;
        this.vEucledian = vEucledian;
    }
    
    @Override
    public void setNextHandlerInChain(PanelHandler nextInChain) {
        this.nextInChain = nextInChain;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getClickCount() == 1){
            Pnt point = new Pnt(e.getX(), e.getY());
            this.mEucledian.addSite(point);
            this.vEucledian.repaint();
        }
        else{
            JOptionPane.showMessageDialog(null, "This action is not valid", null, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {}
    @Override
    public void mouseReleased(MouseEvent me) {}
    @Override
    public void mouseEntered(MouseEvent me) {}
    @Override
    public void mouseExited(MouseEvent me) {}  
}