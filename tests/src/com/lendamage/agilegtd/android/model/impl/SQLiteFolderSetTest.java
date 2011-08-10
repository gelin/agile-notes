package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Folder;

public class SQLiteFolderSetTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    /*
    public void testAddRemove() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder child1 = parent.newFolder("child1", null);
        Folder child2 = parent.newFolder("child2", null);
        //parent.getFolders().add(child1);
        //parent.getFolders().add(child2);
        
        List<Folder> children = parent.getFolders();
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        children.remove(0);
        List<Folder> children2 = parent.getFolders();
        assertEquals(1, children2.size());
        assertEquals(child2, children.get(0));
        Folder child3 = parent.newFolder("child3", null);
        //children2.add(child3);
        List<Folder> children3 = parent.getFolders();
        assertEquals(2, children3.size());
        assertEquals(child2, children3.get(0));
        assertEquals(child3, children3.get(1));
    }
    
    public void testAddAll() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        
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
    
    public void testAddAllToThis() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        model.getRootFolder().getFolders().addAll(children);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(2, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child2, children2.get(1));
    }
    
    public void testClear() {
        model.getRootFolder().newFolder("child1", null);
        model.getRootFolder().newFolder("child2", null);
        
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        model.getRootFolder().getFolders().clear();
        assertEquals(0, model.getRootFolder().getFolders().size());
    }
    
    public void testIterator() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        Folder ethalon = child1;
        for (Folder folder : model.getRootFolder().getFolders()) {
            assertEquals(ethalon, folder);
            ethalon = child2;
        }
    }
    
    public void testIteratorRemove() {
        model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        Iterator<Folder> i = model.getRootFolder().getFolders().iterator();
        i.next();
        i.remove();
        
        assertEquals(1, model.getRootFolder().getFolders().size());
        assertEquals(child2, model.getRootFolder().getFolders().get(0));
    }
    
    public void testRemove() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        
        assertEquals(1, model.getRootFolder().getFolders().size());
        assertNotNull(model.getFolder(new SimplePath("folder")));
        
        model.getRootFolder().getFolders().remove(folder);
        assertEquals(0, model.getRootFolder().getFolders().size());
        assertNull(model.getFolder(new SimplePath("folder")));
    }
    
    public void testRemoveResursive() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        List<Folder> children = parent.getFolders();
        children.add(child1);
        children.add(child2);
        
        assertEquals(2, parent.getFolders().size());
        assertNotNull(model.getFolder(new SimplePath("parent")));
        assertNotNull(model.getFolder(new SimplePath("parent/child1")));
        assertNotNull(model.getFolder(new SimplePath("parent/child2")));
        
        model.getRootFolder().getFolders().remove(parent);
        assertNull(model.getFolder(new SimplePath("parent")));
        assertNull(model.getFolder(new SimplePath("parent/child1")));
        assertNull(model.getFolder(new SimplePath("parent/child2")));
    }
    */
    
}
