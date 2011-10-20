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

import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_ELEMENT;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_HEAD_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_ID_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.ACTION_REF_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.AGILEGTD_ELEMENT;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_ELEMENT;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_NAME_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.FOLDER_TYPE_ATTRIBUTE;
import static com.lendamage.agilegtd.model.xml.XmlNames.NS;
import static com.lendamage.agilegtd.model.xml.XmlNames.VERSION;
import static com.lendamage.agilegtd.model.xml.XmlNames.VERSION_ATTRIBUTE;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelException;

public class XmlExporter {
    
    /** The XML serializer */
    XmlSerializer serializer;
    
    /** The model where to import */
    Model model;
    
    /** Action IDs map */
    Map<Action, String> actions = new HashMap<Action, String>();
    /** Current action ID counter */
    int actionId = 0;
    
    public static void exportModel(Model model, Writer output) throws ModelException {
        XmlPullParserFactory factory;
        XmlSerializer serializer;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            //factory.setValidating(true);
            serializer = factory.newSerializer();
            serializer.setOutput(output);
        } catch (Exception e) {
            throw new XmlExportException(e);
        }
        try {
            XmlExporter exporter = new XmlExporter(model, serializer);
            exporter.writeModel();
        } catch (Exception e) {
            throw new XmlExportException(e);
        }
    }
    
    XmlExporter(Model model, XmlSerializer serializer) {
        this.model = model;
        this.serializer = serializer;
    }
    
    void writeModel() throws IOException {
        this.serializer.startDocument(null, null);
        this.serializer.setPrefix("", NS);
        this.serializer.startTag(NS, AGILEGTD_ELEMENT);
        this.serializer.attribute(null, VERSION_ATTRIBUTE, VERSION);
        writeFolder(this.model.getRootFolder());
        this.serializer.endTag(NS, AGILEGTD_ELEMENT);
        this.serializer.endDocument();
    }
    
    void writeFolder(Folder folder) throws IOException {
        this.serializer.startTag(NS, FOLDER_ELEMENT);
        this.serializer.attribute(null, FOLDER_NAME_ATTRIBUTE, folder.getName());
        if (folder.getType() != null) {
            this.serializer.attribute(null, FOLDER_TYPE_ATTRIBUTE, String.valueOf(folder.getType()));
        }
        for (Folder subfolder : folder.getFolders()) {
            writeFolder(subfolder);
        }
        for (Action action : folder.getActions()) {
            writeAction(action);
        }
        this.serializer.endTag(NS, FOLDER_ELEMENT);
    }
    
    void writeAction(Action action) throws IOException {
        this.serializer.startTag(NS, ACTION_ELEMENT);
        String id = this.actions.get(action);
        if (id == null) {
            id = nextActionId();
            this.serializer.attribute(null, ACTION_ID_ATTRIBUTE, id);
            this.serializer.attribute(null, ACTION_HEAD_ATTRIBUTE, action.getHead());
            if (action.getBody() != null) {
                this.serializer.text(action.getBody());
            }
            this.actions.put(action, id);
        } else {
            this.serializer.attribute(null, ACTION_REF_ATTRIBUTE, id);
        }
        this.serializer.endTag(NS, ACTION_ELEMENT);
    }
    
    String nextActionId() {
        String result = "a" + this.actionId;
        this.actionId++;
        return result;
    }

}
