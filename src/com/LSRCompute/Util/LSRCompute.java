package com.LSRCompute.Util;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import com.LSRCompute.Util.Router;

public class LSRCompute {

    private HashMap<String, HashMap<String, Integer>> lsa;
    private HashMap<String, Router> network;

    public LSRCompute(HashMap<String, HashMap<String, Integer>> lsa) {
        this.lsa = lsa;
        for (String node:this.lsa.keySet()) {
            network.put(node, new Router(node, lsa));
        }
    }

    private HashMap<String, Integer> getNeighborOf(String node) {
        /* get the neighbor of the node with its costs */
        return this.lsa.get(node);
    }

    public void update() {}

    public void addNode(String node) {
        lsa.put(node, null);
        this.update();
    }

    public void addLink(String node1, String node2, int weight) {
        this.lsa.get(node1).put(node2, weight);
        this.lsa.get(node2).put(node1, weight);
        this.update();
    }

}
