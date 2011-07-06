package com.lendamage.agilegtd.model;

import java.util.List;

/**
 *  Full path of the folder.<br>
 *  Contains string path segments.<br> 
 *  Can be parsed from the string and represented as a string.<br>
 *  The path is unmodifiable.
 */
public interface Path {
    
    /**
     *  Returns the segments of the path
     *  @return unmodifiable list
     */
    List<String> getSegments();
    
    /**
     *  Creates a new path with new segment added to the path end.
     */
    Path addSegment(String segment);
    
    /**
     *  Creates a new path with the last segment replaced with provided value.
     */
    Path replaceLastSegment(String segment);
    
    /**
     *  Creates a new path, parent to this one.<br>
     *  For root path, returns root again.
     */
    Path getParent();
    
    /**
     *  Gets the name of the last segment.
     */
    String getLastSegment();
    
    /**
     *  Returns true if the path is root.
     */
    boolean isRoot();
    
    /**
     *  Converts path to string.
     */
    String toString();

}
