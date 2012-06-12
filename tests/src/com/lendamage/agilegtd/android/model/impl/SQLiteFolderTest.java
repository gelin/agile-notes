package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderType;

import static com.lendamage.agilegtd.model.ModelSettings.NewItemPosition.LAST;

public class SQLiteFolderTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
        model.getSettings().setNewItemPosition(LAST);
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
        assertEquals(FolderType.PROJECTS, folder.getType());
        
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
        SQLiteFolder folder = (SQLiteFolder)model.getRootFolder();
        assertEquals("", folder.getName());
        assertEquals(FolderType.ROOT, folder.getType());
        
        Folder.Editor editor = folder.edit();
        editor.setName("folder");
        editor.setType(FolderType.PROJECTS);
        editor.commit();
        
        assertEquals("", folder.getName());
        assertEquals(FolderType.ROOT, folder.getType());
        assertEquals(new SimplePath(""), folder.getPath());
        
        SQLiteFolder folder2 = (SQLiteFolder)model.getFolder(new SimplePath(""));
        assertNotNull(folder2);
        assertEquals(folder, folder2);
        assertEquals("", folder2.getName());
        assertEquals(FolderType.ROOT, folder2.getType());
    }
    
    public void testEditSameName() {
        SQLiteFolder folder = (SQLiteFolder)model.getRootFolder().newFolder("folder", null);
        model.getRootFolder().newFolder("folder2", null);
        
        Folder.Editor editor = folder.edit();
        editor.setName("folder2");
        
        try {
            editor.commit();
            fail();
        } catch (FolderAlreadyExistsException e) {
            //pass
        }
        assertNotNull(model.getFolder(new SimplePath("folder")));
    }
    
    public void testNewFolderOrder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        List<Folder> folders = model.getRootFolder().getFolders();
        folders.add(0, folder2);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Folder folder4 = model.getRootFolder().newFolder("folder4", null);
        
        List<Folder> folders2 = model.getRootFolder().getFolders();
        assertEquals(folder2, folders2.get(0));
        assertEquals(folder1, folders2.get(1));
        assertEquals(folder3, folders2.get(2));
        assertEquals(folder4, folders2.get(3));
    }
    
    public void testNewActionOrder() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        List<Action> actions = model.getRootFolder().getActions();
        actions.add(0, action2);
        Action action3 = model.getRootFolder().newAction("action3", null);
        Action action4 = model.getRootFolder().newAction("action4", null);
        
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(action2, actions2.get(0));
        assertEquals(action1, actions2.get(1));
        assertEquals(action3, actions2.get(2));
        assertEquals(action4, actions2.get(3));
    }
    
    public void testGetFolderTree() {
        FolderTree tree = model.getRootFolder().getFolderTree();
        assertNotNull(tree);
        assertEquals(1, tree.getCount());
        assertNotNull(tree.getNodeByPosition(0));
        assertTrue(tree.getNodeByPosition(0).isLeaf());
    }
    
    public void testMoveFolder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = folder2.newFolder("folder3", null);
        folder1.getFolders().add(folder2);
        assertEquals(folder1, model.getFolder(new SimplePath("folder1")));
        assertEquals(folder2, model.getFolder(new SimplePath("folder1/folder2")));
        assertEquals(folder3, model.getFolder(new SimplePath("folder1/folder2/folder3")));
    }

}
