package com.lendamage.agilegtd.model;

/**
 *  The exception is thrown then the folder cannot be created or moved because it already exists.
 */
public class FolderAlreadyExistsException extends ModelException {

    private static final long serialVersionUID = 9055027206711254309L;
    
    public FolderAlreadyExistsException() {
        super();
    }

    public FolderAlreadyExistsException(String detailMessage,
            Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FolderAlreadyExistsException(String detailMessage) {
        super(detailMessage);
    }

    public FolderAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }

}
