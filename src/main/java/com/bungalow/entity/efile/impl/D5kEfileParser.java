package com.bungalow.entity.efile.impl;

import com.bungalow.entity.edom.parser.EdomXMLReader;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.bungalow.entity.edom.io.EdomSAXReader;
import com.bungalow.entity.efile.EFileParser;
import com.bungalow.entity.efile.ETable;
import com.bungalow.entity.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengXi on 2016/9/8.
 */
public class D5kEfileParser implements EFileParser {
    @Override
    public List<ETable> parseFile(String path) throws Exception {

        //begin
        EdomSAXReader reader = new EdomSAXReader();
        File file = new File(path);
        List<ETable> tableList = new ArrayList<ETable>();

        if (file.length() != 0) {
            Document doc = reader.read(file);
            NodeList nodeList = doc.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element ele = (Element) nodeList.item(i);
                ETable table = new ETable();
                String tagName = ele.getTagName();
                ele.getAttributes();
                if (tagName.contains("::")) {
                    table.setTableName(tagName.split("::")[0]+ "::"+tagName.split("::")[1]);
                } else {
                    table.setTableName(tagName);
                }
//                table.setDate(ele.getAttribute("date"));
                String date = tagName.split("::")[2].replace("date=","");
                table.setDate(date);
                tableList.add(table);

                parseTableData(table, ele.getTextContent());

            }
        }

        return tableList;
    }

    @Override
    public List<ETable> parseString(String str) throws Exception {
        List<ETable> tableList = new ArrayList<ETable>();
//        StringReader sr = new StringReader(str);
//        InputSource is = new InputSource(sr);
//        is.setEncoding("UTF-8");
//
//        EdomSAXReader reader = new EdomSAXReader();
//        Document doc = reader.read(is);
//
//        NodeList nodeList = doc.getChildNodes();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Element ele = (Element) nodeList.item(i);
//            ETable table = new ETable();
//            String tagName = ele.getTagName();
//            if (tagName.contains("::")) {
//                table.setTableName(tagName.split("::")[0]);
//            } else {
//                table.setTableName(tagName);
//            }
//
//            table.setDate(ele.getAttribute("date"));
//            tableList.add(table);
//
//            parseTableData(table, ele.getTextContent());
//
//        }
        return tableList;
    }

    private void parseTableData(ETable table, String content) {
        String[] contentArr = content.split("\n");
        String headStr = contentArr[0].trim();

        if (headStr.startsWith("@")) {

            parseRowType(table, contentArr);
        }

    }

    private void parseRowType(ETable table, String[] contentArr) {
        String headStr = contentArr[0].trim();
        String[] headerArr = splitString(headStr.substring("@".length()));
        String[] columnNames = new String[headerArr.length];
        System.arraycopy(headerArr, 0, columnNames, 0, columnNames.length);
        table.setColumnNames(columnNames);
        if (contentArr.length > 1) {
            for (int i = 1; i < contentArr.length; i++) {
                String linestr = contentArr[i].trim();
                //System.out.println("datastr=" + linestr);


                if (linestr.startsWith("%")) {
                    //类型
                    String[] lineArr = splitString(linestr.substring(1));
                    String[] type = new String[lineArr.length];
                    System.arraycopy(lineArr, 0, type, 0, type.length);
                    table.setTypes(type);
                } else if (linestr.startsWith("$")) {
                    //梁纲引导符  单位
                    String[] lineArr = splitString(linestr.substring(1));
                    String[] unites = new String[lineArr.length];
                    System.arraycopy(lineArr, 0, unites, 0, unites.length);
                    table.setUnites(unites);

                } else if (linestr.startsWith(":")) {
                    //限值引导符
                    String[] lineArr = splitString(linestr.substring(1));
                    String[] limitValues = new String[lineArr.length];
                    System.arraycopy(lineArr, 0, limitValues, 0, limitValues.length);
                    table.setLimitValues(limitValues);

                } else if (linestr.startsWith("#")) {
                    //数据值

                    String[] lineArr = splitString(linestr.substring(1));
                    String[] data = new String[lineArr.length];
                    System.arraycopy(lineArr, 0, data, 0, data.length);
                    table.getDatas().add(data);
                }


            }
        }
    }

    private String[] splitString(String str) {
        return StringUtils.splitLineWithSpace(str);
    }

}
