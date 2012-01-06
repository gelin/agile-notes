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

import android.content.Intent;
import android.widget.Toast;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;

import java.util.ArrayList;
import java.util.List;

/**
 *  Activity to add a new action from the {@link Intent#ACTION_SEND} intent.
 *  Creates the action in the {@link FolderType#INBOX} folder or in the {@link FolderType#ROOT} folder. 
 *  Has no UI, but displays toast message.
 */
public class ActionReceiveActivity extends AbstractModelActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int message = R.string.action_receive_failure;
        List<Folder> inboxes = findInbox();
        for (Folder inbox : inboxes) {
            if (ShareUtils.receiveActionIntent(intent, inbox)) {
                if (inbox.getPath().isRoot()) {
                    message = R.string.action_received_to_root;
                } else {
                    message = R.string.action_received_to_inbox;
                }
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    List<Folder> findInbox() {
        List<Folder> result = new ArrayList<Folder>();
        result.addAll(this.model.findFolders(FolderType.INBOX));
        if (result.isEmpty()) {
            result.add(this.model.getRootFolder());
        }
        return result;
    }

}