package com.LSRCompute.Util;

import java.util.*;


public class Router {

    public String id;
    private HashMap<String, HashMap<String, Integer>> lsdb;
    private HashMap<String, Integer> neighbors;
    private HashMap<String, Integer> costMap;
    private HashMap<String, ArrayList<String>> routeTable;

    public Router(String id, HashMap<String, HashMap<String, Integer>> lsa, HashMap<String, Integer> neighbors) {
        this.id = id;
        this.lsdb = lsa;
        this.neighbors = neighbors;
        this.costMap = this.compute_costMap();
        this.routeTable = this.compute_routeTable();
    }

    public HashMap<String, Integer> getNeighbors() {
        return this.lsdb.get(this.id);
    }

    HashMap<String, Integer> compute_costMap() {
        HashMap<String, Integer> costMap = new HashMap<String, Integer>();
        for (String node:this.lsdb.keySet()) {
            costMap.put(node, shortest_path_cost(this.id, node));
        }
        return costMap;
    }

    HashMap<String, ArrayList<String>> compute_routeTable() {
        routeTable = new HashMap<String, ArrayList<String>>();
        for (String node:this.lsdb.keySet()) {
            this.routeTable.put(node, shortest_path(this.id, node));
        }
        return routeTable;
    }


    int shortest_path_cost(String source, String target) {
        return Integer.MAX_VALUE;
    }

    public ArrayList<String> shortest_path(String source, String target) {

        HashSet<String> visited = new HashSet<>();
        PriorityQueue<Pair> pQ = new PriorityQueue<Pair>();




        return null;
    }

}

class Pair implements Comparable<Pair> {

    private String node;
    private Integer distance;

    public Pair(String node, Integer weight) {
        this.node = node;
        this.distance = weight;
    }

    public String getKey() { return this.node; }

    public Integer getValue() { return this.distance; }

    @Override
    public int compareTo(Pair q) {
        return this.distance.compareTo(q.distance);
    }
}