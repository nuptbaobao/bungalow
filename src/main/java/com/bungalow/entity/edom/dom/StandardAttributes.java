package com.bungalow.entity.edom.dom;

import org.xml.sax.Attributes;

import java.util.LinkedHashMap;

/**
 * Created by ChengXi on 2016/9/12.
 */
public class StandardAttributes implements Attributes {

    private LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();

    public StandardAttributes() {
    }

    public void put(String name, String value) {
        attributes.put(name, value);
    }

    @Override
    public int getLength() {
        return attributes.size();
    }

    @Override
    public String getURI(int index) {
        return null;
    }

    @Override
    public String getLocalName(int index) {
        int i = 0;
        for (String key : attributes.keySet()) {
            if (i == index)
                return key;
            i++;
        }
        return null;
    }

    @Override
    public String getQName(int index) {
        return getLocalName(index);
    }

    @Override
    public String getType(int index) {
        return null;
    }

    @Override
    public String getValue(int index) {
        int i = 0;
        for (String value : attributes.values()) {
            if (i == index) {
                return value;
            }
            i++;
        }
        return null;
    }

    @Override
    public int getIndex(String uri, String localName) {
        return 0;
    }

    @Override
    public int getIndex(String qName) {
        int i = 0;
        for (String key : attributes.keySet()) {
            if (key.equals(qName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public String getType(String uri, String localName) {
        return null;
    }

    @Override
    public String getType(String qName) {
        return null;
    }

    @Override
    public String getValue(String uri, String localName) {
        return null;
    }

    @Override
    public String getValue(String qName) {
        return attributes.get(qName);
    }
}
