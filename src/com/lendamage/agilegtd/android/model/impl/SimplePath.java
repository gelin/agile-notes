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
        assert(string != null);
        List<String> result = new ArrayList<String>();
        if (string.length() == 0) {
            return result;
        }
        int index = 0;
        int separatorIndex = string.indexOf(SEPARATOR, index);
        StringBuilder segment = new StringBuilder();
        while (separatorIndex >= 0) {
            int prevChar = -1;
            if (separatorIndex > 0) {
                prevChar = string.charAt(separatorIndex - 1); 
            }
            if (prevChar == ESCAPE) {
                segment.append(string.substring(index, separatorIndex - 1));
                segment.append(SEPARATOR);
            } else {
                segment.append(string.substring(index, separatorIndex));
                result.add(segment.toString());
                segment.setLength(0);
            }
            index = separatorIndex + 1;
            separatorIndex = string.indexOf(SEPARATOR, index);
        }
        segment.append(string.substring(index));
        result.add(segment.toString());
        return result;
    }

    //@Override
    public List<String> getSegments() {
        return Collections.unmodifiableList(segments);
    }
    
    //@Override
    public Path addSegment(String segment) {
        SimplePath result = new SimplePath(this);
        result.segments.add(segment);
        return result;
    }
    
    //@Override
    public Path replaceLastSegment(String segment) {
        SimplePath result = new SimplePath(this);
        if (result.segments.size() > 0) {
            result.segments.remove(result.segments.size() - 1);
        }
        result.segments.add(segment);
        return result;
    }

    //@Override
    public Path getParent() {
        SimplePath result = new SimplePath(this);
        if (result.segments.size() > 0) {
            result.segments.remove(result.segments.size() - 1);
        }
        return result;
    }
    
    //@Override
    public String getLastSegment() {
        if (segments.size() == 0) {
            return "";
        }
        return segments.get(segments.size() - 1);
    }
    
    //@Override
    public boolean isRoot() {
        return segments.size() == 0;
    }
    
    //@Override
    public boolean startsWith(Path parent) {
        if (parent == null) {
            return false;
        }
        List<String> segments = parent.getSegments();
        if (segments.size() > this.segments.size()) {
            return false;
        }
        int i = 0;
        for (String segment : segments) {
            if (!this.segments.get(i).equals(segment)) {
                return false;
            }
            i++;
        }
        return true;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((segments == null) ? 0 : segments.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimplePath other = (SimplePath) obj;
        if (segments == null) {
            if (other.segments != null)
                return false;
        } else if (!segments.equals(other.segments))
            return false;
        return true;
    }

}
