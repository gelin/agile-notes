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
    final List<Folder> folders;
    
    SQLiteFolderList(SQLiteDatabase db, long id, List<Folder> folders) {
        this.db = db;
        this.id = id;
        this.folders = folders;
    }
    
    /**
     *  Inserts the folder as a subfolder.
     */
    public boolean add(Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("Cannot add not-SQLite folder");
        }
        FolderDao.updateFolderParent(this.db, (SQLiteFolder)folder, this.id);
        return this.folders.add(folder);
    }
    /**
     *  Inserts the folder as a subfolder to specified position.
     */
    public void add(int location, Folder folder) {
        //TODO: do insert
        this.folders.add(location, folder);
    }
    /**
     *  Inserts the folders as subfolders.
     */
    public boolean addAll(Collection<? extends Folder> folders) {
        //TODO: do insert
        return this.folders.addAll(folders);
    }
    /**
     *  Inserts the folders as subfolders to specified position.
     */
    public boolean addAll(int location, Collection<? extends Folder> folders) {
        //TODO: do insert
        return this.folders.addAll(location, folders);
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
        return this.folders.iterator();
    }
    public int lastIndexOf(Object object) {
        return this.folders.lastIndexOf(object);
    }
    public ListIterator<Folder> listIterator() {
        //TODO: return specific iterator to support remove ops.
        return this.folders.listIterator();
    }
    public ListIterator<Folder> listIterator(int location) {
        //TODO: return specific iterator to support remove ops.
        return this.folders.listIterator(location);
    }
    /**
     *  Deletes subfolder. 
     */
    public Folder remove(int location) {
        //TODO: do delete
        return this.folders.remove(location);
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
        //TODO: do update
        return this.folders.set(location, folder);
    }
    public int size() {
        return this.folders.size();
    }
    public List<Folder> subList(int start, int end) {
        //TODO: the sublist should correctly work
        return new SQLiteFolderList(db, id, this.folders.subList(start, end));
    }
    public Object[] toArray() {
        return this.folders.toArray();
    }
    public <T> T[] toArray(T[] array) {
        return this.folders.toArray(array);
    } 

}
