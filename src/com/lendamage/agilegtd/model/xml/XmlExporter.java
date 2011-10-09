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
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelException;
import com.lendamage.agilegtd.model.Path;

public class XmlExporter {
    
    /** The XML serializer */
    XmlSerializer serializer;
    
    /** The model where to import */
    Model model;
    
    /** Action IDs map */
    Map<String, Action> actions = new HashMap<String, Action>();
    
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
        this.serializer.startDocument(null, false);
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
        this.serializer.endTag(NS, FOLDER_ELEMENT);
    }

}
