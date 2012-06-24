/*
    Agile GTD. Flexible Android implementation of GTD.
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

package com.lendamage.agilegtd.android;

import android.content.Context;
import android.content.Intent;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_ACTION_BODY;
import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_FOLDER_PATH;

/**
 *  Methods to share (in Android-way) actions and folders.
 */
class ShareUtils {

    static final String TEXT_MIME_TYPE = "text/plain";

    /**
     *  Opens chooser and starts the necessary activity to send the Action.
     *  @param context  current context
     *  @param title    ID of resource for chooser dialog title
     *  @param action   action to send
     */
    public static void sendAction(Context context, int title, Action action) {
        Intent chooser = Intent.createChooser(sendActionIntent(action), context.getString(title));
        context.startActivity(chooser);
    }

    /**
     *  Opens chooser and starts the necessary activity to send the Folder.
     *  @param context  current context
     *  @param title    ID of resource for chooser dialog title
     *  @param folder   folder to send
     */
    public static void sendFolder(Context context, int title, Folder folder) {
        Intent chooser = Intent.createChooser(sendFolderIntent(folder), context.getString(title));
        context.startActivity(chooser);
    }
    
    /**
     *  Returns the Intent from the Action.
     *  The Intent has:
     *  <ul>
     *      <li>Intent action: {@link Intent#ACTION_SEND}</li>
     *      <li>MIME type: text/plain</li>
     *      <li>EXTRA_SUBJECT: head of the Action</li>
     *      <li>EXTRA_TEXT: body of the Action</li>
     *  </ul>
     *  @param action   action to send
     *  @return intent to start the activity
     */
    public static Intent sendActionIntent(Action action) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TEXT_MIME_TYPE);
        String head = action.getHead();
        if (head != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, head);
        }
        String body = action.getBody();
        if (body != null) {
            intent.putExtra(Intent.EXTRA_TEXT, body);
        }
        return intent;
    }

    /**
     *  Opens the Add Action activity to add the received text into as the action in the specified folder.
     *  @param context  current context
     *  @param intent   intent to analyze
     *  @param folder   folder where to create the new Action
     *  @return true if the Action was created, false if the Intent is not recognized and cannot be converted to Action
     */
    public static void receiveActionIntent(Context context, Intent intent, Folder folder) {
        IntentUtils.startFolderActivity(context, FolderActivity.class, folder);
        Intent addIntent = new Intent(context, AddActionActivity.class);
        addIntent.putExtra(EXTRA_FOLDER_PATH, folder.getPath().toString());
        addIntent.putExtra(EXTRA_ACTION_BODY, getActionBody(intent));
        context.startActivity(addIntent);
    }

    /**
     *  Checks SEND intent and returns new action body.
     */
    static String getActionBody(Intent intent) {
        if (intent == null) {
            return "";
        }
        String head = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String body = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (head == null && body == null) {
            return "";
        }
        if (head == null) {
            return body;
        } else if (body == null) {
            return head;
        } else {
            return head + "\n\n" + body;
        }
    }

    /**
     *  Returns the Intent from the Folder.
     *  The Intent has:
     *  <ul>
     *      <li>Intent action: {@link Intent#ACTION_SEND}</li>
     *      <li>MIME type: text/plain</li>
     *      <li>EXTRA_SUBJECT: name of the Folder</li>
     *      <li>EXTRA_TEXT: recursive list of actions and subfolders</li>
     *  </ul>
     *  @param folder   folder to send
     *  @return intent to start the activity
     */
    public static Intent sendFolderIntent(Folder folder) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TEXT_MIME_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, folder.getName());
        intent.putExtra(Intent.EXTRA_TEXT, formatFolderContent(folder));
        return intent;
    }
    
    static String formatFolderContent(Folder folder) {
        StringBuilder result = new StringBuilder();
        formatFolderContent(result, folder, 0);
        return result.toString();
    }
    
    static void formatFolderContent(StringBuilder result, Folder folder, int depth) {
        for (Folder subfolder : folder.getFolders()) {
            appendHeadIndent(result, depth + 1);
            result.append(subfolder.getName());
            result.append("\n");
            formatFolderContent(result, subfolder, depth + 1);
        }
        for (Action action : folder.getActions()) {
            appendHeadIndent(result, depth + 1);
            result.append(action.getBody());
            result.append("\n");
        }
    }

    static void appendHeadIndent(StringBuilder result, int depth) {
        for (int i = 0; i < depth; i++) {
            result.append("*");
        }
        result.append(" ");
    }

    private ShareUtils() {
        //avoid instantiation
    }

    
}
