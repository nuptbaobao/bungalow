package com.bungalow.entity.edom.io;

import com.bungalow.entity.edom.parser.DdomSAXParser;
import com.bungalow.entity.edom.parser.EdomXMLReader;
import com.bungalow.entity.edom.utils.DocumentException;
import com.bungalow.entity.utils.EfileUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ChengXi on 2016/9/8.
 */
public class EdomSAXReader {
    private XMLReader xmlReader;
    private String encoding;

    public Document read(File file) throws DocumentException {
        try {

            InputSource source = new InputSource(new FileInputStream(file));
            if (this.encoding == null) {
                this.encoding = EfileUtils.getCharset(file);
            }

            if (this.encoding != null) {
                source.setEncoding(this.encoding);
            }
            String path = file.getAbsolutePath();

            if (path != null) {
                // Code taken from Ant FileUtils
                StringBuffer sb = new StringBuffer("file://");

                // add an extra slash for filesystems with drive-specifiers
                if (!path.startsWith(File.separator)) {
                    sb.append("/");
                }

                path = path.replace('\\', '/');
                sb.append(path);

                source.setSystemId(sb.toString());
            }

            return read(source);
        } catch (FileNotFoundException e) {
            throw new DocumentException(e.getMessage(), e);
        }
    }

    public Document read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = getXMLReader();
            EdomSAXContentHandler contentHandler = createContentHandler();
            char ch[] = {'\t'};
//            contentHandler.ignorableWhitespace(ch, 1, 96);
            reader.setContentHandler(contentHandler);
            reader.parse(in);
            in.getByteStream().close();
            return contentHandler.getDocument();
        } catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();

                if (systemId == null) {
                    systemId = "";
                }

                String message = "Error on line "
                        + parseException.getLineNumber() + " of document "
                        + systemId + " : " + parseException.getMessage();

                throw new DocumentException(message, e);
            } else {
                throw new DocumentException(e.getMessage(), e);
            }
        }
    }

    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
        }

        return xmlReader;
    }

    private XMLReader createXMLReader() throws SAXException {
        return new DdomSAXParser().getXMLReader();
    }

    private EdomSAXContentHandler createContentHandler() {
        return new EdomSAXContentHandler();
    }
}
