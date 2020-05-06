package com.LSRCompute.Util;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.HashSet;

import java.util.*;


public class Router {

    public String id;
    private HashMap<String, HashMap<String, Integer>> lsdb;
    private HashMap<String, ArrayList<String>> routeTable;

    public Router(String id, HashMap<String, HashMap<String, Integer>> lsa) {
        this.id = id;
        this.lsdb = lsa;
        this.routeTable = this.compute_routeTable();
    }

    public HashMap<String, Integer> getNeighbors() {
        return lsdb.get(id);
    }

    HashMap<String, ArrayList<String>> compute_routeTable() {
        routeTable = new HashMap<String, ArrayList<String>>();
        for (String node:this.lsdb.keySet()) {
            routeTable.put(node, shortest_path(id, node));
        }
        return routeTable;
    }

    public ArrayList<String> shortest_path(String source, String target) {

        HashSet<String> visited = new HashSet<>();
        PriorityQueue<Pair> pQ = new PriorityQueue<Pair>();

        // Add the source node to the priorityQueue
        pQ.add(new Pair(source, 0));
        // Add all other node to the priorityQueue Setting the distance to infinity
        for (String node:this.lsdb.keySet()) {
            if (node == id) continue;
            pQ.add(new Pair(node, Integer.MAX_VALUE));
        }

        


    public ArrayList<String> shortest_path(String source, String target) {

        HashSet<String> Settled = new HashSet<String>();
        PriorityQueue<HashMap<String, Integer>> Queue = new PriorityQueue<>()


        return null;
    }

}

class Pair implements Comparable<Pair> {

    private String node;
    private Integer distance;

    public Pair(String node, Integer distance) {
        this.node = node;
        this.distance = distance;
    }

    public String getKey() { return node; }

    public Integer getValue() { return distance; }

    @Override
    public int compareTo(Pair q) {
        return this.distance.compareTo(q.distance);
    }
}
