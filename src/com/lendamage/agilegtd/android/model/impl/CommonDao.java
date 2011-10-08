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
