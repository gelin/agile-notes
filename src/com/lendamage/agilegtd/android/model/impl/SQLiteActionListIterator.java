package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

class SQLiteActionListIterator implements Iterator<Action> {

    /** Backed list */
    SQLiteActionList list;
    /** Current location */
    int location = -1;
    
    SQLiteActionListIterator(SQLiteActionList list) {
        this.list = list;
    }
    
    public boolean hasNext() {
        return location < this.list.actions.size() - 1;
    }

    public Action next() {
        location ++;
        if (!inBounds()) {
            throw new NoSuchElementException();
        }
        return this.list.actions.get(location);
    }

    public void remove() {
        if (!inBounds()) {
            throw new IllegalStateException();
        }
        this.list.remove(location);
    }
    
    boolean inBounds() {
        return location >= 0 && location < this.list.actions.size();
    }

}
