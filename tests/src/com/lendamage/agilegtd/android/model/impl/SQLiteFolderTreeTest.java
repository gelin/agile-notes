package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Folder;

public class SQLiteFolderTreeTest extends AndroidTestCase {
    
    SQLiteFolderTree tree;
    Folder root;
    Folder folder1;
    Folder folder2;
    Folder subfolder1;
    Folder subfolder2;
    Folder subfolder3;
    Folder subfolder4;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        SQLiteModel model = new SQLiteModel(getContext(), "agile-gtd-test.db");
        root = model.getRootFolder();
        folder1 = root.newFolder("folder1", null);
        folder2 = root.newFolder("folder2", null);
        subfolder1 = folder1.newFolder("subfolder1", null);
        subfolder2 = folder1.newFolder("subfolder2", null);
        subfolder3 = folder2.newFolder("subfolder3", null);
        subfolder4 = folder2.newFolder("subfolder4", null);
        tree = (SQLiteFolderTree)root.getFolderTree();
    }
    
    public void testInitialCount() {
        assertEquals(3, tree.getCount());
        assertEquals(root, tree.getNodeByPosition(0).getFolder());
        tree.getNodeByPosition(0).setExpanded(false);
        assertEquals(1, tree.getCount());
        assertEquals(root, tree.getNodeByPosition(0).getFolder());
    }
    
}