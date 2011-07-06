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
     *  Returns the folder by the full name.<br>
     *  If the folder is not found, null is returned.
     */
    Folder getFolder(Path fullPath);

}
