package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;

public class SQLiteActionTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
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
