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
     *  If the folder is deleted from the list, it's deleted from the model.
     *  If the folder is added to the list, it's assigned as a subfolder of this folder, the old possible assignment of the folder is removed.
     *  If the folder replaces already existed folder in the list, the replaced folder is deleted from the model, and the
     *  new folder is assigned as a subfolder of this folder.
     *  The list doesn't contain duplicates, if you add a folder which already exists in the list, 
     *  the order of elements will be changed, but not the list length.
     */
    List<Folder> getFolders();
    
    /**
     *  Lists actions of this folder.
     *  The returned list can be modified.
     *  If the action is deleted from the list, the assignment of the action to this folder is removed.
     *  If the action is added to the list, the new assignment of the action to this folder is added.
     *  The list doesn't contain duplicates, if you add an action which already exists in the list, 
     *  the order of elements will be changed, but not the list length.
     */
    List<Action> getActions();
    
    /**
     *  Returns short name of the folder.
     */
    String getName();
    
    /**
     *  Returns full path of the folder.
     *  The full path displays the position of the folder in hierarchy, it's the read-only property.
     *  The full path is changes when the folder is moved as subfolders of another folders.
     */
    Path getPath();
    
    /**
     *  Returns the folder type or null.
     */
    FolderType getType();
    
    public interface Editor extends Entity.Editor {
        
        /**
         *  Updates the folder name.
         */
        void setName(String name);
        
        /**
         *  Updates the folder type.
         */
        void setType(FolderType type);
        
    }

}
