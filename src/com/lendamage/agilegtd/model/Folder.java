package com.lendamage.agilegtd.model;

import java.util.List;

/**
 *  Agile GTD folder.<br>
 *  Each folder can contain other folders and actions.
 */

public interface Folder extends Entity<Folder.Editor> {
    
    /**
     *  Creates a new Action.<br>
     *  It is the only way to create the Action.<br>
     *  The new action is assigned to this folder and added to the end of list of actions returned by {@code #getActions()}.
     */
    Action newAction(String head, String body);
    
    /**
     *  Creates a new Folder.<br>
     *  It is the only way to create the Folder.<br>
     *  The new folder is assigned as subfolder of the current folder 
     *  and added to the end of list of folders returned by {@code #getFolders()}.<br>
     *  If the folder with the same name already exists in this folder, 
     *  the existed folder is returned. No new folder is created in this case.
     *  @throws FolderAlreadyExistsException    if the folder with the same name already exists
     */
    Folder newFolder(String name, FolderType type) throws FolderAlreadyExistsException;
    
    /**
     *  Lists subfolders.<br>
     *  The returned list can be modified.<br>
     *  If the folder is deleted from the list, it's deleted from the model.<br>
     *  If the folder is added to the list, it's assigned as a subfolder of this folder, the old assignment of the folder is removed.<br>
     *  If the folder replaces already existed folder in the list, the replaced folder is deleted from the model, and the
     *  new folder is assigned as a subfolder of this folder.<br>
     *  The list doesn't contain duplicates, if you add a folder which already exists in the list, 
     *  the order of elements will be changed, but not the list length.<br>
     *  The list operations can throw FolderAlreadyExistsException if the folder with the same name already exists.
     */
    List<Folder> getFolders();
    
    /**
     *  Lists actions of this folder.<br>
     *  The returned list can be modified.<br>
     *  If the action is deleted from the list, the assignment of the action to this folder is removed.<br>
     *  If the action is added to the list, the new assignment of the action to this folder is added.<br>
     *  The list doesn't contain duplicates, if you add an action which already exists in the list, 
     *  the order of elements will be changed, but not the list length.
     */
    List<Action> getActions();
    
    /**
     *  Returns short name of the folder.
     */
    String getName();
    
    /**
     *  Returns full path of the folder.<br>
     *  The full path displays the position of the folder in hierarchy, it's the read-only property.<br>
     *  The full path is changed when the folder is moved as subfolders of another folders.
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
        Editor setName(String name);
        
        /**
         *  Updates the folder type.
         */
        Editor setType(FolderType type);
        
    }

}
