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
