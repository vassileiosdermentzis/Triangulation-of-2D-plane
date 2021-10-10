package algorithms;

import entities.graph.Graph;
import entities.point.Pnt;
import entities.triangle.Triangle;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Trigwnopoiisi kata ton algori8mo  se 2 Diastaseis.
 */
public class Triangulation extends AbstractSet<Triangle> {

    private Triangle mostRecent = null;      // Most recently "active" triangle
    private Graph<Triangle> triGraph;        // Holds triangles for navigation

    /**
     * Oles oi plevres prepei na "pesoyn" mesa sto arxiko trigwno.
     * @param triangle
    */ 
    public Triangulation (Triangle triangle) {
        triGraph = new Graph<Triangle>();
        triGraph.add(triangle);
        mostRecent = triangle;
    }

    //methods required by AbstractSet

    @Override
    public Iterator<Triangle> iterator () {
        return triGraph.nodeSet().iterator();
    }

    @Override
    public int size () {
        return triGraph.nodeSet().size();
    }

    @Override
    public String toString () {
        return "Triangulation with " + size() + " triangles";
    }

    /**
     * An kai mono an to trigwno einai melos aftis tis trigwnopoiisis
     * @param triangle
     * @return 
     */
    @Override
    public boolean contains (Object triangle) {
        return triGraph.nodeSet().contains(triangle);
    }

    /**
     * Anaferei ton "geitona" apenanti apo tin akmi (vertex) toy trigwnoy.
     * @param site
     * @param triangle
     * @return 
     */
    public Triangle neighborOpposite (Pnt site, Triangle triangle) {
        if (!triangle.contains(site))
            throw new IllegalArgumentException("Bad vertex; not in triangle");
        for (Triangle neighbor: triGraph.neighbors(triangle)) {
            if (!neighbor.contains(site)) return neighbor;
        }
        return null;
    }

    /**
     * Epistrefei to synolo twn trigwnwn poy sysxetizontai me to en logw trigwno
     * @param triangle
     * @return 
     */
    public Set<Triangle> neighbors(Triangle triangle) {
        return triGraph.neighbors(triangle);
    }

    /**
     * Epistrefei ta trigwna peri tis do8eisas plevras
     * @param site
     * @param triangle
     * @return 
     */
    public List<Triangle> surroundingTriangles (Pnt site, Triangle triangle) {
        if (!triangle.contains(site))
            throw new IllegalArgumentException("Site not in triangle");
        List<Triangle> list = new ArrayList<Triangle>();
        Triangle start = triangle;
        Pnt guide = triangle.getVertexButNot(site);        
        while (true) {
            list.add(triangle);
            Triangle previous = triangle;
            triangle = this.neighborOpposite(guide, triangle); 
            guide = previous.getVertexButNot(site, guide);
            if (triangle == start) break;
        }
        return list;
    }

    /**
     * Locate the triangle with point inside it or on its boundary.
     * @param point
     * @return 
     */
    public Triangle locate (Pnt point) {
        Triangle triangle = mostRecent;
        if (!this.contains(triangle)) triangle = null;

        // Ka8eti diasxisi (i alliws ef8eia poreia ston grafo me toys komvous)
        Set<Triangle> visited = new HashSet<Triangle>();
        while (triangle != null) {
            if (visited.contains(triangle)) { // This should never happen
                System.out.println("Warning: Caught in a locate loop");
                break;
            }
            visited.add(triangle);
            
            // Ta ekaterw8en simeia
            Pnt corner = point.isOutside(triangle.toArray(new Pnt[0]));
            if (corner == null) return triangle;
            triangle = this.neighborOpposite(corner, triangle);
        }
        //Elegxos an exoyn elegx8ei i diasxis8ei oloi oi komvoi
        System.out.println("Warning: Checking all triangles for " + point);
        for (Triangle tri: this) {
            if (point.isOutside(tri.toArray(new Pnt[0])) == null) return tri;
        }
        // Periptwsi poy den yparxei trigwno
        System.out.println("Warning: No triangle holds " + point);
        return null;
    }

