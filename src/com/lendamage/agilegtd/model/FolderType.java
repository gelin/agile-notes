package com.lendamage.agilegtd.model;

/**
 *  Enumeration of supported folder types.
 */
public enum FolderType {
    
    ROOT,
    INBOX,
    TRASH,
    COMPLETED,
    PROJECTS,
    PROJECT,
    CONTEXTS,
    CONTEXT,
    PRIORITIES,
    PRIORITY,
    PERSONS,
    PERSON;
    
    /**
     *  Returns the recommended child folder type for this parent folder type
     *  @return recommended type or null 
     */
    public FolderType getChildType() {
        switch (this) {
        case PROJECTS:
            return PROJECT;
        case CONTEXTS:
            return CONTEXT;
        case PRIORITIES:
            return PRIORITY;
        case PERSONS:
            return PERSON;
        default:
            return null;
        }
    }

}
