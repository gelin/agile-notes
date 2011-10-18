package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

public class SQLiteActionListTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testAddRemove() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        actions.remove(0);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(1, actions2.size());
        assertEquals(action2, actions2.get(0));
        Action action3 = model.getRootFolder().newAction("action3", null);
        List<Action> actions3 = model.getRootFolder().getActions();
        assertEquals(2, actions3.size());
        assertEquals(action2, actions3.get(0));
        assertEquals(action3, actions3.get(1));
    }
    
    public void testRemoveAll() {
        SQLiteAction action = (SQLiteAction)model.getRootFolder().newAction("action", null);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        folder.getActions().add(action);
        assertTrue(model.getRootFolder().getActions().contains(action));
        assertTrue(folder.getActions().contains(action));
        
        model.getRootFolder().getActions().remove(action);
        assertFalse(model.getRootFolder().getActions().contains(action));
        assertTrue(folder.getActions().contains(action));
        
        folder.getActions().remove(action);
        assertFalse(model.getRootFolder().getActions().contains(action));
        assertFalse(folder.getActions().contains(action));
        
        assertNull(ActionDao.selectAction(model.db, action.id));
    }
    
    public void testAddToLocation() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        model.getRootFolder().getActions().add(0, action2);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(2, actions.size());
        assertEquals(action2, actions.get(0));
        assertEquals(action1, actions.get(1));
    }
    
    public void testRearrange() {
        Folder folder = model.getRootFolder();
        Action action1 = folder.newAction("action1", null);
        Action action2 = folder.newAction("action2", null);
        
        List<Action> actions = folder.getActions();
        assertEquals(2, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        actions.add(actions.size(), action1);    //add to the end
        assertEquals("the size should not increase", 2, actions.size());
        List<Action> actions2 = folder.getActions();
        assertEquals(2, actions2.size());
        assertEquals(action2, actions2.get(0));
        assertEquals(action1, actions2.get(1));
    }
    
    public void testRearrangeToLocation1() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Action action3 = model.getRootFolder().newAction("action3", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(3, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        assertEquals(action3, actions.get(2));
        
        actions.add(0, action3);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(3, actions2.size());
        assertEquals(action3, actions2.get(0));
        assertEquals(action1, actions2.get(1));
        assertEquals(action2, actions2.get(2));
    }
    
    public void testRearrangeToLocation2() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Action action3 = model.getRootFolder().newAction("action3", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(3, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        assertEquals(action3, actions.get(2));
        
        actions.add(2, action1);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(3, actions2.size());
        assertEquals(action2, actions2.get(0));
        assertEquals(action3, actions2.get(1));
        assertEquals(action1, actions2.get(2));
    }
    
    public void testRearrangeToLocation3() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Action action3 = model.getRootFolder().newAction("action3", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(3, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        assertEquals(action3, actions.get(2));
        
        actions.add(1, action1);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(3, actions2.size());
        assertEquals(action2, actions2.get(0));
        assertEquals(action1, actions2.get(1));
        assertEquals(action3, actions2.get(2));
    }
    
    public void testAddAll() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(2, actions.size());
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        folder.getActions().addAll(actions);
        List<Action> actions2 = folder.getActions();
        assertEquals(2, actions2.size());
        assertEquals(action1, actions2.get(0));
        assertEquals(action2, actions2.get(1));
        
        assertEquals("no actions should be deleted", 2, model.getRootFolder().getActions().size());
    }
    
    public void testAddAllToLocation() {
        Action action1 = model.getRootFolder().newAction("child1", null);
        Action action2 = model.getRootFolder().newAction("child2", null);
        Action action3 = model.getRootFolder().newAction("child3", null);
        Action action4 = model.getRootFolder().newAction("child4", null);
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        
        List<Action> actions1 = folder1.getActions();
        actions1.add(action1);
        actions1.add(action2);
        List<Action> actions2 = folder2.getActions();
        actions2.add(action3);
        actions2.add(action4);
        
        assertEquals(2, actions2.size());
        
        actions2.addAll(0, actions1);
        actions2 = folder2.getActions();
        
        assertEquals(4, actions2.size());
        assertEquals(action1, actions2.get(0));
        assertEquals(action2, actions2.get(1));
        assertEquals(action3, actions2.get(2));
        assertEquals(action4, actions2.get(3));
        
        assertEquals(4, model.getRootFolder().getActions().size());
        assertEquals(2, folder1.getActions().size());
    }
    
    public void testAddAllToThis() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        model.getRootFolder().getActions().addAll(actions);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(2, actions2.size());
        assertEquals(action1, actions2.get(0));
        assertEquals(action2, actions2.get(1));
    }
    
    public void testAddAllToThisToLocation() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        model.getRootFolder().getActions().addAll(1, actions);
        List<Action> actions2 = model.getRootFolder().getActions();
        assertEquals(2, actions2.size());
        assertEquals(action1, actions2.get(0));
        assertEquals(action2, actions2.get(1));
    }
    
    public void testClear() {
        model.getRootFolder().newAction("action1", null);
        model.getRootFolder().newAction("action2", null);
        
        assertEquals(2, model.getRootFolder().getActions().size());
        
        model.getRootFolder().getActions().clear();
        assertEquals(0, model.getRootFolder().getActions().size());
    }
    
    public void testIterator() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        assertEquals(2, model.getRootFolder().getActions().size());
        
        Action ethalon = action1;
        for (Action action : model.getRootFolder().getActions()) {
            assertEquals(ethalon, action);
            ethalon = action2;
        }
    }
    
    public void testIteratorRemove() {
        model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        assertEquals(2, model.getRootFolder().getActions().size());
        
        Iterator<Action> i = model.getRootFolder().getActions().iterator();
        i.next();
        i.remove();
        
        assertEquals(1, model.getRootFolder().getActions().size());
        assertEquals(action2, model.getRootFolder().getActions().get(0));
    }
    
    public void testRemove() {
        Action action = model.getRootFolder().newAction("action", null);
        
        assertEquals(1, model.getRootFolder().getActions().size());
        assertEquals(action, model.getRootFolder().getActions().get(0));
        
        model.getRootFolder().getActions().remove(action);
        assertEquals(0, model.getRootFolder().getActions().size());
    }
    
    public void testSet() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        List<Action> actions = folder.getActions();
        actions.add(action1);
        actions.add(action2);
        
        Action action3 = model.getRootFolder().newAction("action3", null);
        actions.set(1, action3);
        
        List<Action> actions2 = folder.getActions();
        assertEquals(2, actions2.size());
        assertEquals(action1, actions2.get(0));
        assertEquals(action3, actions2.get(1));
    }
    
    public void testOrderingOnFolderSetChange() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        action1.getFolders().add(folder);
        
        actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        actions = folder.getActions();
        assertEquals(action1, actions.get(0));
    }
    
    public void testAddOfExisted() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        model.getRootFolder().getActions().add(action1);
        
        actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
    }
    
    public void testAddAllOfExisted() {
        Action action1 = model.getRootFolder().newAction("action1", null);
        Action action2 = model.getRootFolder().newAction("action2", null);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        
        List<Action> actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
        
        action1.getFolders().add(folder);
        model.getRootFolder().getActions().addAll(folder.getActions());
        
        actions = model.getRootFolder().getActions();
        assertEquals(action1, actions.get(0));
        assertEquals(action2, actions.get(1));
    }

}
