package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_IN_FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.SORT_ORDER_COLUMN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

class SQLiteAction implements Action {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID in the database */
    final long id;
    
    /** Action head */
    String head;
    /** Action body */
    String body;
    /** Set of folders */
    SQLiteFolderSet folders;
    
    SQLiteAction(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
        this.folders = new SQLiteFolderSet(db, id);
    }
    
    //@Override
    public String getHead() {
        return this.head;
    }
    
    //@Override
    public String getBody() {
        return this.body;
    }

    //@Override
    public Set<Folder> getFolders() {
        assert(this.id != 0);
        db.beginTransaction();
        try {
            Cursor cursor = ActionDao.selectFolders(db, this.id);
            List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
            while (cursor.moveToNext()) {
                result.add(FolderDao.getFolder(db, cursor));
            }
            Collections.sort(result, new SQLiteFolderComparator(db));
            this.folders.setFolders(result);
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return this.folders;
    }

    //@Override
    public Editor edit() {
        return new SQLiteActionEditor();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SQLiteAction other = (SQLiteAction) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return this.id + ":" + this.head;
    }
    
    private class SQLiteActionEditor implements Action.Editor {

        private String head = SQLiteAction.this.head;
        private String body = SQLiteAction.this.body;
        
        public Action.Editor setHead(String head) {
            this.head = head;
            return this;
        }

        public Action.Editor setBody(String body) {
            this.body = body;
            return this;
        }

        public void commit() {
            db.beginTransaction();
            try {
                ActionDao.updateAction(db, SQLiteAction.this, this.head, this.body);
                db.setTransactionSuccessful();
                SQLiteAction.this.head = this.head;
                SQLiteAction.this.body = this.body;
            } finally {
                db.endTransaction();
            }
        }

    }

}
