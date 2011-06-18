package com.lendamage.agilegtd.android.model.impl;

import java.util.Set;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

public class SQLiteAction implements Action {

    /** ID in the database */
    final long id;
    
    /** Action head */
    String head;
    /** Action body */
    String body;
    
    SQLiteAction(long id) {
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
        // TODO Auto-generated method stub
        return null;
    }

}
