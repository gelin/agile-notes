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

class XmlNames {
    
    /** Namespace */
    static final String NS = "http://lendamage.com/agilegtd/xml/model/1";
    
    /** Supported version */
    static final String VERSION = "1";
    
    /** Root element */
    static final String AGILEGTD_ELEMENT = "agilegtd";
    
    /** Version attribute */
    static final String VERSION_ATTRIBUTE = "version";
    
    /** Folder element */
    static final String FOLDER_ELEMENT = "folder";
    
    /** Folder name attribute */
    static final String FOLDER_NAME_ATTRIBUTE = "name";
    
    /** Folder type attribute */
    static final String FOLDER_TYPE_ATTRIBUTE = "type";
    
    /** Action element */
    static final String ACTION_ELEMENT = "action";
    
    /** Action id attribute */
    static final String ACTION_ID_ATTRIBUTE = "id";
    
    /** Action ref attribute */
    static final String ACTION_REF_ATTRIBUTE = "ref";
    
    /** Action head attribute */
    static final String ACTION_HEAD_ATTRIBUTE = "head";
    
    private XmlNames() {
        //avoid instantiation
    }

}
