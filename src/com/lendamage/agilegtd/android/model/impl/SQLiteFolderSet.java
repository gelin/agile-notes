package com.lendamage.agilegtd.android.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Folder;

/**
 *  Special implementation of set of folders of the action to map changes to database.
 */
class SQLiteFolderSet implements Set<Folder> {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID of the action to which the set belongs */
    final long id;
    /** Wrapped list, list, because the order is valuable */
    List<SQLiteFolder> folders;
    
    SQLiteFolderSet(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
    }
    
    void setFolders(List<SQLiteFolder> folders) {
        this.folders = folders;
    }
    
    /**
     *  Inserts the folder to the action.
     */
    public boolean add(Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("cannot add not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        db.beginTransaction();
        try {
            addFolder(sqlFolder);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    void addFolder(SQLiteFolder folder) {
        ActionDao.replaceActionInFolder(db, folder.id, this.id);
        this.folders.remove(folder);
        this.folders.add(folder);
    }
    
    /**
     *  Assigns the folders to the action.
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends Folder> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        if (folders instanceof SQLiteFolderSet) {
            SQLiteFolderSet sqlFolders = (SQLiteFolderSet)folders;
            if (sqlFolders.id == this.id) {
                return false;   //no need to insert into self
            }
        } else if (folders instanceof SQLiteFolderList) {
            //just accepting this type of collection
        } else {
            throw new UnsupportedOperationException("cannot add collection of not-SQLiteFolders");
        }
        Iterator<SQLiteFolder> i = (Iterator<SQLiteFolder>)folders.iterator();
        db.beginTransaction();
        try {
            while (i.hasNext()) {
                SQLiteFolder folder = i.next();
                addFolder(folder);
            }
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Deletes all folders assigned to the action.
     */
    public void clear() {
        db.beginTransaction();
        try {
            ActionDao.deleteAction(db, this.id);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        
        this.folders.clear();
    }
    public boolean contains(Object object) {
        return this.folders.contains(object);
    }
    public boolean containsAll(Collection<?> objects) {
        return this.folders.contains(objects);
    }
    public boolean isEmpty() {
        return this.folders.isEmpty();
    }
    public Iterator<Folder> iterator() {
        return new SQLiteFolderSetIterator(this);
    }
    /**
     *  Deletes assignment between folder and action. 
     */
    public boolean remove(Object object) {
        if (!this.folders.contains(object)) {
            return false;
        }
        SQLiteFolder folder = (SQLiteFolder)object;
        db.beginTransaction();
        try {
            ActionDao.deleteActionFromFolder(db, folder.id, this.id);
            this.folders.remove(object);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Throws UnsupportedOpeartionException.
     *  Different folders collections are never intersect. 
     */
    public boolean removeAll(Collection<?> folders) {
        throw new UnsupportedOperationException("removeAll() is not supported");
    }
    /**
     *  Throws UnsupportedOpeartionException.
     *  Different folders collections are never intersect.
     */
    public boolean retainAll(Collection<?> folders) {
        throw new UnsupportedOperationException("retainAll() is not supported");
    }
    public int size() {
        return this.folders.size();
    }
    public Object[] toArray() {
        return this.folders.toArray();
    }
    public <T> T[] toArray(T[] array) {
        return this.folders.toArray(array);
    }
    
    void updateOrder() {
        Collections.sort(this.folders, new SQLiteFolderComparator(db));
    }

}
