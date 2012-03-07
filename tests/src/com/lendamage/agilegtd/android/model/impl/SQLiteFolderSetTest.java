package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    
    public void testAddAllToThis() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        
        Set<Folder> folders = action.getFolders();
        folders.add(folder1);
        folders.add(folder2);
        assertEquals(3, folders.size());
        
        Set<Folder> folders2 = action.getFolders();
        folders2.addAll(folders);
        assertEquals(3, folders2.size());
    }
    
    public void testClear() {
        model.getRootFolder().newFolder("folder1", null);
        model.getRootFolder().newFolder("folder2", null);
        SQLiteAction action = (SQLiteAction)model.getRootFolder().newAction("action", null);
        
        action.getFolders().addAll(model.getRootFolder().getFolders());
        
        assertEquals(3, action.getFolders().size());
        
        action.getFolders().clear();
        assertEquals(0, action.getFolders().size());
        
        assertNull(ActionDao.selectAction(action.model, action.id));
    }
    
    public void testIterator() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        action.getFolders().add(folder);
        
        assertEquals(2, action.getFolders().size());
        
        Folder ethalon = model.getRootFolder();
        for (Folder f : action.getFolders()) {
            assertEquals(ethalon, f);
            ethalon = folder;
        }
    }
    
    public void testIteratorRemove() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        action.getFolders().add(folder);
        assertEquals(2, action.getFolders().size());
        
        Iterator<Folder> i = action.getFolders().iterator();
        i.next();
        i.remove();
        
        assertEquals(1, action.getFolders().size());
        assertTrue(action.getFolders().contains(folder));
    }
    
    public void testRemove() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        action.getFolders().add(folder);
        
        assertEquals(2, action.getFolders().size());
        
        action.getFolders().remove(folder);
        assertEquals(1, action.getFolders().size());
        assertTrue(action.getFolders().contains(model.getRootFolder()));
    }
    
    public void testAddAllAnotherSet() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        Set<Folder> toAdd = new HashSet<Folder>();
        toAdd.add(folder);
        action.getFolders().addAll(toAdd);
        
        assertEquals(2, action.getFolders().size());
        assertTrue(action.getFolders().contains(model.getRootFolder()));
        assertTrue(action.getFolders().contains(folder));
    }
    
    public void testRetainAll() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        action.getFolders().add(folder);
        action.getFolders().add(folder2);
        assertEquals(3, action.getFolders().size());
        
        Set<Folder> toRetain = new HashSet<Folder>();
        toRetain.add(folder);
        action.getFolders().retainAll(toRetain);
        
        assertEquals(1, action.getFolders().size());
        assertTrue(action.getFolders().contains(folder));
    }
    
    public void testRemoveAll() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        action.getFolders().add(folder);
        action.getFolders().add(folder2);
        assertEquals(3, action.getFolders().size());
        
        Set<Folder> toRemove = new HashSet<Folder>();
        toRemove.add(folder);
        toRemove.add(model.getRootFolder());
        action.getFolders().removeAll(toRemove);
        
        assertEquals(1, action.getFolders().size());
        assertTrue(action.getFolders().contains(folder2));
    }

    public void testAddAllOrder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action1 = folder1.newAction("action1", null);
        Action action2 = folder1.newAction("action2", null);
        List<Action> actions1 = folder1.getActions();
        assertEquals(action1, actions1.get(0));
        
        folder1.getActions().add(0, action2);
        List<Action> actions2 = folder1.getActions();
        assertEquals(action2, actions2.get(0));
        
        Set<Folder> folders = action2.getFolders();
        List<Folder> rootFolders = model.getRootFolder().getFolders();
        folders.addAll(rootFolders);
        folders.retainAll(rootFolders);
        
        Set<Folder> folders2 = action2.getFolders();
        assertEquals(2, folders2.size());
        assertTrue(folders.contains(folder1));
        assertTrue(folders.contains(folder2));

        List<Action> actions3 = folder1.getActions();
        assertEquals(action2, actions3.get(0));
        assertEquals(action1, actions3.get(1));
    }

}
