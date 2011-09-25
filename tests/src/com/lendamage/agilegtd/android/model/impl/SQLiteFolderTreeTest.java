package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;

public class SQLiteFolderTreeTest extends AndroidTestCase {
    
    SQLiteFolderTree tree;
    SQLiteFolder root;
    SQLiteFolder folder1;
    SQLiteFolder folder2;
    SQLiteFolder subfolder1;
    SQLiteFolder subfolder2;
    SQLiteFolder subfolder3;
    SQLiteFolder subfolder4;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        SQLiteModel model = new SQLiteModel(getContext(), "agile-gtd-test.db");
        root = (SQLiteFolder)model.getRootFolder();
        folder1 = (SQLiteFolder)root.newFolder("folder1", null);
        folder2 = (SQLiteFolder)root.newFolder("folder2", null);
        subfolder1 = (SQLiteFolder)folder1.newFolder("subfolder1", null);
        subfolder2 = (SQLiteFolder)folder1.newFolder("subfolder2", null);
        subfolder3 = (SQLiteFolder)folder2.newFolder("subfolder3", null);
        subfolder4 = (SQLiteFolder)folder2.newFolder("subfolder4", null);
        tree = (SQLiteFolderTree)root.getFolderTree();
    }
    
    public void testNodeEquals() {
        assertEquals(tree.getNodeByPosition(0), tree.getNodeByPosition(0));
        assertEquals(tree.getNodeById(root.id), tree.getNodeById(root.id));
        assertEquals(tree.getNodeById(root.id), tree.getNodeByPosition(0));
    }
    
    public void testInitialCount() {
        assertEquals(3, tree.getCount());
        assertEquals(root, tree.getNodeByPosition(0).getFolder());
        tree.getNodeByPosition(0).setExpanded(false);
        assertEquals(1, tree.getCount());
        assertEquals(root, tree.getNodeByPosition(0).getFolder());
    }
    
    public void testCountExpand() {
        assertEquals(3, tree.getCount());
        assertEquals(folder1, tree.getNodeById(folder1.id).getFolder());
        tree.getNodeById(folder1.id).setExpanded(true);
        assertEquals(5, tree.getCount());
        assertEquals(folder2, tree.getNodeById(folder2.id).getFolder());
        tree.getNodeById(folder2.id).setExpanded(true);
        assertEquals(7, tree.getCount());
    }
    
    public void testCountCollapse() {
        assertEquals(3, tree.getCount());
        tree.getNodeById(folder1.id).setExpanded(true);
        tree.getNodeById(folder2.id).setExpanded(true);
        assertEquals(7, tree.getCount());
        tree.getNodeById(folder1.id).setExpanded(false);
        assertEquals(5, tree.getCount());
        tree.getNodeById(root.id).setExpanded(false);
        assertEquals(1, tree.getCount());
        tree.getNodeById(root.id).setExpanded(true);
        assertEquals(5, tree.getCount());
    }
    
    public void testDepth() {
        assertEquals(0, tree.getNodeById(root.id).getDepth());
        assertEquals(1, tree.getNodeById(folder1.id).getDepth());
        assertEquals(2, tree.getNodeById(subfolder1.id).getDepth());
        assertEquals(1, tree.getNodeById(folder2.id).getDepth());
        assertEquals(2, tree.getNodeById(subfolder3.id).getDepth());
    }

}