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

    void addFolder(SQLiteFolder folder) {
        ActionDao.replaceActionInFolder(db, folder.id, this.id);
        this.folders.remove(folder);
        this.folders.add(folder);
    }
    
    /**
     *  Assigns the folders to the action.
     */
    public boolean addAll(Collection<? extends Folder> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        checkDb(db);
        db.beginTransaction();
        try {
            for (Folder folder : folders) {
                if (!(folder instanceof SQLiteFolder)) {
                    continue;
                }
                addFolder((SQLiteFolder)folder);
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
        checkDb(db);
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
        checkDb(db);
        db.beginTransaction();
        try {
            removeFolder(folder);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    
    void removeFolder(SQLiteFolder folder) {
        ActionDao.deleteActionFromFolder(db, folder.id, this.id);
        this.folders.remove(folder);
    }
    
    public boolean removeAll(Collection<?> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        checkDb(db);
        db.beginTransaction();
        try {
            for (Object folder : folders) {
                if (!(folder instanceof SQLiteFolder)) {
                    continue;
                }
                removeFolder((SQLiteFolder)folder);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }
    
    public boolean retainAll(Collection<?> folders) {
        if (folders == null || folders.isEmpty()) {
            return false;
        }
        checkDb(db);
        db.beginTransaction();
        try {
            Iterator<SQLiteFolder> i = this.folders.iterator();
            while (i.hasNext()) {
                SQLiteFolder folder = i.next();
                if (!folders.contains(folder)) {
                    ActionDao.deleteActionFromFolder(db, folder.id, this.id);
                    i.remove();
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
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
