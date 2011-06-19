package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Folder;

public class SQLiteFolderTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testGetFolders() {
        SQLiteFolder parent = (SQLiteFolder)model.newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)model.newFolder("child", null);
        parent.getFolders().add(child);
        List<Folder> children = parent.getFolders();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }
    
    public void testFoldersManupulations() {
        Folder parent = model.newFolder("parent", null);
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        parent.getFolders().add(child1);
        parent.getFolders().add(child2);
        
        List<Folder> children = parent.getFolders();
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        children.remove(0);
        List<Folder> children2 = parent.getFolders();
        assertEquals(1, children2.size());
        assertEquals(child2, children.get(0));
        children2.add(child1);
        List<Folder> children3 = parent.getFolders();
        assertEquals(2, children3.size());
        assertEquals(child2, children3.get(0));
        assertEquals(child1, children3.get(1));
    }
    
}
