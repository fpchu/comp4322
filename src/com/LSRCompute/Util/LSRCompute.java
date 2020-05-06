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
        this.network = new HashMap<String, Router>();
        for (String node:this.lsa.keySet()) {
            network.put(node, new Router(node, lsa));
        }
    }

    public HashMap<String, Router> getNetwork() { return this.network; }

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

    @Override
    public String toString() {
        return "" + this.network.size();
    }
}
