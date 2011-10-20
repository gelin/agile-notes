/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.CommonDao.checkDb;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Folder;

/**
 *  Special implementation of list of folders to map changes to database.
 */
class SQLiteFolderList implements List<Folder> {

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
            throw new UnsupportedOperationException("cannot add not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        checkDb(db);
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
    /**
     *  Inserts the folder as a subfolder to specified position.
     */
    public void add(int location, Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("cannot add not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        checkDb(db);
        db.beginTransaction();
        try {
            addFolder(location, sqlFolder);
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    
    void addFolder(SQLiteFolder folder) {
        if (this.folders.contains(folder)) {
            return;
        }
        addFolder(this.folders.size(), folder);
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
            throw new UnsupportedOperationException("cannot add not-SQLiteFolderList");
        }
        SQLiteFolderList sqlFolders = (SQLiteFolderList)folders;
        if (sqlFolders.id == this.id) {
            return false;   //no need to insert into self
        }
        Iterator<SQLiteFolder> i = sqlFolders.folders.iterator();
        checkDb(db);
        db.beginTransaction();
        try {
            while (i.hasNext()) {
                SQLiteFolder folder = i.next();
                if (folder.id == this.id) {
                    continue;
                }
                i.remove();
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
     *  Inserts the folders as subfolders to specified position.
     */
    public boolean addAll(int location, Collection<? extends Folder> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        if (!(folders instanceof SQLiteFolderList)) {
            throw new UnsupportedOperationException("cannot add not-SQLiteFolderList");
        }
        SQLiteFolderList sqlFolders = (SQLiteFolderList)folders;
        if (sqlFolders.id == this.id) {
            return false;   //no need to insert into self
        }
        ListIterator<SQLiteFolder> i = sqlFolders.folders.listIterator(sqlFolders.folders.size());
        checkDb(db);
        db.beginTransaction();
        try {
            while (i.hasPrevious()) {   //inserting in reverse order to the same position
                SQLiteFolder folder = i.previous();
                if (folder.id == this.id) {
                    continue;
                }
                i.remove();
                addFolder(location, folder);
            }
            updateOrder();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    /**
     *  Deletes all subfolders.
     */
    public void clear() {
        checkDb(db);
        db.beginTransaction();
        try {
            for (SQLiteFolder child : this.folders) {
                FolderDao.deleteFolder(db, child.id);
            }
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
        return new SQLiteFolderListIterator(this);
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
     *  Returns null, because the folder is deleted from the database and the return value is not
     *  actual. 
     */
    public Folder remove(int location) {
        SQLiteFolder folder = this.folders.get(location);
        checkDb(db);
        db.beginTransaction();
        try {
            FolderDao.deleteFolder(this.db, folder.id);
            this.folders.remove(location);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }
    /**
     *  Deletes subfolder. 
     */
    public boolean remove(Object object) {
        int index = this.folders.indexOf(object);
        if (index < 0) {
            return false;
        }
        remove(index);
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
    /**
     *  Replaces the folder on specified location.
     *  Returns null, because the old folder is deleted from the database and the return value is not
     *  actual.
     */
    public Folder set(int location, Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new UnsupportedOperationException("cannot set not-SQLite folder");
        }
        SQLiteFolder newFolder = (SQLiteFolder)folder;
        SQLiteFolder oldFolder = this.folders.set(location, newFolder);
        checkDb(db);
        db.beginTransaction();
        try {
            FolderDao.updateFolderParent(db, newFolder, this.id);
            FolderDao.deleteFolder(db, oldFolder.id);
            updateOrder();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            this.folders.set(location, oldFolder);
        } finally {
            db.endTransaction();
        }
        return null;
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
