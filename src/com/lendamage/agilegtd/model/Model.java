package com.lendamage.agilegtd.model;

import java.util.List;

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
    
    /**
     *  Finds folders with specified type
     *  @return unmodifiable list
     */
    List<Folder> findFolders(FolderType type);

}
