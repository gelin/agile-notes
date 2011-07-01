package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_IN_FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.SORT_ORDER_COLUMN;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

public class SQLiteFolder implements Folder {

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
    public List<Folder> getFolders() {
        assert(this.id != 0);
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                FOLDER_ID_COLUMN + " = ?",
                new String[] {String.valueOf(this.id)},
                null,
                null,
                SORT_ORDER_COLUMN + " ASC");
        List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
        while (cursor.moveToNext()) {
            result.add(FolderDao.getFolder(db, cursor));
        }
        this.folders.setFolders(result);
        return this.folders;
    }
    
    //@Override
    public List<Action> getActions() {
        assert(this.id != 0);
        Cursor cursor = db.query(
                ACTION_TABLE + " a JOIN " + ACTION_IN_FOLDER_TABLE + " af " +
                        "ON (a." + ID_COLUMN + " = af." + ACTION_ID_COLUMN + ")", 
                null, 
                FOLDER_ID_COLUMN + " = ?",
                new String[] {String.valueOf(this.id)},
                null,
                null,
                SORT_ORDER_COLUMN + " ASC");
        List<SQLiteAction> result = new ArrayList<SQLiteAction>();
        while (cursor.moveToNext()) {
            result.add(ActionDao.getAction(db, cursor));
        }
        this.actions.setActions(result);
        return this.actions;
    }

    //@Override
    public Editor edit() {
        // TODO Auto-generated method stub
        return null;
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

}
