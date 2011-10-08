package com.lendamage.agilegtd.model.xml;

import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_ELEMENT;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_HEAD_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_ID_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_REF_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_ELEMENT;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_NAME_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_TYPE_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.NS;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelException;
import com.lendamage.agilegtd.model.Path;

public class XmlImporter {
    
    /** The XML parser */
    XmlPullParser parser;
    
    /** The model where to import */
    Model model;
    /** Current folder */
    Folder folder;
    /** Current action */
    Action action;
    
    /** Action IDs map */
    Map<String, Action> actions = new HashMap<String, Action>();
    
    public static void importModel(Model model, Reader input) throws ModelException {
        XmlPullParserFactory factory;
        XmlPullParser parser;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            //factory.setValidating(true);
            parser = factory.newPullParser();
            parser.setInput(input);
        } catch (Exception e) {
            throw new XmlImportException(e);
        }
        try {
            XmlImporter importer = new XmlImporter(parser, model);
            importer.parse();
        } catch (Exception e) {
            throw new XmlImportException(e, parser.getPositionDescription());
        }
    }
    
    XmlImporter(XmlPullParser parser, Model model) {
        this.parser = parser;
        this.model = model;
        this.folder = model.getRootFolder();
        this.folder.getFolders().clear();
        this.folder.getActions().clear();
    }
    
    void parse() throws XmlPullParserException, IOException {
        int eventType = this.parser.getEventType();
        do {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                processStartTag();
                break;
            case XmlPullParser.END_TAG:
                processEndTag();
                break;
            case XmlPullParser.TEXT:
                processText();
                break;
            }
            eventType = this.parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }
    
    void processStartTag() {
        if (!NS.equals(this.parser.getNamespace())) {
            return;
        }
        String name = this.parser.getName();
        if (FOLDER_ELEMENT.equals(name)) {
            startFolder();
        } else if (ACTION_ELEMENT.equals(name)) {
            startAction();
        }
    }
    
    void processEndTag() {
        if (!NS.equals(this.parser.getNamespace())) {
            return;
        }
        String name = this.parser.getName();
        if (FOLDER_ELEMENT.equals(name)) {
            endFolder();
        } else if (ACTION_ELEMENT.equals(name)) {
            endAction();
        }
    }
    
    void processText() {
        if (this.action == null) {
            return;
            //throw new XmlImportException("unexpected content", this.parser.getPositionDescription());
        }
        textAction();
    }
    
    void startFolder() {
        String name = this.parser.getAttributeValue(null, FOLDER_NAME_ATTRIBUTE);
        String typeString = this.parser.getAttributeValue(null, FOLDER_TYPE_ATTRIBUTE);
        FolderType type = null;
        if (typeString != null) {
            try {
                type = FolderType.valueOf(typeString);
            } catch (IllegalArgumentException e) {
                throw new XmlImportException("invalid folder type: " + typeString, e, 
                        this.parser.getPositionDescription());
            }
        }
        if (FolderType.ROOT.equals(type)) {
            if (!FolderType.ROOT.equals(this.folder.getType())) {
                throw new XmlImportException("ROOT folder must be root and only one",
                        this.parser.getPositionDescription());
            }
            return; //root folder already exists in the model
            //TODO: is it relates only to SQLite model?
        }
        this.folder = this.folder.newFolder(name, type);    //creating, entering
    }
    
    void endFolder() {
        Path path = this.folder.getPath();
        this.folder = this.model.getFolder(path.getParent());
    }
    
    void startAction() {
        String id = this.parser.getAttributeValue(null, ACTION_ID_ATTRIBUTE);
        String ref = this.parser.getAttributeValue(null, ACTION_REF_ATTRIBUTE);
        String head = this.parser.getAttributeValue(null, ACTION_HEAD_ATTRIBUTE);
        if (id != null) {
            if (head == null) {
                throw new XmlImportException("action must have a head", this.parser.getPositionDescription());
            }
            this.action = this.folder.newAction(head, null);
            if (this.actions.containsKey(id)) {
                throw new XmlImportException("duplicated action ID: " + id, this.parser.getPositionDescription());
            }
            this.actions.put(id, this.action);
        } else {
            if (ref == null) {
                throw new XmlImportException("action must have id or ref", this.parser.getPositionDescription());
            }
            Action action = this.actions.get(ref);
            if (action == null) {
                throw new XmlImportException("action must have id or ref", this.parser.getPositionDescription());
            }
            this.folder.getActions().add(action);
        }
    }
    
    void textAction() {
        String body = this.parser.getText();
        this.action.edit().setBody(body).commit();
    }
    
    void endAction() {
        this.action = null;
    }

}
