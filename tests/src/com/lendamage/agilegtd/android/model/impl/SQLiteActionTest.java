package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

public class SQLiteActionTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testGetFolders() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        folder.getActions().add(action);
        Set<Folder> folders = action.getFolders();
        assertNotNull(folders);
        assertEquals(2, folders.size());
        assertTrue(folders.contains(model.getRootFolder()));
        assertTrue(folders.contains(folder));
    }
    
    public void testFoldersSortOrder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        folder1.getActions().add(action);
        folder2.getActions().add(action);
        
        Iterator<Folder> folders = action.getFolders().iterator();
        SQLiteFolder sqlFolder1 = (SQLiteFolder)folders.next();
        SQLiteFolder sqlFolder2 = (SQLiteFolder)folders.next();
        assertEquals(0, sqlFolder1.sortOrder);
        assertEquals(1, sqlFolder2.sortOrder);
    }
    
    public void testGetFoldersOrder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        folder1.getActions().add(action);
        folder2.getActions().add(action);
        
        Iterator<Folder> folders1 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders1.next());
        assertEquals(folder1, folders1.next());
        assertEquals(folder2, folders1.next());
        
        model.getRootFolder().getFolders().add(0, folder2);
        Iterator<Folder> folders2 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders2.next());
        assertEquals(folder2, folders2.next());
        assertEquals(folder1, folders2.next());
    }
    
    public void testGetFoldersOrder2() {
        Folder parent1 = model.getRootFolder().newFolder("parent1", null);
        Folder parent2 = model.getRootFolder().newFolder("parent2", null);
        Folder folder = parent1.newFolder("folder", null);
        Action action = model.getRootFolder().newAction("action", null);
        parent1.getActions().add(action);
        parent2.getActions().add(action);
        folder.getActions().add(action);
        
        Iterator<Folder> folders1 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders1.next());
        assertEquals(parent1, folders1.next());
        assertEquals(folder, folders1.next());
        assertEquals(parent2, folders1.next());
        
        parent2.getFolders().add(folder);
        Iterator<Folder> folders2 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders2.next());
        assertEquals(parent1, folders2.next());
        assertEquals(parent2, folders2.next());
        assertEquals(folder, folders2.next());
    }
    
    public void testGetFoldersOrder3() {
        Folder parent1 = model.getRootFolder().newFolder("parent1", null);
        Folder parent2 = model.getRootFolder().newFolder("parent2", null);
        Folder folder1 = parent1.newFolder("folder1", null);
        Folder folder2 = parent1.newFolder("folder2", null);
        Action action = model.getRootFolder().newAction("action", null);
        parent1.getActions().add(action);
        parent2.getActions().add(action);
        folder1.getActions().add(action);
        folder2.getActions().add(action);
        
        Iterator<Folder> folders1 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders1.next());
        assertEquals(parent1, folders1.next());
        assertEquals(folder1, folders1.next());
        assertEquals(folder2, folders1.next());
        assertEquals(parent2, folders1.next());
        
        parent2.getFolders().add(folder2);
        parent2.getFolders().add(folder1);
        model.getRootFolder().getFolders().add(0, parent2);
        Iterator<Folder> folders2 = action.getFolders().iterator();
        assertEquals(model.getRootFolder(), folders2.next());
        assertEquals(parent2, folders2.next());
        assertEquals(folder2, folders2.next());
        assertEquals(folder1, folders2.next());
        assertEquals(parent1, folders2.next());
    }
    
    public void testEdit() {
        SQLiteAction action = (SQLiteAction)model.getRootFolder().newAction("action", null);
        assertEquals("action", action.getHead());
        assertNull(action.getBody());
        
        Action.Editor editor = action.edit();
        editor.setHead("newname");
        editor.setBody("body");
        editor.commit();
        
        assertEquals("newname", action.getHead());
        assertEquals("body", action.getBody());
        
        SQLiteAction action2 = (SQLiteAction)model.getRootFolder().getActions().get(0);
        assertNotNull(action2);
        assertEquals(action, action2);
        assertEquals("newname", action2.getHead());
        assertEquals("body", action2.getBody());
    }
    
    public void testEditHeadOnly() {
        SQLiteAction action = (SQLiteAction)model.getRootFolder().newAction("action", "body");
        assertEquals("action", action.getHead());
        assertEquals("body", action.getBody());
        
        Action.Editor editor = action.edit();
        editor.setHead("newname");
        editor.commit();
        
        assertEquals("newname", action.getHead());
        assertEquals("body", action.getBody());
        
        SQLiteAction action2 = (SQLiteAction)model.getRootFolder().getActions().get(0);
        assertEquals(action, action2);
        assertEquals("newname", action2.getHead());
        assertEquals("body", action2.getBody());
    }
    
    public void testEditBodyOnly() {
        SQLiteAction action = (SQLiteAction)model.getRootFolder().newAction("action", null);
        assertEquals("action", action.getHead());
        assertNull(action.getBody());
        
        Action.Editor editor = action.edit();
        editor.setBody("body");
        editor.commit();
        
        assertEquals("action", action.getHead());
        assertEquals("body", action.getBody());
        
        SQLiteAction action2 = (SQLiteAction)model.getRootFolder().getActions().get(0);
        assertEquals(action, action2);
        assertEquals("action", action2.getHead());
        assertEquals("body", action2.getBody());
    }

}
