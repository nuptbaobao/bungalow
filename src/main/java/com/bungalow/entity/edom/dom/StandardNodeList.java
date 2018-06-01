package com.bungalow.entity.edom.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengXi on 2016/9/9.
 */
public class StandardNodeList implements NodeList {
    private List<Node> nodes = new ArrayList<Node>();

    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public Node item(int index) {
        return nodes.get(index);
    }

    @Override
    public int getLength() {
        return nodes.size();
    }
}
