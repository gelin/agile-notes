package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.FolderType;

public class SQLiteModelTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
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
        SQLiteFolder f1 = (SQLiteFolder)model.newFolder("", FolderType.ROOT);
        assertFalse(0 == f1.id);
        assertEquals("", f1.getPath().toString());
        assertEquals("", f1.getName());
        assertEquals(FolderType.ROOT, f1.getType());
        SQLiteFolder f2 = (SQLiteFolder)model.newFolder("Projects", FolderType.PROJECTS);
        assertFalse(f1.id == f2.id);
        assertEquals("Projects", f2.getPath().toString());
        assertEquals("Projects", f2.getName());
        assertEquals(FolderType.PROJECTS, f2.getType());
    }
    
    public void testNewFolderNullType() {
        SQLiteFolder f1 = (SQLiteFolder)model.newFolder("null", null);
        assertFalse(0 == f1.id);
        assertEquals("null", f1.getPath().toString());
        assertEquals("null", f1.getName());
        assertNull(f1.getType());
    }
    
    public void testNewSubfolder() {
        SQLiteFolder parent = (SQLiteFolder)model.newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)model.newFolder("child", null);
        assertFalse(parent.id == child.id);
        assertEquals("parent", parent.getPath().toString());
        assertEquals("parent", parent.getName());
        assertFalse(0 == child.id);
        assertEquals("child", child.getPath().toString());
        assertEquals("child", child.getName());
        parent.getFolders().add(child);
        assertEquals("parent/child", child.getPath().toString());
        SQLiteFolder child2 = (SQLiteFolder)model.getFolder(new SimplePath("parent/child"));
        assertEquals(child, child2);
    }
    
    public void testGetRootFolder() {
        SQLiteFolder root = (SQLiteFolder)model.getRootFolder();
        assertFalse(0 == root.id);
        assertEquals("", root.getPath().toString());
        assertEquals("", root.getName());
        assertEquals(FolderType.ROOT, root.getType());
    }
    
    public void testGetFolder() {
        SQLiteFolder newFolder = (SQLiteFolder)model.newFolder("folder", null);
        SQLiteFolder folder = (SQLiteFolder)model.getFolder(new SimplePath("folder"));
        assertEquals(newFolder.id, folder.id);
        assertEquals("folder", folder.getPath().toString());
        assertEquals("folder", folder.getName());
    }
    
    public void testTransactionCommit() {
        SQLiteFolder parent = (SQLiteFolder)model.newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)model.newFolder("child", null);
        parent.getFolders().add(child);
        SQLiteModel model2 = new SQLiteModel(getContext(), "agile-gtd-test.db");
        assertNotNull(model2.getRootFolder());
        assertNotNull(model2.getFolder(new SimplePath("parent")));
        assertNotNull(model2.getFolder(new SimplePath("parent/child")));
    }

}
