package com.lendamage.agilegtd.model;

/**
 *  Common model exception.
 */
public class ModelException extends RuntimeException {

    private static final long serialVersionUID = -87158926972832849L;

    public ModelException() {
        super();
    }

    public ModelException(String detailMessage) {
        super(detailMessage);
    }

    public ModelException(Throwable throwable) {
        super(throwable);
    }

    public ModelException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
