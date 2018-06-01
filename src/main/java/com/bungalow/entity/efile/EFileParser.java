package com.bungalow.entity.efile;

import java.util.List;

/**
 * Created by ChengXi on 2016/9/8.
 */
public interface EFileParser {
    public List<ETable> parseFile(String path) throws Exception;

    public List<ETable> parseString(String str) throws Exception;
}
