package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

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
        //TODO
        SQLiteFolder parent = (SQLiteFolder)model.getRootFolder().newFolder("parent", null);
        SQLiteFolder child = (SQLiteFolder)parent.newFolder("child", null);
        //parent.getFolders().add(child);
        List<Folder> children = parent.getFolders();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
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
