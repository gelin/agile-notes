package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.lendamage.agilegtd.model.Folder;

class SQLiteFolderListIterator implements Iterator<Folder> {

    /** Backed list */
    SQLiteFolderList list;
    /** Current location */
    int location = -1;
    
    SQLiteFolderListIterator(SQLiteFolderList list) {
        this.list = list;
    }
    
    public boolean hasNext() {
        return location < this.list.folders.size() - 1;
    }

    public Folder next() {
        location ++;
        if (!inBounds()) {
            throw new NoSuchElementException();
        }
        return this.list.folders.get(location);
    }

    public void remove() {
        if (!inBounds()) {
            throw new IllegalStateException();
        }
        this.list.remove(location);
    }
    
    boolean inBounds() {
        return location >= 0 && location < this.list.folders.size();
    }

}
