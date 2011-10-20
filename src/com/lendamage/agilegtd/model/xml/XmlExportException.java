/*
    Agile GTD. Flexible implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
