package com.LSRCompute.Util;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;


public class Router {

    public String id;
    private HashMap<String, HashMap<String, Integer>> lsdb;
    private HashMap<String, Integer> neighbors;
    private HashMap<String, Integer> costMap;
    private HashMap<String, ArrayList<String>> routeTable;

    public Router(String id, HashMap<String, HashMap<String, Integer>> lsa) {
        this.id = id;
        this.lsdb = lsa;
        this.neighbors = this.getNeighbors();

        // Initialize the costMap iteratively
        this.costMap = new HashMap<String, Integer>();
        for (String node:this.getAllNodes()) {
            this.costMap.put(node, shortest_path_cost(this.id, node));
        }

        // Initialize the routeTable iteratively
        this.routeTable = new HashMap<String, ArrayList<String>>();
        for (String node:this.getAllNodes()) {
            this.routeTable.put(node, shortest_path(this.id, node));
        }
    }

    public HashMap<String, Integer> getNeighbors() {
        return this.lsdb.get(this.id);
    }

    public ArrayList<String> getAllNodes() {
        Iterator it = lsdb.entrySet().iterator();

        ArrayList<String> xs = new ArrayList<>();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            String key = pair.getKey().toString();
            if (key != this.id)
                xs.add(key);
            it.remove(); // avoids a ConcurrentModificationException
        }
        return xs;
    }
    int shortest_path_cost(String source, String target) {
        return Integer.MAX_VALUE;
    }

    public ArrayList<String> shortest_path(String source, String target) {
        return null;
    }

}
