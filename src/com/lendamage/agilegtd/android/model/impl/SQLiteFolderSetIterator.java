package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.lendamage.agilegtd.model.Folder;

class SQLiteFolderSetIterator implements Iterator<Folder> {

    /** Backed set */
    SQLiteFolderSet set;
    /** Current location */
    int location = -1;
    
    SQLiteFolderSetIterator(SQLiteFolderSet set) {
        this.set = set;
    }
    
    public boolean hasNext() {
        return location < this.set.folders.size() - 1;
    }

    public Folder next() {
        location ++;
        if (!inBounds()) {
            throw new NoSuchElementException();
        }
        return this.set.folders.get(location);
    }

    public void remove() {
        if (!inBounds()) {
            throw new IllegalStateException();
        }
        this.set.remove(location);
    }
    
    boolean inBounds() {
        return location >= 0 && location < this.set.folders.size();
    }

}
