package com.lendamage.agilegtd.android.model.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Folder;

/**
 *  Special implementation of list of folders to map changes to database.
 */
public class SQLiteFolderList implements List<Folder> {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID of the folder to which the list belongs */
    final long id;
    /** Wrapped list */
    List<SQLiteFolder> folders;
    
    SQLiteFolderList(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
    }
    
    void setFolders(List<SQLiteFolder> folders) {
        this.folders = folders;
    }
    
    /**
     *  Inserts the folder as a subfolder.
     */
    public boolean add(Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("Cannot add not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        db.beginTransaction();
        try {
            addFolder(this.folders.size(), sqlFolder);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Inserts the folder as a subfolder to specified position.
     */
    public void add(int location, Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("Cannot add not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        db.beginTransaction();
        try {
            addFolder(location, sqlFolder);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    void addFolder(int location, SQLiteFolder folder) {
        FolderDao.updateFolderParent(this.db, folder, this.id);
        this.folders.remove(folder);
        if (location > this.folders.size()) {
            location = this.folders.size();
        }
        this.folders.add(location, folder);
    }
    /**
     *  Inserts the folders as subfolders.
     */
    public boolean addAll(Collection<? extends Folder> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        if (!(folders instanceof SQLiteFolderList)) {
            throw new UnsupportedOperationException("Cannot add not-SQLiteFolderList");
        }
        SQLiteFolderList sqlFolders = (SQLiteFolderList)folders;
        Iterator<SQLiteFolder> i = sqlFolders.folders.iterator();
        while (i.hasNext()) {
            SQLiteFolder folder = i.next();
            if (folder.id == this.id) {
                continue;
            }
            i.remove();
            addFolder(this.folders.size(), folder);
        }
        updateOrder();
        return true;
    }
    /**
     *  Inserts the folders as subfolders to specified position.
     */
    public boolean addAll(int location, Collection<? extends Folder> folders) {
        //TODO: do insert
        //TODO: type safety
        Collection<SQLiteFolder> sqlFolders = (Collection<SQLiteFolder>)folders;
        return this.folders.addAll(location, sqlFolders);
    }
    /**
     *  Deletes all subfolders.
     */
    public void clear() {
        //TODO: do delete
        this.folders.clear();
    }
    public boolean contains(Object object) {
        return this.folders.contains(object);
    }
    public boolean containsAll(Collection<?> objects) {
        return this.folders.contains(objects);
    }
    public Folder get(int location) {
        return this.folders.get(location);
    }
    public int indexOf(Object object) {
        return this.folders.indexOf(object);
    }
    public boolean isEmpty() {
        return this.folders.isEmpty();
    }
    public Iterator<Folder> iterator() {
        //TODO: return specific iterator to support remove ops.
        //TODO: implement
        //return (Iterator<Folder>)this.folders.iterator();
        return null;
    }
    public int lastIndexOf(Object object) {
        return this.folders.lastIndexOf(object);
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public ListIterator<Folder> listIterator() {
        throw new UnsupportedOperationException("listIterator() is not supported");
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public ListIterator<Folder> listIterator(int location) {
        throw new UnsupportedOperationException("listIterator() is not supported");
    }
    /**
     *  Deletes subfolder. 
     */
    public Folder remove(int location) {
        SQLiteFolder folder = this.folders.get(location);
        Folder result = null;
        db.beginTransaction();
        try {
            FolderDao.deleteFolder(this.db, folder.id);
            result = this.folders.remove(location);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }
    /**
     *  Deletes subfolder. 
     */
    public boolean remove(Object object) {
        //TODO: do delete
        return this.folders.remove(object);
    }
    /**
     *  Deletes subfolders. 
     */
    public boolean removeAll(Collection<?> folders) {
        //TODO: do delete
        return this.folders.removeAll(folders);
    }
    /**
     *  Deletes all subfolders, except the specified ones.
     */
    public boolean retainAll(Collection<?> folders) {
        //TODO: do delete
        return this.folders.retainAll(folders);
    }
    /**
     *  Updates the folder on specified location.
     */
    public Folder set(int location, Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("Cannot set not-SQLite folder");
        }
        //TODO: do update
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        return this.folders.set(location, sqlFolder);
    }
    public int size() {
        return this.folders.size();
    }
    /**
     *  Throws UnsupportedOperationException.
     */
    public List<Folder> subList(int start, int end) {
        throw new UnsupportedOperationException("subList() is not supported");
    }
    public Object[] toArray() {
        return this.folders.toArray();
    }
    public <T> T[] toArray(T[] array) {
        return this.folders.toArray(array);
    }
    
    void updateOrder() {
        for (int i = 0; i < this.folders.size(); i++) {
            SQLiteFolder iFolder = this.folders.get(i);
            FolderDao.updateFolderOrder(this.db, iFolder, i);
        }
    }

}
