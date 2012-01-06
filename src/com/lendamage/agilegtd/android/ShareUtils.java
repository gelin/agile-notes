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

/**
 *  Methods to share (in Android-way) actions and folders.
 */
class ShareUtils {

    static final String TEXT_MIME_TYPE = "text/plain";
    
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
     *  Creates the action received from the {@link Intent#ACTION_SEND} intent in the specified folder.
     *  @param intent   intent to analyze
     *  @param folder   folder where to create the new Action
     *  @return true if the Action was created, false if the Intent is not recognized and cannot be converted to Action
     */
    public static boolean receiveActionIntent(Intent intent, Folder folder) {
        if (intent == null) {
            return false;
        }
        if (!Intent.ACTION_SEND.equals(intent.getAction())) {
            return false;
        }
        if (!TEXT_MIME_TYPE.equals(intent.getType())) {
            return false;
        }
        String head = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String body = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (head == null && body == null) {
            return false;
        }
        if (head == null) {
            head = ActionHelper.getHeadFromBody(body);
        } else if (body == null) {
            body = head;
        } else {
            body = head + "\n\n" + body;
        }
        folder.newAction(head, body);
        return true;
    }
    
    private ShareUtils() {
        //avoid instantiation
    }

    
}
