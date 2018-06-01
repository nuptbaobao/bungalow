package com.bungalow.entity.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChengXi on 2016/9/8.
 */
public class StringUtils {

    private final static char[] wrappers = new char[]{'\'', '\"'};

    public static String[] splitLineWithSpace(String line) {

        List<String> list = new ArrayList<String>();
        parseSection(list, line, 0, 0);


        String[] arr = new String[list.size()];
        return list.toArray(arr);

    }

    private static void parseSection(List<String> list, String line, int start, int pos) {
        char chr = '0';
        char wrapper = '\'';
        boolean sectionEnd = false;
        boolean lineEnd = false;
        boolean hasWrapper = false;

        //跳过空白字符
        do {
            //lineEnd=(pos>=line.length());
            if (pos < line.length())
                chr = line.charAt(pos++);
            else break;

        } while (isSpace(chr));


        if (isWrapper(chr)) {
            wrapper = chr;
            hasWrapper = true;
            start = pos;
        } else {
            start = --pos;
        }

        while (!sectionEnd && !lineEnd) {
            chr = line.charAt(pos++);
            if (pos >= line.length()) {
                lineEnd = true;
            } else if (hasWrapper) {
                if (chr == wrapper) {
                    sectionEnd = true;
                }
            } else {
                if (isSpace(chr))
                    sectionEnd = true;
            }
        }
        //pos--;
        String sectionStr = "";
        if (hasWrapper) {
            sectionStr = line.substring(start, pos - 1);
        } else {
            sectionStr = line.substring(start, pos).trim();
        }

        //System.out.println(sectionStr);
        list.add(sectionStr);
        if (!lineEnd) {
            parseSection(list, line, start, pos);
        }
    }

    /**
     * 判断字符是否是单双引号
     *
     * @param chr
     * @return
     */
    private static boolean isWrapper(char chr) {
        for (char c : wrappers) {
            if (c == chr) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpace(char chr) {
        return (chr == ' ' || chr == '\n' || chr == '\t' || chr == '\r' || chr == '\f');
    }

    public static boolean isInteger(String str) {
        String regx = "\\d+";
        return str.matches(regx);
    }

}
