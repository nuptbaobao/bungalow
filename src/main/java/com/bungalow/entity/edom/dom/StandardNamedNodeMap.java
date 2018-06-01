package com.bungalow.entity.edom.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengXi on 2016/9/9.
 */
public class StandardNamedNodeMap implements NamedNodeMap {

    List<Attr> attrs = new ArrayList<Attr>();

    public StandardNamedNodeMap(List<Attr> attrs) {
        this.attrs = attrs;
    }

    @Override
    public Node getNamedItem(String name) {
        return null;
    }

    @Override
    public Node setNamedItem(Node arg) throws DOMException {
        return null;
    }

    @Override
    public Node removeNamedItem(String name) throws DOMException {
        return null;
    }

    @Override
    public Node item(int index) {
        return null;
    }

    @Override
    public int getLength() {
        return attrs.size();
    }

    @Override
    public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }

    @Override
    public Node setNamedItemNS(Node arg) throws DOMException {
        return null;
    }

    @Override
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }
}
