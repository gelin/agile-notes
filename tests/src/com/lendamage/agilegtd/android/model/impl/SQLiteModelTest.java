package com.lendamage.agilegtd.android.model.impl;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.FolderType;

import android.test.AndroidTestCase;

public class SQLiteModelTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testNewAction() {
        SQLiteAction a1 = (SQLiteAction)model.newAction("head1", "body1");
        assertFalse(0 == a1.id);
        assertEquals("head1", a1.getHead());
        assertEquals("body1", a1.getBody());
        SQLiteAction a2 = (SQLiteAction)model.newAction("head2", "body2");
        assertFalse(a1.id == a2.id);
        assertEquals("head2", a2.getHead());
        assertEquals("body2", a2.getBody());
    }
    
    public void testNewFolder() {
        SQLiteFolder f1 = (SQLiteFolder)model.newFolder(new SimplePath(""), FolderType.ROOT);
        assertFalse(0 == f1.id);
        assertEquals("", f1.getPath().toString());
        assertEquals("", f1.getName());
        assertEquals(FolderType.ROOT, f1.getType());
        SQLiteFolder f2 = (SQLiteFolder)model.newFolder(new SimplePath("Projects"), FolderType.PROJECTS);
        assertFalse(f1.id == f2.id);
        assertEquals("Projects", f2.getPath().toString());
        assertEquals("Projects", f2.getName());
        assertEquals(FolderType.PROJECTS, f2.getType());
    }
    
    public void testNewFolderNullType() {
        SQLiteFolder f1 = (SQLiteFolder)model.newFolder(new SimplePath("null"), null);
        assertFalse(0 == f1.id);
        assertEquals("null", f1.getPath().toString());
        assertEquals("null", f1.getName());
        assertNull(f1.getType());
    }
    
    public void testNewSubfolder() {
        //TODO
    }

}
