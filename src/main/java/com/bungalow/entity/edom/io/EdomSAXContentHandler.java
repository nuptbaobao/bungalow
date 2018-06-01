package com.bungalow.entity.edom.io;

import com.bungalow.entity.edom.dom.StandardDocument;
import com.bungalow.entity.edom.dom.StandardElement;
import com.bungalow.entity.edom.utils.ArrayStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by ChengXi on 2016/9/9.
 */
public class EdomSAXContentHandler extends DefaultHandler {

    private ArrayStack<Node> elmentStack = null;

    private Document document;

    public EdomSAXContentHandler() {
        this(new ArrayStack<Node>());
    }

    public EdomSAXContentHandler(ArrayStack<Node> elmentStack) {
        this.elmentStack = elmentStack;
    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        Element ele = (Element) elmentStack.peek();
        ele.setTextContent(new String(ch, start, length));
    }

    @Override
    public void endDocument() throws SAXException {
        elmentStack.pop();
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        elmentStack.pop();
    }

    @Override
    public void startDocument() throws SAXException {
        document = new StandardDocument();
        elmentStack.push(document);
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        Element ele = new StandardElement(localName);
        for (int i = 0; i < attributes.getLength(); i++) {
            ele.setAttribute(attributes.getLocalName(i), attributes.getValue(i));
        }
        elmentStack.peek().appendChild(ele);
        this.elmentStack.push(ele);
    }


    public Document getDocument() {
        return document;
    }
}
