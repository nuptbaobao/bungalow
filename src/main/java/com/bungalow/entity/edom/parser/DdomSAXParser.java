package com.bungalow.entity.edom.parser;

import org.xml.sax.*;

import javax.xml.parsers.SAXParser;

/**
 * Created by ChengXi on 2016/9/9.
 */
public class DdomSAXParser extends SAXParser {
    @Override
    public Parser getParser() throws SAXException {
        return null;
    }

    @Override
    public XMLReader getXMLReader() throws SAXException {
        return new EdomXMLReader();
    }

    @Override
    public boolean isNamespaceAware() {
        return false;
    }

    @Override
    public boolean isValidating() {
        return false;
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }
}
