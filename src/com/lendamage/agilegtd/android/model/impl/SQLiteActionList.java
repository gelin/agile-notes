package com.lendamage.agilegtd.android.model.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;

/**
 *  Special implementation of list of actions in the folder to map changes to database.
 */
class SQLiteActionList implements List<Action> {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID of the folder to which the list belongs */
    final long id;
    /** Wrapped list */
    List<SQLiteAction> actions;
    
    SQLiteActionList(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
    }
    
    void setActions(List<SQLiteAction> actions) {
        this.actions = actions;
    }
    
    /**
     *  Assigns the action with the folder.
     */
    public boolean add(Action action) {
        if (!(action instanceof SQLiteAction)) {
            throw new UnsupportedOperationException("cannot add not-SQLite action");
        }
        SQLiteAction sqlAction = (SQLiteAction)action;
        db.beginTransaction();
        try {
            addAction(this.actions.size(), sqlAction);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Assigns the action with the folder at specified position.
     */
    public void add(int location, Action action) {
        if (!(action instanceof SQLiteAction)) {
            throw new UnsupportedOperationException("cannot add not-SQLite action");
        }
        SQLiteAction sqlAction = (SQLiteAction)action;
        db.beginTransaction();
        try {
            addAction(location, sqlAction);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    void addAction(int location, SQLiteAction action) {
        ActionDao.replaceActionFolder(this.db, action, this.id);
        this.actions.remove(action);
        if (location > this.actions.size()) {
            location = this.actions.size();
        }
        this.actions.add(location, action);
    }
    /**
     *  Assigns all actions from the collection to the folder.
     */
    public boolean addAll(Collection<? extends Action> actions) {
        if (actions == null || actions.isEmpty()) {
            return false;
        }
        if (!(actions instanceof SQLiteActionList)) {
            throw new UnsupportedOperationException("cannot add not-SQLiteActionList");
        }
        SQLiteActionList sqlActions = (SQLiteActionList)actions;
        if (sqlActions.id == this.id) {
            return false;   //no need to insert into self
        }
        Iterator<SQLiteAction> i = sqlActions.actions.iterator();
        db.beginTransaction();
        try {
            while (i.hasNext()) {
                SQLiteAction action = i.next();
                addAction(this.actions.size(), action);
            }
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Assigns the actions from the collection with the folder at specified position.
     */
    public boolean addAll(int location, Collection<? extends Action> actions) {
        if (actions == null || actions.isEmpty()) {
            return false;
        }
        if (!(actions instanceof SQLiteActionList)) {
            throw new UnsupportedOperationException("cannot add not-SQLiteActionList");
        }
        SQLiteActionList sqlActions = (SQLiteActionList)actions;
        if (sqlActions.id == this.id) {
            return false;   //no need to insert into self
        }
        ListIterator<SQLiteAction> i = sqlActions.actions.listIterator(sqlActions.actions.size());
        db.beginTransaction();
        try {
            while (i.hasPrevious()) {   //inserting in reverse order to the same position
                SQLiteAction action = i.previous();
                addAction(location, action);
            }
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Deletes all assignments of actions from this folder.
     */
    public void clear() {
        db.beginTransaction();
        try {
            //TODO: implement
            //FolderDao.deleteChildFolders(db, this.id);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        
        this.actions.clear();
    }
    public boolean contains(Object object) {
        return this.actions.contains(object);
    }
    public boolean containsAll(Collection<?> objects) {
        return this.actions.contains(objects);
    }
    public Action get(int location) {
        return this.actions.get(location);
    }
    public int indexOf(Object object) {
        return this.actions.indexOf(object);
    }
    public boolean isEmpty() {
        return this.actions.isEmpty();
    }
    public Iterator<Action> iterator() {
        //TODO: implement
        //return new SQLiteActionListIterator(this);
        return null;
    }
    public int lastIndexOf(Object object) {
        return this.actions.lastIndexOf(object);
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public ListIterator<Action> listIterator() {
        throw new UnsupportedOperationException("listIterator() is not supported");
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public ListIterator<Action> listIterator(int location) {
        throw new UnsupportedOperationException("listIterator() is not supported");
    }
    /**
     *  Deletes the assignment of the action at specified location with the folder. 
     */
    public Action remove(int location) {
        SQLiteAction action = this.actions.get(location);
        Action result = null;
        db.beginTransaction();
        try {
            ActionDao.deleteActionFromFolder(this.db, this.id, action.id);
            result = this.actions.remove(location);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }
    /**
     *  Deletes the assignment of the action with the folder. 
     */
    public boolean remove(Object object) {
        int index = this.actions.indexOf(object);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }
    /**
     *  Deletes assignment of the actions with the folder. 
     */
    public boolean removeAll(Collection<?> actions) {
        //TODO: implement
        throw new UnsupportedOperationException("removeAll() is not supported");
    }
    /**
     *  Deletes assignment of the actions with the folder.
     *  Only actions specified in the parameter are stay assigned.
     */
    public boolean retainAll(Collection<?> actions) {
        //TODO: implement
        throw new UnsupportedOperationException("retainAll() is not supported");
    }
    /**
     *  Replaces the action assignment at specified location.
     */
    public Action set(int location, Action action) {
        if (!(action instanceof SQLiteAction)) {
            throw new UnsupportedOperationException("cannot set not-SQLite action");
        }
        SQLiteAction newAction = (SQLiteAction)action;
        SQLiteAction oldAction = this.actions.set(location, newAction);
        db.beginTransaction();
        try {
            //TODO: implement
            //FolderDao.updateFolderParent(db, newFolder, this.id);
            //FolderDao.deleteFolder(db, oldFolder.id);
            updateOrder();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            this.actions.set(location, oldAction);
        } finally {
            db.endTransaction();
        }
        return null;    //TODO
    }
    public int size() {
        return this.actions.size();
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public List<Action> subList(int start, int end) {
        throw new UnsupportedOperationException("subList() is not supported");
    }
    public Object[] toArray() {
        return this.actions.toArray();
    }
    public <T> T[] toArray(T[] array) {
        return this.actions.toArray(array);
    }
    
    void updateOrder() {
        for (int i = 0; i < this.actions.size(); i++) {
            SQLiteAction action = this.actions.get(i);
            ActionDao.updateActionOrder(this.db, action, i);
        }
    }

}
