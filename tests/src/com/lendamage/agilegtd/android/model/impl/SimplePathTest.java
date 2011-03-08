package com.lendamage.agilegtd.android.model.impl;

import com.lendamage.agilegtd.model.Path;

import android.test.AndroidTestCase;

public class SimplePathTest extends AndroidTestCase {
    
    public void testParse() {
        Path path = new SimplePath("abc/def");
        assertEquals("abc/def", path.toString());
    }
    
    public void testEscape() {
        Path path1 = new SimplePath("abc/def");
        Path path2 = path1.addSegment("egh/ifk");
        assertEquals("abc/def/egh\\/ifk", path2.toString());
    }

}
