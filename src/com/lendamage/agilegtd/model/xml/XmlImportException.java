package com.lendamage.agilegtd.model.xml;

import com.lendamage.agilegtd.model.ModelException;

public class XmlImportException extends ModelException {

    private static final long serialVersionUID = -230516649827728857L;
    
    String positionDescription;

    public XmlImportException() {
        super();
    }

    public XmlImportException(String detailMessage) {
        super(detailMessage);
    }
    
    public XmlImportException(String detailMessage, String positionDescription) {
        super(detailMessage + ": " + positionDescription);
        this.positionDescription = positionDescription;
    }

    public XmlImportException(Throwable throwable) {
        super(throwable);
    }
    
    public XmlImportException(Throwable throwable, String positionDescription) {
        super(positionDescription, throwable);
        this.positionDescription = positionDescription;
    }

    public XmlImportException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    public XmlImportException(String detailMessage, Throwable throwable, String positionDescription) {
        super(detailMessage + ": " + positionDescription, throwable);
        this.positionDescription = positionDescription;
    }
    
    public String getPositionDescription() {
        return this.positionDescription;
    }

}
