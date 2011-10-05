package com.lendamage.agilegtd.model;

/**
 *  The exception is thrown then the folder cannot be moved to a subfolder of itself.
 */
public class OuroborosException extends ModelException {

    private static final long serialVersionUID = 9055027206711254309L;
    
    public OuroborosException() {
        super();
    }

    public OuroborosException(String detailMessage,
            Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OuroborosException(String detailMessage) {
        super(detailMessage);
    }

    public OuroborosException(Throwable throwable) {
        super(throwable);
    }

}
