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
import android.util.Log;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import java.util.Set;

import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_ACTION_POSITION;
import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_FOLDER_PATH;
import static com.lendamage.agilegtd.android.Tag.TAG;

/**
 *  Base class for activites which works with the action.
 *  It takes the folder path and the action position withing the folder in intent.
 */
abstract class AbstractActionActivity extends AbstractFolderActivity {
    
    /** Current action */
    private Action action;

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (getFolder() == null) {
            finish();
            return;
        }
        if (getAction() == null) {
            getActionFromIntent();
        } else {
            restoreAction();
        }
    }
    
    void getActionFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ACTION_POSITION)) {
            int position = intent.getIntExtra(EXTRA_ACTION_POSITION, 0);
            try {
                setAction(getFolder().getActions().get(position));
            } catch (IndexOutOfBoundsException e) {
                Log.w(TAG, "action at " + position + " in " + getFolder().getPath() + " is not found");
                finish();
            }
        }
    }
    
    void restoreAction() {
        setAction(getModel().findAction(getAction()));
        if (getAction() == null) {
            Log.w(TAG, "action in " + getFolder().getPath() + " cannot be restored");
            finish();
            return;
        }
        Set<Folder> folders = getAction().getFolders();
        if (folders.isEmpty()) {
            Log.w(TAG, "action has no folders, illegal model state");
            finish();
            return;
        }
        Folder folder = folders.iterator().next();
        Intent intent = getIntent();
        intent.putExtra(EXTRA_FOLDER_PATH, folder.getPath().toString());
        intent.putExtra(EXTRA_ACTION_POSITION, folder.getActions().indexOf(getAction()));
        setFolder(folder);
    }

}