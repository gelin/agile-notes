package com.lendamage.agilegtd.android.model.impl;

import java.util.Set;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import android.test.AndroidTestCase;

public class SQLiteFolderSetTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testAddRemove() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        
        Set<Folder> folders1 = action.getFolders();
        assertEquals(1, folders1.size());
        assertTrue(folders1.contains(model.getRootFolder()));
        
        folders1.add(folder);
        Set<Folder> folders2 = action.getFolders();
        assertEquals(2, folders2.size());
        assertTrue(folders2.contains(model.getRootFolder()));
        assertTrue(folders2.contains(folder));
        
        folders2.remove(folder);
        Set<Folder> folders3 = action.getFolders();
        assertEquals(1, folders3.size());
        assertTrue(folders3.contains(model.getRootFolder()));
    }
    
    public void testAddAll() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        
        Set<Folder> folders = action.getFolders();
        assertEquals(1, folders.size());
        assertTrue(folders.contains(model.getRootFolder()));
        
        folders.addAll(model.getRootFolder().getFolders());
        Set<Folder> folders2 = action.getFolders();
        assertEquals(3, folders2.size());
        assertTrue(folders.contains(model.getRootFolder()));
        assertTrue(folders.contains(folder1));
        assertTrue(folders.contains(folder2));
    }
    
    public void testAddAllFromAction() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        action1.getFolders().add(folder1);
        action1.getFolders().add(folder2);
        
        Set<Folder> folders = action2.getFolders();
        assertEquals(1, folders.size());
        assertTrue(folders.contains(model.getRootFolder()));
        
        folders.addAll(action1.getFolders());
        Set<Folder> folders2 = action2.getFolders();
        assertEquals(3, folders2.size());
        assertTrue(folders.contains(model.getRootFolder()));
        assertTrue(folders.contains(folder1));
        assertTrue(folders.contains(folder2));
    }
    
    /*
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
