package com.lendamage.agilegtd.model;

import java.util.List;

/**
 *  Agile GTD folder.
 *  Each folder can contain other folders and actions.
 */
public interface Folder extends Entity<Folder.Editor> {
    
    /**
     *  Lists subfolders.
     *  The returned list can be modified.
     */
    List<Folder> getFolders();
    
    /**
     *  Lists actions of this folder.
     *  The returned list can be modified.
     */
    List<Action> getActions();
    
    /**
     *  Returns short name of the folder.
     */
    String getName();
    
    /**
     *  Returns full name of the folder.
     */
    String getFullName();
    
    /**
     *  Returns the folder type or null.
     */
    FolderType getType();
    
    public interface Editor extends Entity.Editor {
        
        /**
         *  Updates the folder name.
         */
        void setName(String name);
        
    }

}
