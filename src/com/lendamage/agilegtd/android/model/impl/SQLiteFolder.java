package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

class SQLiteFolder implements Folder {

    /** DB handler */
    transient SQLiteDatabase db;
    /** ID in the database */
    final long id; 
    
    /** Full path */
    Path path;
    /** Short name */
    String name;
    /** Type */
    FolderType type;
    /** Sort older */
    long sortOrder;
    /** List of folders */
    SQLiteFolderList folders;
    /** List of actions */
    SQLiteActionList actions;
    
    SQLiteFolder(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
        this.folders = new SQLiteFolderList(db, id);
        this.actions = new SQLiteActionList(db, id);
    }
    
    //@Override
    public Path getPath() {
        return this.path;
    }

    //@Override
    public String getName() {
        return this.name;
    }

    //@Override
    public FolderType getType() {
        return this.type;
    }
    
    //@Override
    public Action newAction(String head, String body) {
        assert(head != null);
        db.beginTransaction();
        try {
            SQLiteAction result = ActionDao.insertAction(db, this.id, head, body);
            result.head = head;
            result.body = body;
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    //@Override
    public Folder newFolder(String name, FolderType type) throws FolderAlreadyExistsException {
        assert(name != null);
        if (FolderType.ROOT.equals(type)) {
            throw new FolderAlreadyExistsException("root already exists");
        }
        db.beginTransaction();
        try {
            SQLiteFolder result = FolderDao.insertFolder(db, this.id, name, type);
            db.setTransactionSuccessful();
            return result;
        } catch (SQLiteConstraintException ce) {
            throw new FolderAlreadyExistsException(ce);
        } finally {
            db.endTransaction();
        }
    }
    
    //@Override
    public List<Folder> getFolders() {
        assert(this.id != 0);
        db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectFolders(db, this.id);
            List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
            while (cursor.moveToNext()) {
                result.add(FolderDao.getFolder(db, cursor));
            }
            this.folders.setFolders(result);
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return this.folders;
    }
    
    //@Override
    public List<Action> getActions() {
        assert(this.id != 0);
        db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectActions(db, this.id);
            List<SQLiteAction> result = new ArrayList<SQLiteAction>();
            while (cursor.moveToNext()) {
                result.add(ActionDao.getAction(db, cursor));
            }
            this.actions.setActions(result);
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return this.actions;
    }

    //@Override
    public Editor edit() {
        if (this.path.isRoot()) {
            return new SQLiteRootFolderEditor();
        }
        return new SQLiteFolderEditor();
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
        SQLiteFolder other = (SQLiteFolder) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return this.id + ":" + this.name;
    }
    
    private class SQLiteFolderEditor implements Folder.Editor {

        private String name = SQLiteFolder.this.name;
        private FolderType type = SQLiteFolder.this.type;
        
        public Folder.Editor setName(String name) {
            this.name = name;
            return this;
        }

        public Folder.Editor setType(FolderType type) {
            this.type = type;
            return this;
        }

        public void commit() {
            db.beginTransaction();
            try {
                FolderDao.updateFolder(db, SQLiteFolder.this, this.name, this.type);
                db.setTransactionSuccessful();
                SQLiteFolder.this.name = this.name;
                SQLiteFolder.this.path = SQLiteFolder.this.path.replaceLastSegment(this.name);
                SQLiteFolder.this.type = this.type;
            } catch (SQLiteConstraintException ce) {
                throw new FolderAlreadyExistsException(ce);
            } finally {
                db.endTransaction();
            }
        }

    }
    
    /**
     *  Editor for root folder does nothing.
     */
    private class SQLiteRootFolderEditor implements Folder.Editor {
        public Folder.Editor setName(String name) {
            return this;
        }
        public Folder.Editor setType(FolderType type) {
            return this;
        }
        public void commit() {
        }
        
    }

}
