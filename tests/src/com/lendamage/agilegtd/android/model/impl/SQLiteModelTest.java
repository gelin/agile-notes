package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

public class SQLiteModelTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testNewAction() {
        SQLiteAction a1 = (SQLiteAction)model.getRootFolder().newAction("head1", "body1");
        assertFalse(0 == a1.id);
        assertEquals("head1", a1.getHead());
        assertEquals("body1", a1.getBody());
        SQLiteAction a2 = (SQLiteAction)model.getRootFolder().newAction("head2", "body2");
        assertFalse(a1.id == a2.id);
        assertEquals("head2", a2.getHead());
        assertEquals("body2", a2.getBody());
        
        assertEquals(2, model.getRootFolder().getActions().size());
    }
    
    public void testNewFolder() {
        SQLiteFolder f1 = (SQLiteFolder)model.getRootFolder();
        assertFalse(0 == f1.id);
        assertEquals("", f1.getPath().toString());
        assertEquals("", f1.getName());
        assertEquals(FolderType.ROOT, f1.getType());
        SQLiteFolder f2 = (SQLiteFolder)model.getRootFolder().newFolder("Projects", FolderType.PROJECTS);
        assertFalse(f1.id == f2.id);
        assertEquals("Projects", f2.getPath().toString());
        assertEquals("Projects", f2.getName());
        assertEquals(FolderType.PROJECTS, f2.getType());
    }
    
    public void testNewRootFolder() {
        try {
            model.getRootFolder().newFolder("", FolderType.ROOT);
        } catch (FolderAlreadyExistsException fae) {
            return;
        }
        fail();
    }
    
    public void testNewFolderNullType() {
        SQLiteFolder f1 = (SQLiteFolder)model.getRootFolder().newFolder("null", null);
        assertFalse(0 == f1.id);
        assertEquals("null", f1.getPath().toString());
        assertEquals("null", f1.getName());
        assertNull(f1.getType());
    }
    
    public void testNewSubfolder() {
        SQLiteFolder parent = (SQLiteFolder)model.getRootFolder().newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)parent.newFolder("child", null);
        assertFalse(parent.id == child.id);
        assertEquals("parent", parent.getPath().toString());
        assertEquals("parent", parent.getName());
        assertFalse(0 == child.id);
        assertEquals("parent/child", child.getPath().toString());
        SQLiteFolder child2 = (SQLiteFolder)model.getFolder(new SimplePath("parent/child"));
        assertEquals(child, child2);
    }
    
    public void testNewFolderExists() {
        SQLiteFolder parent = (SQLiteFolder)model.getRootFolder().newFolder("parent", null);
        parent.newFolder("child", null);
        try {
            parent.newFolder("child", null);
        } catch (FolderAlreadyExistsException fae) {
            return;
        }
        fail();
    }
    
    public void testGetRootFolder() {
        SQLiteFolder root = (SQLiteFolder)model.getRootFolder();
        assertFalse(0 == root.id);
        assertEquals("", root.getPath().toString());
        assertEquals("", root.getName());
        assertEquals(FolderType.ROOT, root.getType());
    }
    
    public void testGetFolder() {
        SQLiteFolder newFolder = (SQLiteFolder)model.getRootFolder().newFolder("folder", null);
        SQLiteFolder folder = (SQLiteFolder)model.getFolder(new SimplePath("folder"));
        assertEquals(newFolder.id, folder.id);
        assertEquals("folder", folder.getPath().toString());
        assertEquals("folder", folder.getName());
    }
    
    public void testTransactionCommit() {
        SQLiteFolder parent = (SQLiteFolder)model.getRootFolder().newFolder("parent", null);
        parent.newFolder("child", null);
        //parent.getFolders().add(child);
        SQLiteModel model2 = new SQLiteModel(getContext(), "agile-gtd-test.db");
        assertNotNull(model2.getRootFolder());
        assertNotNull(model2.getFolder(new SimplePath("parent")));
        assertNotNull(model2.getFolder(new SimplePath("parent/child")));
    }
    
    public void testFindFoldersRoot() {
        List<Folder> folders = model.findFolders(FolderType.ROOT);
        assertEquals(model.getRootFolder(), folders.get(0));
    }
    
    public void testFindFoldersCompleted() {
        Folder completed1 = model.getRootFolder().newFolder("Completed1", FolderType.COMPLETED);
        Folder completed2 = model.getRootFolder().newFolder("Completed2", FolderType.COMPLETED);
        List<Folder> folders = model.findFolders(FolderType.COMPLETED);
        assertEquals(2, folders.size());
        assertEquals(completed1, folders.get(0));
        assertEquals(completed2, folders.get(1));
    }
    
    public void testFindFoldersNull() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        List<Folder> folders = model.findFolders(null);
        assertEquals(2, folders.size());
        assertEquals(folder1, folders.get(0));
        assertEquals(folder2, folders.get(1));
    }
    
    public void testFindFoldersOrder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        model.getRootFolder().getFolders().add(0, folder2);
        List<Folder> folders = model.findFolders(null);
        assertEquals(2, folders.size());
        assertEquals(folder2, folders.get(0));
        assertEquals(folder1, folders.get(1));
    }
    
    public void testFindFoldersOrder2() {
        Folder parent1 = model.getRootFolder().newFolder("parent1", null);
        Folder parent2 = model.getRootFolder().newFolder("parent2", null);
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        model.getRootFolder().getFolders().add(0, parent2);
        parent1.getFolders().add(folder2);
        parent1.getFolders().add(folder1);
        
        List<Folder> folders = model.findFolders(null);
        assertEquals(4, folders.size());
        assertEquals(parent2, folders.get(0));
        assertEquals(parent1, folders.get(1));
        assertEquals(folder2, folders.get(2));
        assertEquals(folder1, folders.get(3));
    }
    
    public void testMultipleModels() {
        model.getRootFolder().newFolder("folder", null);
        model.close();
        SQLiteModel model2 = new SQLiteModel(getContext(), "agile-gtd-test.db");
        assertEquals(1, model2.getRootFolder().getFolders().size());
    }
    
    public void testSelectAfterClose() {
        model.getRootFolder().newFolder("folder", null);
        model.close();
        try {
            assertEquals(1, model.getRootFolder().getFolders().size());
            fail();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("closed"));
        }
    }
    
    public void testClear() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        folder.newFolder("subfolder", null);
        assertEquals(2, model.findFolders(null).size());
        model.getRootFolder().getFolders().clear();
        assertEquals(0, model.findFolders(null).size());
    }
    
    public void testDeepClear() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        Folder subfolder = folder.newFolder("subfolder", null);
        subfolder.newFolder("subsubfolder", null);
        assertEquals(3, model.findFolders(null).size());
        model.getRootFolder().getFolders().clear();
        assertEquals(0, model.findFolders(null).size());
    }

}
