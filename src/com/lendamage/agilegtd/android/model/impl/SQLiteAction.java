package com.lendamage.agilegtd.android.model.impl;

import java.util.Set;

import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

public class SQLiteAction implements Action {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID in the database */
    final long id;
    
    /** Action head */
    String head;
    /** Action body */
    String body;
    
    SQLiteAction(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
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
        // TODO Auto-generated method stub
        return null;
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
