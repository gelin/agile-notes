package com.lendamage.agilegtd.android.model.impl;

import com.lendamage.agilegtd.model.Path;

import android.test.AndroidTestCase;

public class SimplePathTest extends AndroidTestCase {
    
    public void testParse() {
        Path path = new SimplePath("abc/def");
        assertEquals("abc/def", path.toString());
    }
    
    public void testParseWithEscape() {
        Path path = new SimplePath("abc/def\\/ght");
        assertEquals("abc/def\\/ght", path.toString());
    }
    
    public void testEscape() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.addSegment("egh/ifk");
        assertEquals("abc/def/egh\\/ifk", path2.toString());
    }
    
    public void testAddSegment() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.addSegment("ghi");
        assertEquals("abc/def/ghi", path2.toString());
    }
    
    public void testReplaceLastSegment() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.replaceLastSegment("ghi");
        assertNotSame(path1, path2);
        assertEquals("abc/ghi", path2.toString());
    }
    
    public void testGetParent() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.getParent();
        assertEquals("abc", path2.toString());
        Path path3 = path2.getParent();
        assertEquals("", path3.toString());
    }
    
    public void testGetLastSegment() {
        Path path1 = new SimplePath("abc/def");
        assertEquals("def", path1.getLastSegment());
        Path path2 = new SimplePath("abc/def\\/ght");
        assertEquals("def/ght", path2.getLastSegment());
    }
    
    public void testIsRoot() {
        Path path1 = new SimplePath("abc/def");
        assertFalse(path1.isRoot());
        Path path2 = path1.getParent();
        assertFalse(path2.isRoot());
        Path path3 = path2.getParent();
        assertTrue(path3.isRoot());
        Path path4 = path3.getParent();
        assertTrue(path4.isRoot());
    }
    
    public void testAddSegmentToRoot() {
        Path path1 = new SimplePath("");
        assertTrue(path1.isRoot());
        Path path2 = path1.addSegment("folder");
        assertFalse(path2.isRoot());
        assertEquals(path1, path2.getParent());
        assertEquals("folder", path2.toString());
    }

}
