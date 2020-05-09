package com.LSRCompute.Util;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashSet;

public class Router {

    private String id;
    private HashMap<String, HashMap<String, Integer>> lsdb;
    private HashMap<String, Destination> routeTable;

    public Router(String id, HashMap<String, HashMap<String, Integer>> lsa) {
        this.id = id;
        this.lsdb = lsa;
        this.routeTable = dijkstra();
    }

    public HashMap<String, Integer> getNeighborsOf(String node) {
        return lsdb.get(node);
    }

    public HashMap<String, Destination> dijkstra() {

        HashMap<String, Destination> costMap = new HashMap<String, Destination>();

        /* initialize the map which store the Destination object */
        for (String node: this.lsdb.keySet()) {
            if (node == id) {
                costMap.put(node, new Destination(node, 0));
            }
            else {
                costMap.put(node, new Destination(node, Integer.MAX_VALUE));
            }
        }

        PriorityQueue<Destination> pQ = new PriorityQueue<Destination>();
        HashSet<String> visited = new HashSet<String>();

        /* PriorityQueue store the object of the Destination which come from rMap.
         * We can modify the Destination in the rMap which bring the changes to pQ as well
         * I need a deep copy of object instead of shallow one
         */

        for (String s: costMap.keySet()) {
            pQ.add(costMap.get(s).deepCopy());
        }

        /* Initialize the Path, Start with the source */
        for (String s: costMap.keySet()) {
            costMap.get(s).appendPath(id);
        }

        while (visited.size() < lsdb.size()) {
            System.out.println("I am router: " + id);
            System.out.println("Top of the pQ: " + pQ.peek());
            Destination currentNode = pQ.poll(); // from starting point to all of the nodes

            System.out.println("Current Node: " + currentNode.getKey());
            if (visited.contains(currentNode.getKey())) {// predicted duplicate node due to the stupid PQ
                System.out.println(currentNode.getKey() + " has been visited.");
                continue;
            }

            HashMap<String, Integer> neighbors = getNeighborsOf(currentNode.getKey());
            System.out.println("neighbors: " + neighbors);
            if (neighbors == null) {
                /* isolated node should not be connected */
                continue;
            }

            for (String neighbor_id: neighbors.keySet()) {

                if (visited.contains(neighbor_id))
                    continue;

                /* The current Node key has a hashMap as value
                 * And the Value is the neighbor nodes and its cost */
                Integer cost = lsdb.get(currentNode.getKey()).get(neighbor_id);

                Integer newDistance = cost + currentNode.getDistance();

                if (newDistance < costMap.get(neighbor_id).getDistance()) {
                    /* update distance cost */
                    Destination updateObject = new Destination(neighbor_id, newDistance);
                    System.out.println("The update");

                    costMap.get(neighbor_id).setDistance(newDistance);

                    /* update path */
                    ArrayList<String> newPath = currentNode.copyPath();
                    newPath.add(neighbor_id);
                    costMap.get(neighbor_id).overwritePath(newPath);
                    updateObject.overwritePath(newPath);
                    pQ.add(updateObject);
                }
            }
            System.out.println("pQ After Update: " + pQ.toString());
            visited.add(currentNode.getKey());
        }
        return costMap;
    }

    public String printPath(String node) {

        String s = "";
        for (String st: routeTable.get(node).getPath()) {
            s += st + "->";
        }
        return s.substring(0, s.length()-2);

    }

    public String printAllPath() {
        String s = "";
        for (String st: routeTable.keySet()) {
            if (st == id)
                continue;
            s += "Destination " + st + ": ";
            s += printPath(st) + " " + "Cost:" + routeTable.get(st).getDistance() + "\n";
        }
        return s;
    }

    @Override
    public String toString() {
        return "Node: " + id + "\n" + "lsdb=" + lsdb + "\n";
    }
}

class Destination implements Comparable<Destination>, Cloneable {

    private String node;
    private Integer distance;
    private ArrayList<String> path;

    public Destination(String node, Integer distance) {
        this.node = node;
        this.distance = distance;
        this.path = new ArrayList<String>();
    }

    public String getKey() { return node; }

    public Integer getDistance() { return distance; }

    public ArrayList<String> getPath() { return path; }

    public void setDistance(Integer value) { this.distance = value; }

    public void appendPath(String s) { this.path.add(s); }

    public void overwritePath(ArrayList<String> p) { this.path = p; }

    public ArrayList<String> copyPath() {
        ArrayList<String> p = new ArrayList<String>();
        for (String s: path)
            p.add(s);
        return p;
    }

    public Destination deepCopy() {
        Destination d = new Destination(node, distance);
        for (String p: path)
        {
            d.appendPath(new String(p));
        }
        return d;
    }

    @Override
    public int compareTo(Destination r) {
        return this.distance.compareTo(r.distance);
    }

    @Override
    public String toString() { // Debugging purpose

        String p = "";

        for (String d: path) {
            p += (d + "->");
        }

        return "Destination{" +
                "node='" + node + '\'' +
                ", path=" + path +
                ", distance=" + distance +
                '}';
    }
}
