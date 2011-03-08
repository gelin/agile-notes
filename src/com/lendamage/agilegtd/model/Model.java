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
    Folder getFolder(String fullName);
    
    /**
     *  Creates new Action.
     *  It is the only way to create the Action.
     */
    Action newAction(String head, String body);
    
    /**
     *  Creates new Folder.
     *  It is the only way to create the Folder.
     */
    Folder newFolder(String fullName, FolderType type);

}
