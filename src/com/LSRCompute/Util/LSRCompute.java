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

    public HashMap<String, HashMap<String, Integer>> getLsa() { return lsa; }
    public HashMap<String, Router> getNetwork() { return this.network; }


    public void update() {
        this.network = new HashMap<String, Router>();
        for (String node:this.lsa.keySet()) {
            network.put(node, new Router(node, lsa));
        }
    }

    public void addNode(HashMap<String, HashMap<String, Integer>> newEntry) {
        // Add the routers
        lsa.putAll(newEntry);
        // add the links
        for (String r: newEntry.keySet()) {
            for (String sr: lsa.get(r).keySet()) {
                lsa.get(sr).put(r, newEntry.get(r).get(sr));
            }
        }
    }

    public void deleteNode(String router) {
        // remove the router
        lsa.remove(router);
        // remove the connection link
        for (String r: lsa.keySet()) {
            if (lsa.get(r).containsKey(router))
                lsa.get(r).remove(router);
        }
    }

    public void deleteLink(String link) {
        String[] pair = link.split("->");
        String key1 = pair[0];
        String key2 = pair[1];
        lsa.get(key1).remove(key2);
        lsa.get(key2).remove(key1);

    }

    public String routerPaths(String router) {
        return network.get(router).printAllPath();
    }

    public String allRoutersPath() {
        String s = "";

        for (String router: network.keySet()) {
            s += "Starting Point: " + router + "\n";
            s += network.get(router).printAllPath();
            s += "\n";
        }

        //s += network.get("A").printAllPath();
        return s;
    }
}
