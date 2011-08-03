package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;

public class SQLiteFolderTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testGetFolders() {
        SQLiteFolder parent = (SQLiteFolder)model.getRootFolder().newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)parent.newFolder("child", null);
        //parent.getFolders().add(child);
        List<Folder> children = parent.getFolders();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }
    
    public void testGetActions() {
        SQLiteAction action1 = (SQLiteAction)model.getRootFolder().newAction("action1", null);
        SQLiteAction action2 = (SQLiteAction)model.getRootFolder().newAction("action2", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(2, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
    }
    
    public void testEdit() {
        SQLiteFolder folder = (SQLiteFolder)model.getRootFolder().newFolder("folder", null);
        assertEquals("folder", folder.getName());
        assertNull(folder.getType());
        
        Folder.Editor editor = folder.edit();
        editor.setName("newname");
        editor.setType(FolderType.PROJECTS);
        editor.commit();
        
        assertEquals("newname", folder.getName());
        assertEquals(FolderType.PROJECTS, folder.getType());
        assertEquals(new SimplePath("newname"), folder.getPath());
        
        assertNull(model.getFolder(new SimplePath("folder")));
        SQLiteFolder folder2 = (SQLiteFolder)model.getFolder(new SimplePath("newname"));
        assertNotNull(folder2);
        assertEquals(folder, folder2);
        assertEquals("newname", folder2.getName());
        assertEquals(FolderType.PROJECTS, folder2.getType());
    }
    
    public void testEditNameOnly() {
        SQLiteFolder folder = (SQLiteFolder)model.getRootFolder().newFolder("folder", FolderType.PROJECTS);
        assertEquals("folder", folder.getName());
        assertEquals(FolderType.PROJECTS, folder.getType());;
        
        Folder.Editor editor = folder.edit();
        editor.setName("newname");
        editor.commit();
        
        assertEquals("newname", folder.getName());
        assertEquals(FolderType.PROJECTS, folder.getType());
        assertEquals(new SimplePath("newname"), folder.getPath());
        
        assertNull(model.getFolder(new SimplePath("folder")));
        SQLiteFolder folder2 = (SQLiteFolder)model.getFolder(new SimplePath("newname"));
        assertNotNull(folder2);
        assertEquals(folder, folder2);
        assertEquals("newname", folder2.getName());
        assertEquals(FolderType.PROJECTS, folder2.getType());
    }
    
    public void testEditTypeOnly() {
        SQLiteFolder folder = (SQLiteFolder)model.getRootFolder().newFolder("folder", null);
        assertEquals("folder", folder.getName());
        assertNull(folder.getType());
        
        Folder.Editor editor = folder.edit();
        editor.setType(FolderType.PROJECTS);
        editor.commit();
        
        assertEquals("folder", folder.getName());
        assertEquals(FolderType.PROJECTS, folder.getType());
        assertEquals(new SimplePath("folder"), folder.getPath());
        
        SQLiteFolder folder2 = (SQLiteFolder)model.getFolder(new SimplePath("folder"));
        assertNotNull(folder2);
        assertEquals(folder, folder2);
        assertEquals("folder", folder2.getName());
        assertEquals(FolderType.PROJECTS, folder2.getType());
    }
    
    public void testEditRoot() {
        //TODO: test inability to change root folder name
    }

}
