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
