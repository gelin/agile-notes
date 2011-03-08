package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lendamage.agilegtd.model.Path;

/**
 *  Path with slash ("/") separator.
 */
public class SimplePath implements Path {

    public static final char SEPARATOR = '/';
    public static final String SEPARATOR_ESCAPE = "\\/";
    public static final char ESCAPE = '\\';
    
    /** Path segments */
    List<String> segments = new ArrayList<String>();
    
    /**
     *  Creates the path from the string.
     */
    public SimplePath(String string) {
        this.segments = parse(string);
    }
    
    /**
     *  Creates the path from another path.
     */
    public SimplePath(Path path) {
        this.segments.addAll(path.getSegments());
    }
    
    static List<String> parse(String string) {
        List<String> result = new ArrayList<String>();
        int index = 0;
        int separatorIndex = string.indexOf(SEPARATOR, index);
        while (separatorIndex >= 0) {
            int prevChar = -1;
            if (separatorIndex > 0) {
                prevChar = string.charAt(separatorIndex - 1); 
            }
            if (prevChar == ESCAPE) {
                separatorIndex = string.indexOf(SEPARATOR, separatorIndex);
                continue;
            }
            result.add(string.substring(index, separatorIndex));
            index = separatorIndex + 1;
            separatorIndex = string.indexOf(SEPARATOR, index);
        };
        result.add(string.substring(index));
        return result;
    }

    @Override
    public List<String> getSegments() {
        return Collections.unmodifiableList(segments);
    }
    
    @Override
    public Path addSegment(String segment) {
        SimplePath result = new SimplePath(this);
        result.segments.add(segment);
        return result;
    }
    
    @Override
    public Path replaceLastSegment(String segment) {
        SimplePath result = new SimplePath(this);
        if (result.segments.size() > 0) {
            result.segments.remove(result.segments.size() - 1);
        }
        result.segments.add(segment);
        return result;
    }

    @Override
    public Path getParent() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getLastSegment() {
        if (segments.size() == 0) {
            return "";
        }
        return segments.get(segments.size() - 1);
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
        while (separatorIndex >= 0) {
            result.append(segment.substring(index, separatorIndex));
            result.append(SEPARATOR_ESCAPE);
            index = separatorIndex + 1;
            separatorIndex = segment.indexOf(SEPARATOR, index);
        };
        result.append(segment.substring(index));
    }

}
