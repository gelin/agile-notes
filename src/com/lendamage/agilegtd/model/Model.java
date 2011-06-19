package com.lendamage.agilegtd.model;

/**
 *  Agile GTD model
 */
public interface Model {
    
    /**
     *  Returns the root folder.
     */
    Folder getRootFolder();
    
    /**
     *  Returns the folder by the full name.
     *  If the folder is not found, null is returned.
     */
    Folder getFolder(Path fullPath);
    
    /**
     *  Creates new Action.
     *  It is the only way to create the Action.
     *  The new action is not assigned to any folder.
     */
    Action newAction(String head, String body);
    
    /**
     *  Creates new Folder.
     *  It is the only way to create the Folder.
     *  The new folder is assigned as subfolder of the root folder, the assignment should be changed.
     */
    Folder newFolder(String name, FolderType type);

}
