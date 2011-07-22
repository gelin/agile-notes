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
    
    /*
    public void testAddToLocation() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder child1 = parent.newFolder("child1", null);
        Folder child2 = parent.newFolder("child2", null);
        //parent.getFolders().add(child1);
        parent.getFolders().add(0, child2);
        
        List<Folder> children = parent.getFolders();
        assertEquals(2, children.size());
        assertEquals(child2, children.get(0));
        assertEquals(child1, children.get(1));
    }
    
    public void testRearrange() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder child1 = parent.newFolder("child1", null);
        Folder child2 = parent.newFolder("child2", null);
        //parent.getFolders().add(child1);
        //parent.getFolders().add(child2);
        List<Folder> children = parent.getFolders();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        
        children.add(child1);    //add to the end
        assertEquals("the size should not increase", 2, children.size());
        List<Folder> children2 = parent.getFolders();
        assertEquals(2, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child1, children2.get(1));
    }
    
    public void testRearrangeToLocation1() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder child3 = model.getRootFolder().newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(0, child3);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child3, children2.get(0));
        assertEquals(child1, children2.get(1));
        assertEquals(child2, children2.get(2));
    }
    
    public void testRearrangeToLocation2() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder child3 = model.getRootFolder().newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(2, child1);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child3, children2.get(1));
        assertEquals(child1, children2.get(2));
    }
    
    public void testRearrangeToLocation3() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder child3 = model.getRootFolder().newFolder("child3", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
        
        children.add(1, child1);
        List<Folder> children2 = model.getRootFolder().getFolders();
        assertEquals(3, children2.size());
        assertEquals(child2, children2.get(0));
        assertEquals(child1, children2.get(1));
        assertEquals(child3, children2.get(2));
    }
    
    public void testAddAll() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        
        List<Folder> children = model.getRootFolder().getFolders();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        
        parent.getFolders().addAll(children);
        List<Folder> children2 = parent.getFolders();
        assertEquals(2, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child2, children2.get(1));
        
        assertEquals(1, model.getRootFolder().getFolders().size());
    }
    
    public void testAddAllToLocation() {
        Folder child1 = model.getRootFolder().newFolder("child1", null);
        Folder child2 = model.getRootFolder().newFolder("child2", null);
        Folder child3 = model.getRootFolder().newFolder("child3", null);
        Folder child4 = model.getRootFolder().newFolder("child4", null);
        Folder parent1 = model.getRootFolder().newFolder("parent1", null);
        Folder parent2 = model.getRootFolder().newFolder("parent2", null);
        
        List<Folder> children1 = parent1.getFolders();
        children1.add(child1);
        children1.add(child2);
        List<Folder> children2 = parent2.getFolders();
        children2.add(child3);
        children2.add(child4);
        
        assertEquals(2, children2.size());
        
        children2.addAll(0, children1);
        children2 = parent2.getFolders();
        
        assertEquals(4, children2.size());
        assertEquals(child1, children2.get(0));
        assertEquals(child2, children2.get(1));
        assertEquals(child3, children2.get(2));
        assertEquals(child4, children2.get(3));
    }
    
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
