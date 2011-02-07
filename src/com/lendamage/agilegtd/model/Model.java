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

}
