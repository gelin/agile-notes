/*
    Agile GTD. Flexible implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
     *  Returns true if this path is a child (or subchild) of the specified path,
     *  i.e. it starts with the specified path.
     */
    boolean startsWith(Path parent);
    
    /**
     *  Converts path to string.
     */
    String toString();

}
