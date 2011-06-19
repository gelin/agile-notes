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
     */
    Folder getFolder(Path fullPath);
    
    /**
     *  Creates new Action.
     *  It is the only way to create the Action.
     */
    Action newAction(String head, String body);
    
    /**
     *  Creates new Folder.
     *  It is the only way to create the Folder.
     *  If the folder with specified name is already exists, the folder from database is returned.
     */
    Folder newFolder(Path fullPath, FolderType type);

}
