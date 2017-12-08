package com.leetcode.tree.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CloneGraph {
    
    class UndirectedGraphNode {
        int label;
        List<UndirectedGraphNode> neighbors;
        UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
    };

    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        Map<UndirectedGraphNode, UndirectedGraphNode> map = new HashMap<UndirectedGraphNode, UndirectedGraphNode>();
        fillMap(node, map);
        Set<UndirectedGraphNode> keySet = map.keySet();
        for (UndirectedGraphNode key : keySet) {
            UndirectedGraphNode newNode = map.get(key);
            List<UndirectedGraphNode> neighbors = key.neighbors;
            for (UndirectedGraphNode neighbor : neighbors) {
                newNode.neighbors.add(map.get(neighbor));
            }
        }
        return map.get(node);
    }
    

    private void fillMap(UndirectedGraphNode node, Map<UndirectedGraphNode, UndirectedGraphNode> map) {
        if(map.containsKey(node)) {
            return;
        }else{
            UndirectedGraphNode newNode = new UndirectedGraphNode(node.label);
            map.put(node, newNode);
            List<UndirectedGraphNode> list = node.neighbors;
            for (int i = 0; i < list.size(); i++) {
                fillMap(list.get(i),map);
            }    
        }
    }
}
