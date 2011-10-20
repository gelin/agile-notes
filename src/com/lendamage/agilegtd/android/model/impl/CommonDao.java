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

import android.database.sqlite.SQLiteDatabase;

class CommonDao {
    
    /**
     *  Check the DB state.
     *  Throws IllegalStateException if the DB is closed or read-only.
     */
    static void checkDb(SQLiteDatabase db) {
        assert(db != null);
        if (!db.isOpen()) {
            throw new IllegalStateException("database is closed");
        }
        if (db.isReadOnly()) {
            throw new IllegalStateException("database is read-only");
        }
    }
    
    private CommonDao() {
        //avoid instantiation
    }

}
