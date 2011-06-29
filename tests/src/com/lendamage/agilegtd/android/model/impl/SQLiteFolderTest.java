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
    
    public void testFolderAddRemove() {
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
        Folder child3 = model.newFolder("child3", null);
        children2.add(child3);
        List<Folder> children3 = parent.getFolders();
        assertEquals(2, children3.size());
        assertEquals(child2, children3.get(0));
        assertEquals(child3, children3.get(1));
    }
    
    public void testFolderAddToLocation() {
        Folder parent = model.newFolder("parent", null);
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        parent.getFolders().add(child1);
        parent.getFolders().add(0, child2);
        
        List<Folder> children = parent.getFolders();
        assertEquals(2, children.size());
        assertEquals(child2, children.get(0));
        assertEquals(child1, children.get(1));
    }
    
    public void testFolderRearrange() {
        Folder parent = model.newFolder("parent", null);
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        parent.getFolders().add(child1);
        parent.getFolders().add(child2);
        List<Folder> children = parent.getFolders();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        
        children.add(child1);    //add to the end
        assertEquals("the size should not increase", 2, children.size());
        List<Folder> children2 = parent.getFolders();
        assertEquals(2, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child1, children2.get(1));
    }
    
    public void testFolderRearrangeToLocation1() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        Folder child3 = model.newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(0, child3);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child3, children2.get(0));
        assertEquals(child1, children2.get(1));
        assertEquals(child2, children2.get(2));
    }
    
    public void testFolderRearrangeToLocation2() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        Folder child3 = model.newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(2, child1);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child3, children2.get(1));
        assertEquals(child1, children2.get(2));
    }
    
    public void testFolderRearrangeToLocation3() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        Folder child3 = model.newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(1, child1);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child1, children2.get(1));
        assertEquals(child3, children2.get(2));
    }
    
    public void testAddAll() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        Folder parent = model.newFolder("parent", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        
        parent.getFolders().addAll(children);
        List<Folder> children2 = parent.getFolders();
        assertEquals(2, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child2, children2.get(1));
        
        assertEquals(1, model.getRootFolder().getFolders().size());
    }
    
    public void testAddAllToLocation() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        Folder child3 = model.newFolder("child3", null);
        Folder child4 = model.newFolder("child4", null);
        Folder parent1 = model.newFolder("parent1", null);
        Folder parent2 = model.newFolder("parent2", null);
        
        List<Folder> children1 = parent1.getFolders();
        children1.add(child1);
        children1.add(child2);
        List<Folder> children2 = parent2.getFolders();
        children2.add(child3);
        children2.add(child4);
        
        assertEquals(2, children2.size());
        
        children2.addAll(0, children1);
        children2 = parent2.getFolders();
        
        assertEquals(4, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child2, children2.get(1));
        assertEquals(child3, children2.get(2));
        assertEquals(child4, children2.get(3));
    }
    
    public void testClearChildren() {
        Folder child1 = model.newFolder("child1", null);
        Folder child2 = model.newFolder("child2", null);
        
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        model.getRootFolder().getFolders().clear();
        assertEquals(0, model.getRootFolder().getFolders().size());
    }
    
}