    /**
     * Topo8etisi neas plevras
     * @param site
     */
    public void eukleidianPlace (Pnt site) {

        
        Triangle triangle = locate(site);
       
        if (triangle == null)
            throw new IllegalArgumentException("No containing triangle");
        if (triangle.contains(site)) return;

        
        Set<Triangle> cavity = getCavity(site, triangle);
        mostRecent = update(site, cavity);
    }
    
    //parallagi gia diagrafi komvoy
    public void eukleidianPlace2 (Pnt site) {

        
        Triangle triangle = locate(site);
       
        if (triangle == null)
            throw new IllegalArgumentException("No containing triangle");
        if (triangle.contains(site)) triangle.clear();

        
        Set<Triangle> cavity = getCavity(site, triangle);
        mostRecent = update(site, cavity);
    }

    /**
     * Determine the cavity caused by site.
     */
    private Set<Triangle> getCavity (Pnt site, Triangle triangle) {
        Set<Triangle> encroached = new HashSet<Triangle>();
        Queue<Triangle> toBeChecked = new LinkedList<Triangle>();
        Set<Triangle> marked = new HashSet<Triangle>();
        toBeChecked.add(triangle);
        marked.add(triangle);
        while (!toBeChecked.isEmpty()) {
            triangle = toBeChecked.remove();
            if (site.vsCircumcircle(triangle.toArray(new Pnt[0])) == 1)
                continue; // Site outside triangle => triangle not in cavity
            encroached.add(triangle);
            // Check the neighbors
            for (Triangle neighbor: triGraph.neighbors(triangle)){
                if (marked.contains(neighbor)) continue;
                marked.add(neighbor);
                toBeChecked.add(neighbor);
            }
        }
        return encroached;
    }

    /**
     * Update the triangulation by removing the cavity triangles and then
     * filling the cavity with new triangles.
     * @param site the site that created the cavity
     * @param cavity the triangles with site in their circumcircle
     * @return one of the new triangles
     */
    private Triangle update (Pnt site, Set<Triangle> cavity) {
        Set<Set<Pnt>> boundary = new HashSet<Set<Pnt>>();
        Set<Triangle> theTriangles = new HashSet<Triangle>();

        // Find boundary facets and adjacent triangles
        for (Triangle triangle: cavity) {
            theTriangles.addAll(neighbors(triangle));
            for (Pnt vertex: triangle) {
                Set<Pnt> facet = triangle.facetOpposite(vertex);
                if (boundary.contains(facet)) boundary.remove(facet);
                else boundary.add(facet);
            }
        }
        theTriangles.removeAll(cavity);        // Adj triangles only

        // Remove the cavity triangles from the triangulation
        for (Triangle triangle: cavity) triGraph.remove(triangle);

        // Build each new triangle and add it to the triangulation
        Set<Triangle> newTriangles = new HashSet<Triangle>();
        for (Set<Pnt> vertices: boundary) {
            vertices.add(site);
            Triangle tri = new Triangle(vertices);
            triGraph.add(tri);
            newTriangles.add(tri);
        }

        // Update the graph links for each new triangle
        theTriangles.addAll(newTriangles);    // Adj triangle + new triangles
        for (Triangle triangle: newTriangles)
            for (Triangle other: theTriangles)
                if (triangle.isNeighbor(other))
                    triGraph.add(triangle, other);

        // Return one of the new triangles
        return newTriangles.iterator().next();
    }

    /**
     * Main program; used for testing.
     */
//    public static void main (String[] args) {
//        Triangle tri =
//            new Triangle(new Pnt(-10,10), new Pnt(10,10), new Pnt(0,-10));
//        System.out.println("Triangle created: " + tri);
//        Triangulation dt = new Triangulation(tri);
//        System.out.println("DelaunayTriangulation created: " + dt);
//        dt.eukleidianPlace(new Pnt(0,0));
//        dt.eukleidianPlace(new Pnt(1,0));
//        dt.eukleidianPlace(new Pnt(0,1));
//        System.out.println("After adding 3 points, we have a " + dt);
//        Triangle.moreInfo = true;
//        System.out.println("Triangles: " + dt.triGraph.nodeSet());
//    }
}