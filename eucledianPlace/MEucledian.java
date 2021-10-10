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

import algorithms.Triangulation;
import entities.point.Pnt;
import entities.triangle.Triangle;
import java.awt.Graphics;

/**
 *
 * @author Dermentzis Vassileios <vassileiosdermentzis@gmail.com>
 */
public class MEucledian {
    private VEucledian vEucledian;
    private final Triangulation triangulation;
    private Graphics g; // Stored graphics context
    private static int initialSize = 10000; // Size of initial triangle (coordinates)
    private final Triangle initialTriangle; // Initial triangle that encloses every other triangle
    private String s;
    
    public MEucledian(){
        this.vEucledian = new VEucledian();
        this.initialTriangle = new Triangle(new Pnt(-initialSize, -initialSize),new Pnt(initialSize, -initialSize),new Pnt(0, initialSize));
        this.triangulation = new Triangulation(this.initialTriangle);
    }
    
    public void drawAllEukleidian() {
        for (Triangle triangle : triangulation) {
            Pnt[] vertices = triangle.toArray(new Pnt[0]);
            if(vertices[0].coord(0) != this.vEucledian.getX())
                draw(vertices);
        }
    }

    public void draw(Pnt[] polygon) {
        int[] x = new int[polygon.length];
        int[] y = new int[polygon.length];
            for (int i = 0; i < polygon.length; i++) {
                x[i] = (int) polygon[i].coord(0);
                y[i] = (int) polygon[i].coord(1);
                g.fillOval(x[i], y[i], 10, 10);
                g.drawString(s.valueOf(x[i])+ " " + s.valueOf(y[i]), x[i], y[i]);
            }            
        g.drawPolygon(x, y, polygon.length);
    }

    public void addSite(Pnt point) {
        triangulation.eukleidianPlace(point);
    }
    
    public void removeSite(Pnt point){
        triangulation.eukleidianPlace2(point);
    } 
}