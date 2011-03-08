package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;

public class SQLiteFolder implements Folder {

    /** ID in the database */
    final long id; 
    
    /** Full name */
    String fullName;
    /** Short name */
    String name;
    /** Type */
    FolderType type;
    
    SQLiteFolder(long id) {
        this.id = id;
    }
    
    @Override
    public String getFullName() {
        return this.fullName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public FolderType getType() {
        return this.type;
    }
    
    @Override
    public List<Folder> getFolders() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<Action> getActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Editor edit() {
        // TODO Auto-generated method stub
        return null;
    }

}
