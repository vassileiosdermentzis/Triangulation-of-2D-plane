/*
 * Copyright (C) 2014 Dermentzis Vassileios <vassileiosdermentzis@gmail.com>
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

/**
 *
 * @author Dermentzis Vassileios <vassileiosdermentzis@gmail.com>
 */
public class CEucledian {
    private VEucledian vEucledian;
    private MEucledian mEucledian;
    private PanelHandler panelMouseClickedHandler;
    
    public CEucledian(MEucledian mEucledian, VEucledian vEucledian){
        this.mEucledian = mEucledian;
        this.vEucledian = vEucledian;
        this.panelMouseClickedHandler = new PanelMouseClickedHandler(this.mEucledian, this.vEucledian);
        this.panelMouseClickedHandler.setNextHandlerInChain(this.panelMouseClickedHandler);
        this.vEucledian.addMouseListener(this.panelMouseClickedHandler);
    }
}