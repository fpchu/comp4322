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

        /* initialize the map which store the object Destination */
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
         * We can modify the Destination in the rMap which bring the changes to pQ as well */
        for (String s: costMap.keySet()) {
            pQ.add(costMap.get(s));
        }

        while (visited.size() < lsdb.size()) {

            Destination currentNode = pQ.poll();
            if (visited.contains(currentNode)) // predicted duplicate node due to the stupid PQ
                continue;

            HashMap<String, Integer> neighbors = getNeighborsOf(currentNode.getKey());

            for (String neighbor_id: neighbors.keySet()) {

                if (visited.contains(neighbor_id))
                    continue;

                /* The current Node key has a hashMap as value
                 * And the Value is the neighbor nodes and its cost */
                Integer cost = lsdb.get(currentNode.getKey()).get(neighbor_id);

                Integer newDistance = cost + currentNode.getDistance();

                if (newDistance < costMap.get(neighbor_id).getDistance()) {
                    pQ.add(new Destination(neighbor_id, newDistance));
                    costMap.get(neighbor_id).setDistance(newDistance);
                }
            }

            visited.add(currentNode.getKey());
        }
        return costMap;
    }

    @Override
    public String toString() {
        return "Router{" +
                "id='" + id + '\'' +
                ", routeTable=" + routeTable +
                '}';
    }
}

class Destination implements Comparable<Destination> {

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

    public void setDistance(Integer value) { this.distance = value; }

    @Override
    public int compareTo(Destination r) {
        return this.distance.compareTo(r.distance);
    }

    @Override
    public String toString() { // Debugging purpose
        return "Destination{" +
                "node='" + node + '\'' +
                ", distance=" + distance +
                '}';
    }
}
