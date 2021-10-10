package app;

import entities.point.Pnt;
import entities.triangle.Triangle;
import algorithms.Triangulation;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Graphics;
import javax.swing.*;

public class TriangulationApp extends javax.swing.JApplet implements Runnable, MouseListener {
    
    private static String windowTitle = "Triangulation";
    private EukleidianPanel3 eukleidianPanel = new EukleidianPanel3(this);
    private static int numberOfMatrices; //o ari8mos twn akmwn toy polygwnoy
    private static String str1;  //afto pairnei to alfari8mitiko sto para8yro ekkinisis
    private static String str2;
    private boolean A;
    private Pnt point;
    
    public static void main(String[] args){

        TriangulationApp applet = new TriangulationApp(); 
        applet.init();                           // Applet initialization
        JFrame dWindow = new JFrame();           // Create window
        dWindow.setSize(800, 600);               // Set window size
        dWindow.setTitle(windowTitle);           // Set window title
        dWindow.setLayout(new BorderLayout());   // Specify layout manager
        dWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                 // Specify closing behavior
        dWindow.add(applet, "Center");           // Place applet into window
        dWindow.setVisible(true);                // Show the window
  
    } //end main
    
   
    public void setA(boolean a) {
        A = a;
    }

    public boolean getA() {
        return A;
    }

    /**
     * Initialize the applet and compute the delay between frames.
     */
    public void init() {
        
        //setA(false);
        
        try {
            SwingUtilities.invokeAndWait(this);
        }
        catch (Exception e) {
            System.err.println("Initialization failure");
        }
       
        eukleidianPanel.repaint(); 
    }

    /**
     * This method is called when the applet becomes visible on
     * the screen. Create a thread and start it.
     */
    public void start() {
	
    }

    /**
     * This method is called by the thread that was created in
     * the start method. It does the main animation.
     */
    public void run() {
        setLayout(new BorderLayout());
        // Build the Eucledian panel
        eukleidianPanel.setBackground(Color.gray);
        this.add(eukleidianPanel, "Center");

        // Register the listeners
        eukleidianPanel.addMouseListener(this);
	
    }

    /**
     * This method is called when the applet is no longer
     * visible. Set the animator variable to null so that the
     * thread will exit before displaying the next frame.
     */
    public void stop() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Pnt point = new Pnt(e.getX(), e.getY());
            eukleidianPanel.addSite(point);
            eukleidianPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }    
}


class EukleidianPanel3 extends JPanel {

    public static Color TriangulationColor = Color.green;
    private Triangulation dt;                   // Delaunay triangulation
    private Triangle initialTriangle;           // Initial triangle
    private static int initialSize = 10000;     // Size of initial triangle
    private Graphics g; // Stored graphics context
    private TriangulationApp controller;
    
    private int[] erasedPoints = new int[10];
    private Pnt eraser;
    
    private String s;
    //constructor
    public EukleidianPanel3(TriangulationApp controller) {
        this.controller = controller;
        
        initialTriangle = new Triangle(
                new Pnt(-initialSize, -initialSize),
                new Pnt(initialSize, -initialSize),
                new Pnt(0, initialSize));

        dt = new Triangulation(initialTriangle);
    }

    //afti einai i paint poy emfanizei ta panta stin o8oni
 
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        // Flood the drawing area with a "background" color
        Color temp = g.getColor();
        g.setColor(TriangulationColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(temp);
        drawAllEukleidian();
    }

    public void drawAllEukleidian() {
        for (Triangle triangle : dt) {
            Pnt[] vertices = triangle.toArray(new Pnt[0]);
            if(controller.getA() && vertices[0].coord(0) == controller.getX())
                return;
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
        dt.eukleidianPlace(point);
    }
    
    public void removeSite(Pnt point){
        dt.eukleidianPlace2(point);
    }


//    public void clear() {
//        dt = new Triangulation(initialTriangle);
//    }
    
    public int genX() {
        /* 8a paragei tyxaies tetmimenes.
         * Aftes oi tetmimenes 8eloume na
         * briskontai mesa sta oria enos parallilogrammoy
         * mesa sto opoio 8a symbainoyn oi trigwnopoiiseis.
         * 
         * To parallilogrammo afto oristike, estw me syntetagmenes
         * gia ka8e simeio
         * 
         * A(6,12), B(700,12), C(700,500), D(6,500)
         * 
         * Ara an to doume sto kartesiano systima a3onwn,
         * to X menei na brisketai anamesa stis times
         * 
         * 6.0 <= X < 700.0
         * 
         */

        Random random = new Random();

        float q = random.nextFloat();
        int x = (int) Math.floor(695 * q + 6);

        /* Dioti otan dimiourgoume enan tyxaio ari8mo
         * typoy float, tote aftos vrisketai anamesa sto diastima
         * 0.0 <= X < 1
         * Pollaplasiazoyme tis aniswseis me to 695 kai katopin pros8etoume to 6
         * wste na kalypsoume ton ari8mo 6 apo to "<=" kai 701, wste na symperilaboume
         * tin timi 700 (afou einai monaxa megalytero kai oxi iso)
         */

        return x;
    }

    public int genY() {
        //Paromoia ekteloume gia to Y

        Random random = new Random();

        float q = random.nextFloat();
        int y = (int) Math.floor(489 * q + 12);

        return y;
    }
}   //end class EukleidianPanel