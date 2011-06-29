package com.lendamage.agilegtd.android.model.impl;

import java.util.Iterator;

import com.lendamage.agilegtd.model.Folder;

class SQLiteFolderListIterator implements Iterator<Folder> {

    /** Backed list */
    SQLiteFolderList list;
    /** Current location */
    int location = 0;
    
    SQLiteFolderListIterator(SQLiteFolderList list) {
        this.list = list;
    }
    
    public boolean hasNext() {
        return location < this.list.folders.size();
    }

    public Folder next() {
        Folder result = this.list.folders.get(location);
        location++;
        return result;
    }

    public void remove() {
        this.list.remove(location);
    }

}
