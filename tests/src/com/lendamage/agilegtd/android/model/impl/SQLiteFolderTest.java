package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;
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
    
    public void testGetActions() {
        SQLiteAction action1 = (SQLiteAction)model.newAction("action1", null);
        SQLiteAction action2 = (SQLiteAction)model.newAction("action2", null);
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(0, actions.size());
        actions.add(action1);
        actions.add(action2);
        
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(2, actions2.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
    }

}
