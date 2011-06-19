package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;

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
    
    SQLiteFolder(SQLiteDatabase db, long id) {
        this.db = db;
        this.id = id;
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
        assert(id != 0);
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                FOLDER_ID_COLUMN + " = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);
        List<Folder> result = new ArrayList<Folder>();
        while (cursor.moveToNext()) {
            result.add(SQLiteUtils.getFolder(db, cursor));
        }
        return result;
    }
    
    //@Override
    public List<Action> getActions() {
        // TODO Auto-generated method stub
        return null;
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

}
