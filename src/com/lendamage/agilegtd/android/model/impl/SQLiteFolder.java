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

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.ModelSettings;
import com.lendamage.agilegtd.model.Path;

import java.util.ArrayList;
import java.util.List;

import static com.lendamage.agilegtd.android.model.impl.CommonDao.checkDb;

class SQLiteFolder implements Folder {

    /** Link to model */
    transient SQLiteModel model;
    /** DB handler */
    transient SQLiteDatabase db;
    /** ID in the database */
    final long id;
    /** True if New item should be added to the first */
    boolean newItemPositionFirst = false;
    
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
    
    SQLiteFolder(SQLiteModel model, long id) {
        this.model = model;
        updateSettings(model.getSettings());
        this.db = model.db;
        this.id = id;
        this.folders = new SQLiteFolderList(this.model, id);
        this.actions = new SQLiteActionList(this.db, id);
    }

    /**
     *  Updated the folder internals based on the model settings.
     */
    void updateSettings(ModelSettings settings) {
        this.newItemPositionFirst = ModelSettings.NewItemPosition.FIRST.equals(settings.getNewItemPosition());
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
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            SQLiteAction result = ActionDao.insertAction(this.model, this.id, head, body);
            result.head = head;
            result.body = body;
            if (this.newItemPositionFirst) {
                getActions().add(0, result);    //TODO optimize?
            }
            this.db.setTransactionSuccessful();
            return result;
        } finally {
            this.db.endTransaction();
        }
    }

    //@Override
    public Folder newFolder(String name, FolderType type) throws FolderAlreadyExistsException {
        assert(name != null);
        if (FolderType.ROOT.equals(type)) {
            throw new FolderAlreadyExistsException("root already exists");
        }
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            SQLiteFolder result = FolderDao.insertFolder(this.model, this.id, name, type);
            this.db.setTransactionSuccessful();
            return result;
        } catch (SQLiteConstraintException ce) {
            throw new FolderAlreadyExistsException(ce);
        } finally {
            this.db.endTransaction();
        }
    }
    
    //@Override
    public List<Folder> getFolders() {
        assert(this.id != 0);
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectFolders(this.db, this.id);
            List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
            while (cursor.moveToNext()) {
                result.add(FolderDao.getFolder(this.model, cursor));
            }
            this.folders.setFolders(result);
            cursor.close();
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
        return this.folders;
    }
    
    //@Override
    public List<Action> getActions() {
        assert(this.id != 0);
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectActions(this.db, this.id);
            List<SQLiteAction> result = new ArrayList<SQLiteAction>();
            while (cursor.moveToNext()) {
                result.add(ActionDao.getAction(this.model, cursor));
            }
            this.actions.setActions(result);
            cursor.close();
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
        return this.actions;
    }
    
    public FolderTree getFolderTree() {
        return new SQLiteFolderTree(this);
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
            checkDb(db);
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
