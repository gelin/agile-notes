package com.lendamage.agilegtd.android.model.impl;

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
        
        actions.add(action1);    //add to the end
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
    
    /*
    public void testClear() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        model.getRootFolder().getFolders().clear();
        assertEquals(0, model.getRootFolder().getFolders().size());
    }
    
    public void testIterator() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        Folder ethalon = child1;
        for (Folder folder : model.getRootFolder().getFolders()) {
            assertEquals(ethalon, folder);
            ethalon = child2;
        }
    }
    
    public void testIteratorRemove() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        assertEquals(2, model.getRootFolder().getFolders().size());
        
        Iterator<Folder> i = model.getRootFolder().getFolders().iterator();
        i.next();
        i.remove();
        
        assertEquals(1, model.getRootFolder().getFolders().size());
        assertEquals(child2, model.getRootFolder().getFolders().get(0));
    }
    
    public void testRemove() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        
        assertEquals(1, model.getRootFolder().getFolders().size());
        assertNotNull(model.getFolder(new SimplePath("folder")));
        
        model.getRootFolder().getFolders().remove(folder);
        assertEquals(0, model.getRootFolder().getFolders().size());
        assertNull(model.getFolder(new SimplePath("folder")));
    }
    
    public void testRemoveResursive() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        List<Folder> children = parent.getFolders();
        children.add(child1);
        children.add(child2);
        
        assertEquals(2, parent.getFolders().size());
        assertNotNull(model.getFolder(new SimplePath("parent")));
        assertNotNull(model.getFolder(new SimplePath("parent/child1")));
        assertNotNull(model.getFolder(new SimplePath("parent/child2")));
        
        model.getRootFolder().getFolders().remove(parent);
        assertNull(model.getFolder(new SimplePath("parent")));
        assertNull(model.getFolder(new SimplePath("parent/child1")));
        assertNull(model.getFolder(new SimplePath("parent/child2")));
    }
    
    public void testSet() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        List<Folder> children = parent.getFolders();
        children.add(child1);
        children.add(child2);
        
        Folder child3 = model.getRootFolder().newFolder("child3", null);
        children.set(1, child3);
        
        List<Folder> children2 = parent.getFolders();
        assertEquals(2, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child3, children2.get(1));
        assertNull(model.getFolder(new SimplePath("parent/child2")));
    }
    */
    
}
