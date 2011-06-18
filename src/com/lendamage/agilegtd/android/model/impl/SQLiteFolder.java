package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

public class SQLiteFolder implements Folder {

    /** ID in the database */
    final long id; 
    
    /** Full path */
    Path path;
    /** Short name */
    String name;
    /** Type */
    FolderType type;
    
    SQLiteFolder(long id) {
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
        // TODO Auto-generated method stub
        return null;
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

}
