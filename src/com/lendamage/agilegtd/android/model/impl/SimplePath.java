package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lendamage.agilegtd.model.Path;

/**
 *  Path with slash ("/") separator.
 */
public class SimplePath implements Path {

    public static final String SEPARATOR = "/";
    public static final String SEPARATOR_ESCAPE = "\\/";
    
    /** Path segments */
    List<String> segments = new ArrayList<String>();
    
    public SimplePath(String string) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<String> getSegments() {
        return Collections.unmodifiableList(segments);
    }
    
    @Override
    public Path addSegment(String segment) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Path replaceLastSegment(String segment) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path getParent() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getLastSegment() {
        return segments.get(segments.size());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String segment : this.segments) {
            if (first) {
                first = false;
            } else {
                result.append(SEPARATOR);
            }
            escape(result, segment);
        }
        return result.toString();
    }
    
    void escape(StringBuilder result, String segment) {
        int index = 0;
        int separatorIndex = segment.indexOf(SEPARATOR, index);
        while (separatorIndex > 0) {
            result.append(segment.substring(index, separatorIndex));
            result.append(SEPARATOR_ESCAPE);
            index = separatorIndex + SEPARATOR.length();
            separatorIndex = segment.indexOf(SEPARATOR, index);
        };
        result.append(segment.substring(index));
    }

}
