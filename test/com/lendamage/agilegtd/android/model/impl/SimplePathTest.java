package com.lendamage.agilegtd.android.model.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lendamage.agilegtd.model.Path;

public class SimplePathTest {
    
    @Test
    public void testParse() {
        Path path = new SimplePath("abc/def");
        assertEquals("abc/def", path.toString());
    }
    
    @Test
    public void testParseWithEscape() {
        Path path = new SimplePath("abc/def\\/ght");
        assertEquals("abc/def\\/ght", path.toString());
    }
    
    @Test
    public void testEscape() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.addSegment("egh/ifk");
        assertEquals("abc/def/egh\\/ifk", path2.toString());
    }
    
    @Test
    public void testAddSegment() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.addSegment("ghi");
        assertEquals("abc/def/ghi", path2.toString());
    }
    
    @Test
    public void testReplaceLastSegment() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.replaceLastSegment("ghi");
        assertNotSame(path1, path2);
        assertEquals("abc/ghi", path2.toString());
    }
    
    @Test
    public void testGetParent() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.getParent();
        assertEquals("abc", path2.toString());
        Path path3 = path2.getParent();
        assertEquals("", path3.toString());
    }
    
    @Test
    public void testGetLastSegment() {
        Path path1 = new SimplePath("abc/def");
        assertEquals("def", path1.getLastSegment());
        Path path2 = new SimplePath("abc/def\\/ght");
        assertEquals("def/ght", path2.getLastSegment());
    }

}
