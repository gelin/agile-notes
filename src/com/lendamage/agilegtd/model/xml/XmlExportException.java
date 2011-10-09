package com.lendamage.agilegtd.model.xml;

import com.lendamage.agilegtd.model.ModelException;

public class XmlExportException extends ModelException {

    private static final long serialVersionUID = -230516649827728857L;
    
    public XmlExportException() {
        super();
    }

    public XmlExportException(String detailMessage) {
        super(detailMessage);
    }
    
    public XmlExportException(Throwable throwable) {
        super(throwable);
    }
    
    public XmlExportException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
}
